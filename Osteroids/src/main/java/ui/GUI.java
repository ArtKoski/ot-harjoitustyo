/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import gamelogic.*;
import java.util.*;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author artkoski
 */
public class GUI extends Application {

    public static int LEVEYS = 600;
    public static int KORKEUS = 400;
    private Scene peliNakyma;
    private Scene alkuNakyma;
    private Scene gameOverScene;
    private Scene gameOverSceneVictory;
    private Scene instructionsScene;
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ikkuna) {
        random = new Random();

        //ALKURUUTU
        BorderPane ruutu = new BorderPane();
        VBox ruudunSisalla = new VBox();
        ruudunSisalla.setPadding(new Insets(10, 50, 50, 50));
        ruudunSisalla.setSpacing(10);

        ruutu.setPrefSize(LEVEYS, KORKEUS);

        Button nappi = new Button("START");
        Button instructionsButton = new Button("HOW TO PLAY");
        ruudunSisalla.getChildren().add(nappi);
        ruudunSisalla.getChildren().add(instructionsButton);
        ruudunSisalla.setAlignment(Pos.CENTER);
        ruutu.setCenter(ruudunSisalla);

        nappi.setOnAction(click -> {
            ikkuna.setScene(peliNakyma);
        });

        instructionsButton.setOnAction(click -> {
            ikkuna.setScene(instructionsScene);
        });

        alkuNakyma = new Scene(ruutu);

        //INSTRUCTIONS SCENE
        BorderPane instructionsLayout = new BorderPane();
        instructionsLayout.setPrefSize(LEVEYS, KORKEUS);
        VBox instructionsMenu = new VBox();
        instructionsMenu.setPadding(new Insets(20, 20, 20, 20));
        instructionsMenu.setSpacing(10);

        Label instructionsLabel = new Label("INSTRUCTIONS: \nArrow keys to move\nSHIFT+Arrow keys to slow down\nX to shoot\nE for immunity(disabled atm)\nGoal: spaceship go brrt");  //TÄHÄN KIVEMPI TEXT
        Button startButton = new Button("START GAME");
        instructionsMenu.getChildren().addAll(instructionsLabel, startButton);
        instructionsMenu.setAlignment(Pos.CENTER);
        instructionsLayout.setCenter(instructionsMenu);
        instructionsScene = new Scene(instructionsLayout);

        startButton.setOnAction(click -> {
            ikkuna.setScene(peliNakyma);
        });

        //gameOverScene
        BorderPane gameOverLayout = new BorderPane();
        gameOverLayout.setPrefSize(LEVEYS, KORKEUS);
        VBox menu = new VBox();
        menu.setPadding(new Insets(20, 20, 20, 20));
        menu.setSpacing(10);

        Label gameOverLabel = new Label("GAME OVER");  //TÄHÄN KIVEMPI TEXT
        Button tryAgainButton = new Button("TRY AGAIN");
        Button hiScores = new Button("");
        Button exitButton = new Button("EXIT");
        menu.getChildren().addAll(gameOverLabel, tryAgainButton, hiScores);
        menu.setAlignment(Pos.CENTER);
        gameOverLayout.setCenter(menu);
        gameOverScene = new Scene(gameOverLayout);

        //gameOverSceneVictory
        BorderPane gameOverVictory = new BorderPane();
        gameOverVictory.setPrefSize(LEVEYS, KORKEUS);
        VBox menuV = new VBox();
        menuV.setPadding(new Insets(20, 20, 20, 20));
        menuV.setSpacing(10);

        Label gameOverLabelVictory = new Label("VICTORY");  //TÄHÄN KIVEMPI TEXT
        Button tryAgainButtonV = new Button("HISCORES");
        Button hiScoresV = new Button("GO AGAIN");
        Button exitButtonV = new Button("EXIT");
        menuV.getChildren().addAll(gameOverLabelVictory, tryAgainButtonV, hiScoresV);
        menuV.setAlignment(Pos.CENTER);
        gameOverVictory.setCenter(menuV);
        gameOverSceneVictory = new Scene(gameOverVictory);

        //Pelinäkymä
        Pane peliRuutu = new Pane();
        peliRuutu.setPrefSize(LEVEYS, KORKEUS);

        //ADD HERO
        hero = new Hero(150, 100);
        peliRuutu.getChildren().add(hero.getPoly());

        //ADD TUT ENEMY
        tut(peliRuutu);

        //ROUND/SCORE TEXT
        Text roundATM = new Text();
        Text scoreText = new Text();
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
                    hero.toLeft();
                }

                if (painetutNapit.getOrDefault(KeyCode.RIGHT, false)) {
                    hero.toRight();
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
                    hero.accelerate();
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
                if (painetutNapit.getOrDefault(KeyCode.X, false) && ammo.size() < 3) {
                    Bullet shot = new Bullet((int) hero.getPoly().getTranslateX(), (int) hero.getPoly().getTranslateY());

                    shot.getPoly().setRotate(hero.spritePolygon.getRotate());
                    ammo.add(shot);

                    shot.accelerate();
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
                    round++;

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
                        gameOverLabelVictory.setText("VICTORY! \nScore: " + score + "\nRound: " + round);
                        painetutNapit.clear();
                        ikkuna.setScene(gameOverSceneVictory);
                    }

                }

                //ENEMY PATROL
                enemies.forEach(enemy -> {
                    enemy.move();
                    if (nykyhetki % 10 == 0) {
                        enemy.patrol();
                    }
                });

                //ENEMY SHOOT
                if (nykyhetki % 1500 == 0) {
                    for (Sprite enemy : enemies) {
                        if (enemy.getLiving()) {
                            int spray = 10 - random.nextInt(20);
                            Bullet shot = new Bullet((int) enemy.getPoly().getTranslateX(), (int) enemy.getPoly().getTranslateY());

                            // System.out.println("angle " + Math.toDegrees(Math.atan2(hero.getPoly().getTranslateY() - shot.getPoly().getTranslateY(),  hero.getPoly().getTranslateX() - shot.getPoly().getTranslateX())));
                            shot.getPoly().setRotate(Math.toDegrees(Math.atan2(hero.getPoly().getTranslateY() - shot.getPoly().getTranslateY(), hero.getPoly().getTranslateX() - shot.getPoly().getTranslateX())) + spray);
                            enemyAmmo.add(shot);

                            shot.accelerate();
                            shot.setMovement(shot.getMovement().normalize().multiply(0.2));

                            peliRuutu.getChildren().add(shot.getPoly());
                        }
                        if (enemy.getLiving() && enemy.getClass() == Enemy2.class) {  //CIRCLE ATTACK
                            for (int i = 0; i < 10; i++) {
                                Bullet shot = new Bullet((int) enemy.getPoly().getTranslateX(), (int) enemy.getPoly().getTranslateY());
                                shot.getPoly().setRotate(enemy.getPoly().getRotate() + 36 * i);
                                enemyAmmo.add(shot);

                                shot.accelerate();
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
                    animateUsingTimeline(hero.getPoly(), 1.0, 1.4);
                    gameOverLabel.setText("GAME OVER \nScore: " + score + "\nRound: " + round);
                    ikkuna.setScene(gameOverScene);
                    peliRuutu.getChildren().remove(hero.spritePolygon);

                    // TOO FAST ATM, FIX
                }
            }
        }.start();

        //gameOverButton
        tryAgainButton.setOnAction(click -> {  // MAKE METHOD FOR 'RESET', BULLETS NEED TO BE CLEARED AS WELL
            resetRound(peliRuutu);
            roundATM.setText("Tutorial  ");
            scoreText.setText("Points: " + score);
            ikkuna.setScene(peliNakyma);
        });

        tryAgainButtonV.setOnAction(click -> {
            resetRound(peliRuutu);
            roundATM.setText("Tutorial");
            scoreText.setText("Points: " + score);
            ikkuna.setScene(peliNakyma);
        });

        //---
        ikkuna.setScene(alkuNakyma);
        ikkuna.show();

    }

    //RESET ROUND, = CLEAR OLD ENTITIES, ADD NEW HERO
    void resetRound(Pane peliRuutu) {    //NEED TO FIX: AFTER RESET HERO FLIPS
        round = 0;
        score = 0;
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
        hero = new Hero(150, 100);
        peliRuutu.getChildren().add(hero.getPoly());

        tut(peliRuutu);

    }
