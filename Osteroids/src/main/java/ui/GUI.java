/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import applogic.loadDifficulty;
import applogic.Score;
import applogic.SheetsLeaderBoards;
import gamelogic.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
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
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author artkoski
 */
public class GUI extends Application {

    private SheetsLeaderBoards hiScores;
    public static int LEVEYS = 600;
    public static int KORKEUS = 400;
    private Scene peliNakyma;
    private Scene alkuNakyma;
    private Scene gameOverScene;
    private Scene instructionsScene;
    private Pane peliRuutu;
    private List<Bullet> ammo = new ArrayList<>();
    private List<Bullet> enemyAmmo = new ArrayList<>();
    private List<Sprite> enemies = new ArrayList<>();
    private Button startGameFromTut;
    private int round = 0;
    private Hero hero;
    private int score = 0;
    private long timer;
    private boolean cooldown = false;
    private long cdTimer = 0;
    private boolean cleared;
    private long roundTimerStart;
    private long roundTimerEnd;
    private Label gameOverLabel = new Label();
    private Text roundATM = new Text();
    private Text scoreText = new Text();
    private double multiplier = 1;
    private int difficulty;
    private final PriorityQueue<Score> bestTimes = new PriorityQueue<Score>();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage currentStage) {

        //Google Sheets leaderboard connection
        hiScores = new SheetsLeaderBoards();
        hiScores.init();

        //to main menu
        mainScreenStage(currentStage);

    }

    public void startGame(Stage ikkuna, int setting) {

        peliRuutu = new Pane();
        peliRuutu.setPrefSize(LEVEYS, KORKEUS);
        peliNakyma = new Scene(peliRuutu);
        //Difficulty, atm 1 or 2 (normal, hard)
        difficulty = setting;
        loadDifficulty loadSettings = new loadDifficulty(setting);
        ArrayList<Double> settings = loadSettings.difficultyConfig();
        double patrolSpeed = settings.get(0);
        double bossAmmoFrequency = settings.get(1);
        double enemyAmmoFrequency = settings.get(2);
        double extraAcceleration = settings.get(3);
        multiplier = settings.get(4);

        Random random = new Random();

        //ADD HERO
        hero = new Hero(150, 100, 800);
        peliRuutu.getChildren().add(hero.getPoly());

        //Start Game (tutorial round) 
        roundTimerStart = System.nanoTime();
        roundZero(peliRuutu);

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

                    shot.getPoly().setRotate(hero.getPoly().getRotate());
                    ammo.add(shot);

                    shot.accelerate(0.0005);
                    shot.setMovement(shot.getMovement().normalize().multiply(1.5));

                    peliRuutu.getChildren().add(shot.getPoly());
                }

                if (painetutNapit.getOrDefault(KeyCode.SHIFT, false)) {
                    hero.slowDownShift();
                }

                //HERO BASIC MOVEMENT
                hero.move();
                hero.slowDown();

                //ENEMY PATROL
                enemies.forEach(enemy -> {
                    enemy.move();
                    if (nykyhetki % patrolSpeed == 0) {
                        enemy.patrol();
                    }
                });

                //BOSS THINGIES
                if (enemies.get(0).getClass() == EnemySpecial.class) {

                    if (nykyhetki % bossAmmoFrequency == 0) {

                        int number = 50 + random.nextInt(40);
                        int number2 = 50 + random.nextInt(40);
                        round3(peliRuutu, number, 0, 2, 2);
                        round3(peliRuutu, number2, 180, 598, 2);

                        if (nykyhetki % (bossAmmoFrequency + 1000) == 0) {

                            round3Vertical(peliRuutu, number, 270, 2, 398);
                            round3Vertical(peliRuutu, number2, 90, 2, 2);
                        }
                    }
                }

                //ENEMY SHOOT
                if (nykyhetki % enemyAmmoFrequency == 0) {
                    enemies.stream().map((enemy) -> {
                        if (enemy.getLiving() && enemy.getClass() == Enemy1.class) {

                            int spray = 10 - random.nextInt(20);
                            Bullet shot = new Bullet((int) enemy.getPoly().getTranslateX(), (int) enemy.getPoly().getTranslateY());

                            //shoot towards player
                            shot.getPoly().setRotate(Math.toDegrees(Math.atan2(hero.getPoly().getTranslateY() - shot.getPoly().getTranslateY(), hero.getPoly().getTranslateX() - shot.getPoly().getTranslateX())) + spray);
                            enemyAmmo.add(shot);

                            shot.accelerate(0.0005 + extraAcceleration);
                            shot.setMovement(shot.getMovement().normalize().multiply(0.2));

                            peliRuutu.getChildren().add(shot.getPoly());
                        }
                        return enemy;
                    }).filter((enemy) -> (enemy.getLiving() && enemy.getClass() == Enemy2.class)).forEachOrdered((enemy) -> {
                        //CIRCLE ATTACK
                        Bullet shot;
                        for (int i = 0; i < 10; i++) {
                            shot = new Bullet((int) enemy.getPoly().getTranslateX(), (int) enemy.getPoly().getTranslateY());
                            shot.getPoly().setRotate(enemy.getPoly().getRotate() + 36 * i);
                            enemyAmmo.add(shot);

                            shot.accelerate(0.0005 + extraAcceleration);
                            shot.setMovement(shot.getMovement().normalize().multiply(0.2));

                            peliRuutu.getChildren().add(shot.getPoly());
                        }
                    });
                }

                //AMMO BASIC MOVEMENT
                ammo.forEach(ammus -> ammus.move());
                enemyAmmo.forEach(ammus -> ammus.move());

                //BULLET HITS ENEMY, HERO HITS ENEMY
                ammo.forEach(shot -> {
                    enemies.forEach(enemy -> {
                        if (shot.intersect(enemy)) {
                            shot.setLiving(false);
                            animateUsingTimeline(enemy.getPoly(), 1.0, 1.4);
                            if (enemy.damage(40)) { // og 40
                                score += 10;
                                scoreText.setText("Points: " + score);
                                peliRuutu.getChildren().remove(enemy.getPoly());
                            }
                            if (enemy.getClass() == EnemySpecial.class) {
                                //jotain kivaa?
                            }

                        }

                    });

                });
                enemies.forEach(enemy -> {
                    if (hero.intersect(enemy)) {
                        hero.damage(10);
                    }
                });

                //BULLET HITS HERO
                enemyAmmo.forEach(shot -> {
                    if (shot.intersect(hero)) {
                        shot.damage(1000);
                        hero.damage(5.0 * multiplier * multiplier);
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
                if (!hero.getLiving()) {

                    painetutNapit.clear();
                    gameEndScore("FAILURE");
                    gameOverStage(ikkuna);
                    stop();
                }
                //ROUNDS
                if (enemies.isEmpty() && ikkuna.getScene() == peliNakyma) {
                    cleared = true;
                    round++;
                    updateRound();
                }

                if (cleared) {
                    cleared = false;

                    if (round == 1) {
                        painetutNapit.clear();
                        round1(peliRuutu);
                    }
                    if (round == 2) {
                        painetutNapit.clear();
                        round2(peliRuutu);
                    }
                    if (round == 3) {
                        painetutNapit.clear();
                        round3Boss(peliRuutu);
                    }
                    if (round == 4) {
                        painetutNapit.clear();
                        gameEndScore("VICTORY!");
                        gameOverStage(ikkuna);
                        stop();

                    }
                }
            }
        }.start();

        //---
        ikkuna.setScene(peliNakyma);
        ikkuna.show();

    }

    // RESET ROUND, = CLEAR OLD ENTITIES
    void resetRound(Pane peliRuutu) {
        round = 0;
        score = 0;

        if (hero != null) {
            peliRuutu.getChildren().remove(hero.getPoly());
        }
        enemies.stream().map((e) -> {
            e.setLiving(false);
            return e;
        }).forEachOrdered((e) -> {
            peliRuutu.getChildren().remove(e.getPoly());
        });
        enemies.clear();

        enemyAmmo.stream().map((e) -> {
            e.setLiving(false);
            return e;
        }).forEachOrdered((e) -> {
            peliRuutu.getChildren().remove(e.getPoly());
        });

        enemyAmmo.clear();

    }

    void mainScreenStage(Stage ikkuna) {

        BorderPane ruutu = new BorderPane();
        VBox ruudunSisalla = new VBox();
        HBox ruudunSisalla2 = new HBox();
        ruudunSisalla.setPadding(new Insets(10, 50, 50, 50));
        ruudunSisalla.setSpacing(10);

        ruutu.setPrefSize(LEVEYS, KORKEUS);

        RadioButton hardToggle = new RadioButton("Hard");
        RadioButton normalToggle = new RadioButton("Normal");
        Text selectDifficulty = new Text("");
        ruudunSisalla2.getChildren().addAll(hardToggle, normalToggle);
        ruudunSisalla2.setAlignment(Pos.BOTTOM_CENTER);
        Text DifficultyInfo = new Text("");
        DifficultyInfo.setTextAlignment(TextAlignment.CENTER);

        hardToggle.setOnAction(click -> {
            DifficultyInfo.setText("Hard mode selected.\n"
                    + "Enemies shoot faster and do slightly more damage.");
            selectDifficulty.setText("");
            if (normalToggle.isSelected()) {
                normalToggle.setSelected(false);
            }
        });
        normalToggle.setOnAction(click -> {
            DifficultyInfo.setText("Normal mode selected.");
            selectDifficulty.setText("");
            if (hardToggle.isSelected()) {
                hardToggle.setSelected(false);
            }
        });

        Button startGameButton = new Button("START");
        Button instructionsButton = new Button("HOW TO PLAY");
        Button exitButton = new Button("EXIT");
        ruudunSisalla.getChildren().addAll(startGameButton, instructionsButton, exitButton, DifficultyInfo, selectDifficulty);
        ruudunSisalla.setAlignment(Pos.CENTER);

        ruutu.setCenter(ruudunSisalla);
        ruutu.setBottom(ruudunSisalla2);

        exitButton.setOnAction(click -> {
            ikkuna.close();
        });

        startGameButton.setOnAction(click -> {
            resetRound(peliRuutu);
            if (hardToggle.isSelected()) {
                startGame(ikkuna, 2);
            } else if (normalToggle.isSelected()) {
                startGame(ikkuna, 1);
            } else {
                selectDifficulty.setText("Select difficulty first!");
            }
        });

        instructionsButton.setOnAction(click -> {
            ikkuna.setScene(instructionsScene);
        });

        instructionsScene = new Scene(instructionsScene());

        startGameFromTut.setOnAction(click -> {
            ikkuna.setScene(alkuNakyma);
        });

        alkuNakyma = new Scene(ruutu);
        ikkuna.setScene(alkuNakyma);
        ikkuna.show();

    }

    Pane instructionsScene() {
        BorderPane instructionsLayout = new BorderPane();
        instructionsLayout.setPrefSize(LEVEYS, KORKEUS);
        VBox instructionsMenu = new VBox();
        instructionsMenu.setPadding(new Insets(20, 20, 20, 20));
        instructionsMenu.setSpacing(10);

        Label instructionsLabel = new Label("CONTROLS: \nArrow keys to move\nSHIFT+Arrow keys to slow down\nX to shoot\nGoal: Destroy the enemies as fast as you can.");  //TÄHÄN KIVEMPI TEXT
        instructionsLabel.setTextAlignment(TextAlignment.CENTER);
        startGameFromTut = new Button("Main Menu");
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

        Button tryAgainButton = new Button("TRY AGAIN");
        Button saveScoreFromGameOverButton = new Button("SAVE BEST SCORE");
        saveScoreFromGameOverButton.setMaxWidth(150);
        Button hiScoresButton = new Button("CHECK LEADERBOARDS");
        Button exitButton = new Button("EXIT");
        gameOverLabel.setTextAlignment(TextAlignment.CENTER);
        menu.getChildren().addAll(gameOverLabel, tryAgainButton, saveScoreFromGameOverButton, hiScoresButton, exitButton);
        menu.setAlignment(Pos.CENTER);
        gameOverLayout.setCenter(menu);

        tryAgainButton.setOnAction(click -> {
            resetRound(peliRuutu);
            ikkuna.setScene(alkuNakyma);
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

    void scoreSaveStage(Stage ikkuna) {

        BorderPane nameSetLayout = new BorderPane();
        nameSetLayout.setPrefSize(250, 200);
        VBox menu = new VBox();
        menu.setPadding(new Insets(20, 20, 20, 20));
        menu.setSpacing(10);
        Label l = new Label("NAME:");
        Label wrongInput = new Label("");
        TextField tf = new TextField();
        Button saveButton = new Button("SAVE BEST SCORE");
        Text infoText = new Text("Score to be saved: " + bestTimes.peek().toStringNoName());
        menu.getChildren().addAll(l, tf, saveButton, wrongInput, infoText);
        menu.setAlignment(Pos.CENTER);
        nameSetLayout.setCenter(menu);

        saveButton.setOnAction(click -> {
            if (tf.getLength() < 2 || tf.getLength() > 10) {
                wrongInput.setText("NAME BETWEEN 2 AND 8 CHARACTERS");
            } else {
                try {
                    hiScores.leaderboardUpdate(tf.getText(), bestTimes.poll());

                    leaderboardsStage(ikkuna);
                } catch (IOException ex) {
                    System.out.println("Problems with updating leaderboards..");
                } catch (GeneralSecurityException ex) {
                    System.out.println("Problems with updating leaderboards..");
                } catch (ParseException ex) {
                    System.out.println("Problems with updating leaderboards..");
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
        menu.setPadding(new Insets(20, 20, 20, 20));
        menu.setSpacing(10);

        Button tryAgainButton = new Button("MENU");
        Button exitButton = new Button("EXIT");
        Button saveScoreFromGameOverButton = new Button("SAVE BEST SCORE");
        saveScoreFromGameOverButton.setMinWidth(150);
        Text alreadySavedText = new Text("");

        menu.getChildren().addAll(tryAgainButton, saveScoreFromGameOverButton, exitButton);
        menu.setAlignment(Pos.CENTER_LEFT);
        lbLayout.setCenter(menu);
        lbLayout.setRight(hiScores.craftLeaderboard());
        lbLayout.setBottom(alreadySavedText);

        tryAgainButton.setOnAction(click -> {
            ikkuna.setScene(alkuNakyma);
        });

        saveScoreFromGameOverButton.setOnAction(click -> {
            if (bestTimes.isEmpty()) {
                alreadySavedText.setText("There are no scores to save.");
            } else {
                scoreSaveStage(ikkuna);
            }
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
        Score newScore = new Score(time, round, score, difficulty);
        bestTimes.add(newScore);

        if (bestTimes.peek().equals(newScore)) {
            gameOverLabel.setText(WorL
                    + "\nNew Session Best! \n" + bestTimes.peek().toStringNoName());

        } else {
            gameOverLabel.setText(WorL + " \nTime: " + time + ", round: " + round
                    + "\nSession Best: " + bestTimes.peek().toStringNoName());
        }
    }

    //HIT ANIMATION
    private boolean animateUsingTimeline(Polygon thing, double value1, double value2) {
        DoubleProperty scale = new SimpleDoubleProperty(1);
        thing.scaleXProperty().bind(scale);
        thing.scaleYProperty().bind(scale);

        Paint colourATM = thing.getFill();


        Timeline beat = new Timeline(
                new KeyFrame(Duration.ZERO, event -> scale.setValue(value1)),
                new KeyFrame(Duration.seconds(0.1), event -> scale.setValue(value2))
        );
        if (scale.getValue() == value2) {
            thing.setFill(Color.LIGHTPINK);
        }
        beat.setAutoReverse(true);
        beat.setCycleCount(2);
        beat.play();
        thing.setFill(colourATM);

        return true;
    }

    // ROUNDS OF ENEMIES
    void roundZero(Pane pane) {

        Enemy tutorialEnemy = new Enemy(300, 150);
        pane.getChildren().add(tutorialEnemy.getPoly());
        enemies.add(tutorialEnemy);

        roundATM.setText("Tutorial");
        scoreText.setText("Points: " + score);
        BorderPane roundsPane = new BorderPane();
        roundsPane.setPadding(new Insets(20, 20, 20, 20));
        roundsPane.setLeft(roundATM); //FIX THIS
        roundsPane.setRight(scoreText);
        peliRuutu.getChildren().add(roundsPane);

        cleared = false;
    }

    void updateRound() {
        roundATM.setText("Round: " + String.valueOf(round));
    }

    void round1(Pane pane) {
        for (int i = 0; i < 2; i++) {
            enemies.add(new Enemy1(300 / (i + 1), 150 / (i + 1), 400 * multiplier));
            pane.getChildren().add(enemies.get(i).getPoly());
        }
        cleared = false;
    }

    void round2(Pane pane) {

        enemies.add(new Enemy2(300, 150, 2000 * multiplier));
        pane.getChildren().add(enemies.get(0).getPoly());
        cleared = false;
    }

    void round3Boss(Pane pane) {
        enemies.add(new EnemySpecial(300, 2, 5000 * multiplier));
        enemies.get(0).getPoly().setRotate(-45);
        pane.getChildren().add(enemies.get(0).getPoly());
        cleared = false;
    }

    void round3(Pane pane, int value, int rotate, int startX, int startY) {
        Bullet shot;
        for (int i = startY; i < 400; i++) {
            shot = new Bullet((int) startX, (int) i);
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
        Bullet shot;
        for (int i = startX; i < 400; i++) {
            shot = new Bullet((int) i, (int) startY);
            shot.getPoly().setRotate(rotate);
            enemyAmmo.add(shot);

            shot.accelerate(0.0005);
            shot.setMovement(shot.getMovement().normalize().multiply(0.1));

            pane.getChildren().add(shot.getPoly());
            i += value;
        }
        cleared = false;

    }
}
