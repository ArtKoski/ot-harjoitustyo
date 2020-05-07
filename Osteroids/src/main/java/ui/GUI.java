/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import gamelogic.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import scores.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author artkoski
 */
public class GUI extends Application {

    SheetsLeaderBoards hiScores;
    public static int LEVEYS = 600;
    public static int KORKEUS = 400;
    private Scene peliNakyma;
    private Scene alkuNakyma;
    private Scene gameOverScene;
    private Scene instructionsScene;
    Pane peliRuutu;
    List<Bullet> ammo = new ArrayList<>();
    List<Bullet> enemyAmmo = new ArrayList<>();
    List<Sprite> enemies = new ArrayList<>();
    int round = 0;
    Hero hero;
    int score = 0;
    Random random;
    long timer;
    boolean cooldown = false;
    long cdTimer = 0;
    boolean cleared;
    long roundTimerStart;
    long roundTimerEnd;
    Button startGameButton;
    Button instructionsButton;
    Button startGameFromTut;
    Button tryAgainButton;
    Button hiScoresButton;
    Button exitButton;
    Button saveButton;
    Label gameOverLabel = new Label();
    Text roundATM = new Text();
    Text scoreText = new Text();

    final PriorityQueue<Score> bestTimes = new PriorityQueue<Score>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage firstStage) {

        hiScores = new SheetsLeaderBoards();
        hiScores.init();
        
        alkuNakyma = new Scene(alkuRuutu());

        startGameButton.setOnAction(click -> {
            roundTimerStart = System.nanoTime();
            startGame(firstStage);
        });

        instructionsButton.setOnAction(click -> {
            firstStage.setScene(instructionsScene);
        });

        instructionsScene = new Scene(instructionsScene());

        startGameFromTut.setOnAction(click -> {
            roundTimerStart = System.nanoTime(); // TEE VIELÄ LOPPUTIMER JA LASKE TULOS KUN PELI PÄÄÄTTTYY
            startGame(firstStage);
        });

        firstStage.setScene(alkuNakyma);
        firstStage.show();
    }

    public void startGame(Stage ikkuna) {
        random = new Random();

        //ConfigScene, change enemy damage ETC !!
        //gameScene
        peliRuutu = new Pane();
        peliRuutu.setPrefSize(LEVEYS, KORKEUS);

        //ADD HERO - can be done elsewhere
        hero = new Hero(150, 100);
        peliRuutu.getChildren().add(hero.getPoly());

        //Start Game 
        tut(peliRuutu);

        //ROUND/SCORE TEXT  -- own method 4 this
        roundATM.setText("Tutorial");
        scoreText.setText("Points: " + score);
        roundATM.setText(String.valueOf(score));
        BorderPane roundsPane = new BorderPane();
        roundsPane.setLeft(roundATM); //FIX THIS
        roundsPane.setRight(scoreText);
        peliRuutu.getChildren().add(roundsPane);

        peliNakyma = new Scene(peliRuutu);
        Rectangle heroHPbar = new Rectangle(200.0, 50.0, Color.RED); //ei käytössä vielä

        Map<KeyCode, Boolean> painetutNapit = new HashMap<>();

        peliNakyma.setOnKeyPressed(event -> {
            painetutNapit.put(event.getCode(), Boolean.TRUE);
        });

        peliNakyma.setOnKeyReleased(event -> {
            painetutNapit.put(event.getCode(), Boolean.FALSE);
        });

        new AnimationTimer() {
            @Override
            public void handle(long nykyhetki) {
                if (painetutNapit.getOrDefault(KeyCode.LEFT, false)) {
                    hero.toLeft(0.3);
                }

                if (painetutNapit.getOrDefault(KeyCode.RIGHT, false)) {
                    hero.toRight(0.3);
                }
                if (painetutNapit.getOrDefault(KeyCode.DOWN, false)) {
                    hero.accelerateBack();
                }
                if (painetutNapit.getOrDefault(KeyCode.SHIFT, false) && painetutNapit.getOrDefault(KeyCode.RIGHT, false)) {
                    hero.toRight(0.01);
                }
                if (painetutNapit.getOrDefault(KeyCode.SHIFT, false) && painetutNapit.getOrDefault(KeyCode.LEFT, false)) {
                    hero.toLeft(0.01);
                }

                if (painetutNapit.getOrDefault(KeyCode.UP, false)) {
                    hero.accelerate(0.0005);
                }

                //IMMUNITY COOLDOWN && TIMER, little broken ATM
                /*if (painetutNapit.getOrDefault(KeyCode.E, false) && cooldown==false) {
                    cdTimer=0;
                    cooldown=true;
                    hero.immunityOn();
                    timer = nykyhetki;
                }
                if(nykyhetki>(timer+1050000000)) {
                    hero.immunityOff();
                }
                if(cooldown==true && cdTimer>4000) {
                    cdTimer=0;
                    System.out.println("valmis"); // MAYBE AN ICON HERE OR JUST TEXT ON SCREEN
                    
                    cooldown=false;
                } else{
                    cdTimer++;
                }
                 */
                //HERO SHOOT
                if (painetutNapit.getOrDefault(KeyCode.X, false) && ammo.size() < 2) {
                    Bullet shot = new Bullet((int) hero.getPoly().getTranslateX(), (int) hero.getPoly().getTranslateY());

                    shot.getPoly().setRotate(hero.spritePolygon.getRotate());
                    ammo.add(shot);

                    shot.accelerate(0.0005);
                    shot.setMovement(shot.getMovement().normalize().multiply(1.5));

                    peliRuutu.getChildren().add(shot.getPoly());
                }

                if (painetutNapit.getOrDefault(KeyCode.SHIFT, false && ammo.size() < 1)) {
                    hero.slowDownShift();
                }

                //HERO BASIC MOVEMENT
                hero.move();
                hero.slowDown();

                //ROUNDS
                if (enemies.isEmpty() && ikkuna.getScene() == peliNakyma) {
                    cleared = true;
                    round++;
                }

                if (cleared) {
                    cleared = false;
                    roundATM.setText("Round " + round);

                    if (round == 1) {
                        System.out.println("r: " + 1);
                        round1(peliRuutu);
                    }
                    if (round == 2) {
                        System.out.println("r: " + 2);
                        round2(peliRuutu);
                    }
                    if (round == 3) {

                        System.out.println("r: " + 3 + " _final wave_");
                        round3Boss(peliRuutu);
                    }
                    if (round == 4) {
                        painetutNapit.clear();
                        gameEndScore("VICTORY!");
                        gameOverStage(ikkuna);
                        stop();
                    }
                }

                //ENEMY PATROL
                enemies.forEach(enemy -> {
                    enemy.move();
                    if (nykyhetki % 10 == 0) {
                        enemy.patrol();
                    }
                });

                //BOSS THINGIES
                if (nykyhetki % 3000 == 0) {
                    if (enemies.get(0).getClass() == EnemySpecial.class) {
                        round3(peliRuutu, 40, 0, 2, 2);
                        round3(peliRuutu, 40, 180, 598, 2);

                        if (nykyhetki % 5000 == 0) {
                            int number = 25 + random.nextInt(40);
                            int number2 = 25 + random.nextInt(40);
                            round3Vertical(peliRuutu, number, 270, 2, 398);
                            round3Vertical(peliRuutu, number2, 90, 2, 2);
                        }
                    }
                }

                //ENEMY SHOOT
                if (nykyhetki % 1500 == 0) {
                    for (Sprite enemy : enemies) {
                        if (enemy.getLiving() && enemy.getClass() == Enemy1.class) {
                            int spray = 10 - random.nextInt(20);
                            Bullet shot = new Bullet((int) enemy.getPoly().getTranslateX(), (int) enemy.getPoly().getTranslateY());

                            // System.out.println("angle " + Math.toDegrees(Math.atan2(hero.getPoly().getTranslateY() - shot.getPoly().getTranslateY(),  hero.getPoly().getTranslateX() - shot.getPoly().getTranslateX())));
                            shot.getPoly().setRotate(Math.toDegrees(Math.atan2(hero.getPoly().getTranslateY() - shot.getPoly().getTranslateY(), hero.getPoly().getTranslateX() - shot.getPoly().getTranslateX())) + spray);
                            enemyAmmo.add(shot);

                            shot.accelerate(0.0005);
                            shot.setMovement(shot.getMovement().normalize().multiply(0.2));

                            peliRuutu.getChildren().add(shot.getPoly());
                        }
                        if (enemy.getLiving() && enemy.getClass() == Enemy2.class) {  //CIRCLE ATTACK
                            for (int i = 0; i < 10; i++) {
                                Bullet shot = new Bullet((int) enemy.getPoly().getTranslateX(), (int) enemy.getPoly().getTranslateY());
                                shot.getPoly().setRotate(enemy.getPoly().getRotate() + 36 * i);
                                enemyAmmo.add(shot);

                                shot.accelerate(0.0005);
                                shot.setMovement(shot.getMovement().normalize().multiply(0.2));

                                peliRuutu.getChildren().add(shot.getPoly());
                            }
                        }
                    }
                }

                //AMMO BASIC MOVEMENT
                ammo.forEach(ammus -> ammus.move());
                enemyAmmo.forEach(ammus -> ammus.move());

                //BULLET HITS ENEMY, HERO HITS ENEMY
                ammo.forEach(shot -> {
                    enemies.forEach(enemy -> {
                        if (shot.intersect(enemy)) {
                            shot.setLiving(false);
                            animateUsingTimeline(enemy.spritePolygon, 1.0, 1.4);
                            if (enemy.damage(40)) {
                                score += 10;
                                scoreText.setText("Points: " + score);
                                peliRuutu.getChildren().remove(enemy.spritePolygon);
                            }

                        }

                    });

                });
                enemies.forEach(enemy -> {
                    if (hero.intersect(enemy)) {
                        hero.damage(100);
                        animateUsingTimeline(hero.spritePolygon, 1.0, 0.8); //Doesnt work atm
                    }
                });

                //BULLET HITS HERO
                enemyAmmo.forEach(shot -> {
                    if (shot.intersect(hero)) {
                        shot.damage(1000);
                        hero.damage(5);
                    }
                });

                //REMOVE DEAD ENTITITES, UPDATE LISTS
                ammo.stream()
                        .filter(shot -> !shot.getLiving())
                        .forEach(shot -> peliRuutu.getChildren().remove(shot.getPoly()));
                ammo.removeAll(ammo.stream()
                        .filter(shot -> !shot.getLiving())
                        .collect(Collectors.toList()));
                enemyAmmo.stream()
                        .filter(shot -> !shot.getLiving())
                        .forEach(shot -> peliRuutu.getChildren().remove(shot.getPoly()));
                enemyAmmo.removeAll(ammo.stream()
                        .filter(shot -> !shot.getLiving())
                        .collect(Collectors.toList()));

                enemies.stream()
                        .filter(enemy -> !enemy.getLiving())
                        .forEach(enemy -> peliRuutu.getChildren().remove(enemy.getPoly()));
                enemies.removeAll(enemies.stream()
                        .filter(enemy -> !enemy.getLiving())
                        .collect(Collectors.toList()));

                //HERO DEAD
                if (!hero.alive) {
                    painetutNapit.clear();

                    //animateUsingTimeline(hero.getPoly(), 1.0, 1.4);
                    gameEndScore("FAILURE");
                    gameOverStage(ikkuna);
                    stop();

                    //peliRuutu.getChildren().remove(hero.spritePolygon);
                    // TOO FAST ATM, FIX
                }
            }
        }.start();

        //---
        ikkuna.setScene(peliNakyma);
        ikkuna.show();

    }

    // RESET ROUND, = CLEAR OLD ENTITIES, ADD NEW HERO
    void resetRound(Pane peliRuutu) {
        round = 0;
        score = 0;
        roundTimerStart = System.nanoTime();

        peliRuutu.getChildren().remove(hero.getPoly());
        for (Sprite e : enemies) {
            e.alive = false;
            peliRuutu.getChildren().remove(e.getPoly());
        }
        enemies.clear();
        for (Sprite e : enemyAmmo) {
            e.alive = false;
            peliRuutu.getChildren().remove(e.getPoly());
        }
        enemyAmmo.clear();

    }

    void restart(Stage ikkuna) { //kinda useless ?
        startGame(ikkuna);
    }

    void scoreSaveStage(Stage ikkuna) {

        BorderPane nameSetLayout = new BorderPane();
        nameSetLayout.setPrefSize(150, 200);
        VBox menu = new VBox();
        menu.setPadding(new Insets(20, 20, 20, 20));
        menu.setSpacing(10);
        Label l = new Label("NAME:");
        TextField tf = new TextField();
        saveButton = new Button("SAVE BEST SCORE");
        menu.getChildren().addAll(l, tf, saveButton);
        menu.setAlignment(Pos.CENTER);
        nameSetLayout.setCenter(menu);

        saveButton.setOnAction(click -> {
            if (tf.getLength() < 2) {
                nameSetLayout.getChildren().add(new Label("Minimum 2 characters!"));
            } else {
                try {
                    hiScores.leaderboardUpdate(tf.getText(), bestTimes.poll());

                    leaderboardsStage(ikkuna);
                } catch (IOException ex) {
                    System.out.println("Problems with updating leaderboards..");
                } catch (GeneralSecurityException ex) {
                    System.out.println("Problems with updating leaderboards..");
                } catch (ParseException ex) {
                    Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        Scene nameScene = new Scene(nameSetLayout);
        ikkuna.setScene(nameScene);

    }

    void leaderboardsStage(Stage ikkuna) throws IOException, GeneralSecurityException, ParseException {

        BorderPane lbLayout = new BorderPane();
        lbLayout.setPrefSize(LEVEYS, KORKEUS);
        VBox menu = new VBox();
        VBox menuScores = new VBox();
        menu.setPadding(new Insets(20, 20, 20, 20));
        menu.setSpacing(10);

        Label l = new Label("LEADERBOARD");
        menuScores.getChildren().add(l);

        PriorityQueue<Score> tmpr = hiScores.search();
        int ranking = 1;
        while (!tmpr.isEmpty()) {
            menuScores.getChildren().add(new Text(ranking + ". " + tmpr.poll().toString()));
            ranking++;
        }

        Button tryAgainButton = new Button("PLAY AGAIN");
        Button exitButton = new Button("EXIT");
        menu.getChildren().addAll(tryAgainButton, exitButton);
        menu.setAlignment(Pos.CENTER_LEFT);
        menuScores.setAlignment(Pos.CENTER_RIGHT);
        lbLayout.setCenter(menu);
        lbLayout.setRight(menuScores);

        tryAgainButton.setOnAction(click -> {
            resetRound(peliRuutu);
            roundATM.setText("Tutorial  ");  //move these?
            scoreText.setText("Points: " + score);
            restart(ikkuna);
        });

        exitButton.setOnAction(click -> {
            ikkuna.close();
        });

        Scene leaderboardScene = new Scene(lbLayout);
        ikkuna.setScene(leaderboardScene);

    }

    void gameEndScore(String WorL) {
        roundTimerEnd = System.nanoTime();
        double time = (Math.round((roundTimerEnd - roundTimerStart) / 1e9 * 100.0)) / 100.0;
        //Score oldBest = bestTimes.peek();
        Score newScore = new Score(time, round);
        bestTimes.add(newScore);

        if (bestTimes.peek().equals(newScore)) {
            gameOverLabel.setText(WorL
                    + "\nNew Session Best!: " + bestTimes.peek().getTime() + " s, round " + bestTimes.peek().getRound());

        } else {
            gameOverLabel.setText(WorL + " \nTime: " + time + ", round: " + round
                    + "\nSession Best: " + bestTimes.peek().getTime() + " s, round " + bestTimes.peek().getRound());
        }
    }

    Pane alkuRuutu() {
        BorderPane ruutu = new BorderPane();
        VBox ruudunSisalla = new VBox();
        ruudunSisalla.setPadding(new Insets(10, 50, 50, 50));
        ruudunSisalla.setSpacing(10);

        ruutu.setPrefSize(LEVEYS, KORKEUS);

        startGameButton = new Button("START");
        instructionsButton = new Button("HOW TO PLAY");
        Button Configuration = new Button("CONFIG");                  //final week stuff maby
        ruudunSisalla.getChildren().add(startGameButton);
        ruudunSisalla.getChildren().add(instructionsButton);
        ruudunSisalla.setAlignment(Pos.CENTER);
        ruutu.setCenter(ruudunSisalla);

        return ruutu;

    }

    Pane instructionsScene() {
        BorderPane instructionsLayout = new BorderPane();
        instructionsLayout.setPrefSize(LEVEYS, KORKEUS);
        VBox instructionsMenu = new VBox();
        instructionsMenu.setPadding(new Insets(20, 20, 20, 20));
        instructionsMenu.setSpacing(10);

        Label instructionsLabel = new Label("INSTRUCTIONS: \nArrow keys to move\nSHIFT+Arrow keys to slow down\nX to shoot\nE for immunity(disabled atm)\nGoal: spaceship go brrt");  //TÄHÄN KIVEMPI TEXT
        startGameFromTut = new Button("START GAME");
        instructionsMenu.getChildren().addAll(instructionsLabel, startGameFromTut);
        instructionsMenu.setAlignment(Pos.CENTER);
        instructionsLayout.setCenter(instructionsMenu);
        return instructionsLayout;
    }

    void gameOverStage(Stage ikkuna) {
        BorderPane gameOverLayout = new BorderPane();
        gameOverLayout.setPrefSize(LEVEYS, KORKEUS);
        VBox menu = new VBox();
        menu.setPadding(new Insets(20, 20, 20, 20));
        menu.setSpacing(10);

        //gameOverLabel = new Label(result);  //TÄHÄN KIVEMPI TEXT
        Button tryAgainButton = new Button("TRY AGAIN");
        Button saveScoreFromGameOverButton = new Button("SAVE SCORE");
        Button hiScoresButton = new Button("CHECK LEADERBOARDS");
        Button exitButton = new Button("EXIT");
        menu.getChildren().addAll(gameOverLabel, tryAgainButton, saveScoreFromGameOverButton, hiScoresButton, exitButton);
        menu.setAlignment(Pos.CENTER);
        gameOverLayout.setCenter(menu);

        tryAgainButton.setOnAction(click -> {
            resetRound(peliRuutu);
            roundATM.setText("Tutorial  ");  //move these?
            scoreText.setText("Points: " + score);
            restart(ikkuna);
        });

        exitButton.setOnAction(click -> {
            ikkuna.close();
        });
        saveScoreFromGameOverButton.setOnAction(click -> {
            scoreSaveStage(ikkuna);
        });
        hiScoresButton.setOnAction(click -> {
            try {
                leaderboardsStage(ikkuna);
            } catch (IOException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);   //clean up?
            } catch (GeneralSecurityException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        gameOverScene = new Scene(gameOverLayout);
        ikkuna.setScene(gameOverScene);
        ikkuna.show();

    }

    //HIT ANIMATION
    private boolean animateUsingTimeline(Polygon thing, double value1, double value2) {
        DoubleProperty scale = new SimpleDoubleProperty(1);
        thing.scaleXProperty().bind(scale);
        thing.scaleYProperty().bind(scale);

        Timeline beat = new Timeline(
                new KeyFrame(Duration.ZERO, event -> scale.setValue(value1)),
                new KeyFrame(Duration.seconds(0.1), event -> scale.setValue(value2))
        );
        beat.setAutoReverse(true);
        beat.setCycleCount(2);
        beat.play();
        return true;
    }

    // ROUNDS OF ENEMIES
    void tut(Pane pane) {

        Enemy tutorialEnemy = new Enemy(300, 150);
        pane.getChildren().add(tutorialEnemy.getPoly());
        enemies.add(tutorialEnemy);
        cleared = false;
    }

    void round1(Pane pane) {

        for (int i = 0; i < 2; i++) {
            enemies.add(new Enemy1(300 / (i + 1), 150));
            pane.getChildren().add(enemies.get(i).spritePolygon);
        }
        cleared = false;
    }

    void round2(Pane pane) {
        enemies.add(new Enemy2(300, 150));
        pane.getChildren().add(enemies.get(0).spritePolygon);
        cleared = false;
    }

    void round3Boss(Pane pane) {
        enemies.add(new EnemySpecial(300, 2));
        enemies.get(0).getPoly().setRotate(-45);
        pane.getChildren().add(enemies.get(0).spritePolygon);
    }

    void round3(Pane pane, int value, int rotate, int startX, int startY) {
        for (int i = startY; i < 400; i++) {
            Bullet shot = new Bullet((int) startX, (int) i);
            shot.getPoly().setRotate(rotate);
            enemyAmmo.add(shot);

            shot.accelerate(0.0005);
            shot.setMovement(shot.getMovement().normalize().multiply(0.1));

            pane.getChildren().add(shot.getPoly());
            i += value;
        }
        cleared = false;

    }

    void round3Vertical(Pane pane, int value, int rotate, int startX, int startY) {
        for (int i = startX; i < 400; i++) {
            Bullet shot = new Bullet((int) i, (int) startY);
            shot.getPoly().setRotate(rotate);
            enemyAmmo.add(shot);

            shot.accelerate(0.0005);
            shot.setMovement(shot.getMovement().normalize().multiply(0.1));

            pane.getChildren().add(shot.getPoly());
            i += value;
        }
        cleared = false;

    }
    /*
    private boolean animateUsingTimeline2(Polygon thing, double value1, double value2) { //not used atm
        DoubleProperty scale = new SimpleDoubleProperty(1);
        thing.scaleXProperty().bind(scale);
        thing.scaleYProperty().bind(scale);
        //for (int i = 1; i < 100; i++) {
        final int add = 2;
        Timeline beat = new Timeline(
                new KeyFrame(Duration.ZERO, event -> scale.setValue(value1)),
                new KeyFrame(Duration.seconds(add / 10), event -> scale.setValue(value2 + add))
        );
        beat.setAutoReverse(true);
        beat.setCycleCount(4);
        beat.play();
        //}

        return true;
    }*/
}