// ROUNDS OF ENEMIES

    void tut(Pane pane) {

        Enemy tutorialEnemy = new Enemy(300, 150);
        pane.getChildren().add(tutorialEnemy.getPoly());
        enemies.add(tutorialEnemy);

    }

    void round1(Pane pane) {
        for (int i = 0; i < 2; i++) {
            enemies.add(new Enemy1(300 / (i + 1), 150));
            pane.getChildren().add(enemies.get(i).spritePolygon);
        }
    }

    void round2(Pane pane) {
        enemies.add(new Enemy2(300, 150));
        pane.getChildren().add(enemies.get(0).spritePolygon);

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

    private boolean animateUsingTimeline2(Polygon thing, double value1, double value2) {
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
    }
    /*
    private void moveCircleOnKeyPress(Scene scene, Hahmo hahmo) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        hahmo.liike = hahmo.liike.add(hahmo.getLiike().getX(), hahmo.liike.getY() - 0.5);
                        break;
                    case RIGHT:
                        hahmo.liike = hahmo.liike.add(hahmo.liike.getX() + 0.5, hahmo.getLiike().getY());
                        break;
                    case DOWN:
                        hahmo.liike = hahmo.liike.add(hahmo.getLiike().getX(), hahmo.liike.getY() + 0.5);
                        break;
                    case LEFT:
                        hahmo.liike = hahmo.liike.add(hahmo.liike.getX() - 0.5, hahmo.getLiike().getY());
                        break;
                }
            }
        });
    }*/
}
