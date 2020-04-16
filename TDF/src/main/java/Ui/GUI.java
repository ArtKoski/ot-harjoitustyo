/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ui;

import java.awt.Canvas;
import java.awt.TextField;
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
import javafx.geometry.Point2D;
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
import gameLogic.Bullet;
import gameLogic.Enemy;
import gameLogic.Hero;
import gameLogic.Hero;
import gameLogic.Sprite;
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
    int circleForFive = 72;
    List<Bullet> ammo = new ArrayList<>();
    List<Bullet> enemyAmmo = new ArrayList<>();
    List<Enemy> enemies = new ArrayList<>();
    int round = 0;
    Hero hero;
    int score = 0;
    //Enemy tutorialEnemy;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ikkuna) {
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

        Label instructionsLabel = new Label("INSTRUCTIONS");  //TÄHÄN KIVEMPI TEXT
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
        Button tryAgainButtonV = new Button("TRY AGAIN");
        Button hiScoresV = new Button("");
        Button exitButtonV = new Button("EXIT");
        menuV.getChildren().addAll(gameOverLabelVictory, tryAgainButtonV, hiScoresV);
        menuV.setAlignment(Pos.CENTER);
        gameOverVictory.setCenter(menuV);
        gameOverSceneVictory = new Scene(gameOverVictory);

        //Pelinäkymä
        Pane peliRuutu = new Pane();
        peliRuutu.setPrefSize(LEVEYS, KORKEUS);

        Text roundATM = new Text();
        roundATM.setText("Tutorial");
        Text scoreText = new Text();
        roundATM.setText(String.valueOf(score));
        GridPane roundsPane = new GridPane();

        //ADD HERO
        hero = new Hero(150, 100);
        peliRuutu.getChildren().add(hero.getPoly());

        //ADD TUT ENEMY
        tut(peliRuutu);

        //Tut Text
        roundsPane.getChildren().add(roundATM);
        roundsPane.getChildren().add(scoreText);
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

                hero.move();
                hero.slowDown();

                //ROUNDS
                if (enemies.isEmpty()) {
                    round++;
                    roundATM.setText("Round " + round);

                    if (round == 1) {
                        System.out.println("r: " + 1);
                        tut(peliRuutu);
                    }
                    if (round == 2) {
                        System.out.println("r: " + 2);
                        tut(peliRuutu);
                    }
                    if (round == 3) {
                        System.out.println("cleared!");
                        stop();
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

                //ENEMY SHOOT, Fix this :)
                if (nykyhetki % 500 == 0) {
                    for (Sprite enemy : enemies) {
                        if (enemy.getLiving()) {
                            Bullet shot = new Bullet((int) enemy.getPoly().getTranslateX(), (int) enemy.getPoly().getTranslateY());
                            shot.getPoly().setRotate(shot.spritePolygon.getRotate());
                            enemyAmmo.add(shot);

                            shot.accelerate();
                            shot.setMovement(shot.getMovement().normalize().multiply(1.5));

                            peliRuutu.getChildren().add(shot.getPoly());
                        }
                    }
                }

                if (painetutNapit.getOrDefault(KeyCode.SPACE, false) && ammo.size() < 3) {
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
                /*
for(int i = 0; i<5; i++){
                    Bullet shot = new Bullet((int) hero.getPoly().getTranslateX(), (int) hero.getPoly().getTranslateY());
                    shot.getPoly().setRotate(hero.getPoly().getRotate()+circleForFive*i);
                    ammo.add(shot);

                    shot.accelerate();
                    shot.setVelocity(shot.getAcceleration().normalize().multiply(1.5));

                    peliRuutu.getChildren().add(shot.getPoly());
                    }
                 */
                ammo.forEach(ammus -> ammus.move());
                enemyAmmo.forEach(ammus -> ammus.move());

                //BULLET HITS ENEMY, HERO HITS ENEMY
                ammo.forEach(shot -> {
                    enemies.forEach(enemy -> {
                        if (shot.intersect(enemy)) {
                            shot.setLiving(false);

                            if (enemy.damage(40)) {
                                score += 10;

                            }
                            animateUsingTimeline(enemy.spritePolygon, 1.0, 1.4);

                        }

                    });

                });
                enemies.forEach(enemy -> {
                    if (hero.intersect(enemy)) {
                        hero.damage(100);
                        animateUsingTimeline(hero.spritePolygon, 1.0, 0.8);
                    }
                });

                //BULLET HITS HERO
                enemyAmmo.forEach(shot -> {
                    if (shot.intersect(hero)) {
                        hero.damage(40);
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

                if (!hero.alive) {
                    animateUsingTimeline(hero.getPoly(), 1.0, 1.4);
                    //ikkuna.setScene(gameOverScene);

                    // TOO FAST ATM
                }
            }
        }.start();

        //gameOverButton
        tryAgainButton.setOnAction(click -> {
            round = 0;
            hero = new Hero(150, 100);
            peliRuutu.getChildren().add(hero.getPoly());
            ikkuna.setScene(peliNakyma);
        });

        //---
        ikkuna.setScene(alkuNakyma);
        ikkuna.show();

    }

    void tut(Pane pane) {

        Enemy tutorialEnemy = new Enemy(300, 150);
        pane.getChildren().add(tutorialEnemy.getPoly());
        enemies.add(tutorialEnemy);

    }

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
