/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Ui;

import java.awt.Canvas;
import java.awt.Label;
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

/**
 *
 * @author artkoski
 */
public class GUI extends Application {

    public static int LEVEYS = 600;
    public static int KORKEUS = 400;
    private Scene peliNakyma;
    private Scene alkuNakyma;
    int circleForFive = 72;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage ikkuna) {
        //ALKURUUTU
        Pane ruutu = new Pane();
        ruutu.setPrefSize(LEVEYS, KORKEUS);

        Button nappi = new Button("START");
        ruutu.getChildren().add(nappi);

        alkuNakyma = new Scene(ruutu);

        //Pelinäkymä
        Pane peliRuutu = new Pane();
        peliRuutu.setPrefSize(LEVEYS, KORKEUS);

        Hero hero = new Hero(150, 100);
        Enemy enemy = new Enemy(300, 150);

        peliRuutu.getChildren().add(hero.getPoly());
        peliRuutu.getChildren().add(enemy.getPoly());

        peliNakyma = new Scene(peliRuutu);
        Rectangle heroHPbar = new Rectangle(200.0, 50.0, Color.RED);

        List<Bullet> ammo = new ArrayList<>();
        List<Bullet> enemyAmmo = new ArrayList<>();
        List<Enemy> enemies = new ArrayList<>();
        enemies.add(enemy);
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

                if (painetutNapit.getOrDefault(KeyCode.UP, false)) {
                    hero.accelerate();
                }

                hero.move();
                hero.slowDown();

                //ENEMY PATROL
                enemy.move();
                if (nykyhetki % 10 == 0) {
                    enemy.patrol();
                }

                //ENEMY SHOOT
                if (nykyhetki % 500 == 0) {
                    for (Sprite enemy : enemies) {
                        if(enemy.getLiving()) {
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

                //BULLET HITS ENEMY
                ammo.forEach(shot -> {
                    enemies.forEach(enemy -> {
                        if (shot.intersect(enemy)) {
                            shot.setLiving(false);

                            enemy.damage(40);
                            animateUsingTimeline(enemy.spritePolygon, 1.0, 1.4);

                        }
                    });

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
                    if(animateUsingTimeline2(hero.getPoly(), 1, 1));  // TOO FAST ATM
                }
            }
        }.start();

        nappi.setOnAction(click -> {
            ikkuna.setScene(peliNakyma);
        });

        //---
        ikkuna.setScene(alkuNakyma);
        ikkuna.show();

    }

    private void animateUsingTimeline(Polygon thing, double value1, double value2) {
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
    }
private boolean animateUsingTimeline2(Polygon thing, double value1, double value2) {
        DoubleProperty scale = new SimpleDoubleProperty(1);
        thing.scaleXProperty().bind(scale);
        thing.scaleYProperty().bind(scale);
        for(int i =1 ;i<10;i++) {
            final int add=i;
        Timeline beat = new Timeline(
                new KeyFrame(Duration.ZERO, event -> scale.setValue(value1)),
                new KeyFrame(Duration.seconds(i/10), event -> scale.setValue(value2+add))
        );
        beat.setAutoReverse(true);
        beat.setCycleCount(2);
        beat.play();
        }
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
