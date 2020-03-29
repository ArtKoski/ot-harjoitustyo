/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tdf.tdf;

import java.awt.Label;
import java.awt.TextField;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import tdf.tdf.Hahmo;

/**
 *
 * @author artkoski
 */
public class SovellusEhka extends Application {

    private static final int KEYBOARD_MOVEMENT_DELTA = 5;

    private Scene peliNakyma;
    private Scene alkuNakyma;

    @Override
    public void start(Stage ikkuna) {
        //ALKURUUTU
        Pane ruutu = new Pane();
        ruutu.setPrefSize(600, 400);

        Button nappi = new Button("START");
        ruutu.getChildren().add(nappi);

        alkuNakyma = new Scene(ruutu);

        //Pelinäkymä
        Pane peliRuutu = new Pane();
        peliRuutu.setPrefSize(600, 400);

        Hahmo ukkeli = new Hahmo(150, 100);
        peliRuutu.getChildren().add(ukkeli.getHahmo());

        peliNakyma = new Scene(peliRuutu);
        moveCircleOnKeyPress(peliNakyma, ukkeli);

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

                if (painetutNapit.getOrDefault(KeyCode.UP, Boolean.FALSE)) {
                    ukkeli.liike.add(ukkeli.getLiike().getX(), ukkeli.liike.getY() - KEYBOARD_MOVEMENT_DELTA);
                }

                if (painetutNapit.getOrDefault(KeyCode.RIGHT, Boolean.FALSE)) {
                    ukkeli.liike.add(ukkeli.liike.getX() + KEYBOARD_MOVEMENT_DELTA, ukkeli.getLiike().getY());
                }

                if (painetutNapit.getOrDefault(KeyCode.DOWN, Boolean.FALSE)) {
                    ukkeli.liike.add(ukkeli.getLiike().getX(), ukkeli.liike.getY() + KEYBOARD_MOVEMENT_DELTA);
                }

                if (painetutNapit.getOrDefault(KeyCode.LEFT, Boolean.FALSE)) {
                    ukkeli.liike.add(ukkeli.liike.getX() - KEYBOARD_MOVEMENT_DELTA, ukkeli.getLiike().getY());
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

    public static void main(String[] args) {
        launch(args);
    }

    private void moveCircleOnKeyPress(Scene scene, Hahmo hahmo) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case UP:
                        hahmo.liike.add(hahmo.getLiike().getX(), hahmo.liike.getY() - KEYBOARD_MOVEMENT_DELTA);
                        break;
                    case RIGHT:
                        hahmo.liike.add(hahmo.liike.getX() + KEYBOARD_MOVEMENT_DELTA, hahmo.getLiike().getY());
                        break;
                    case DOWN:
                        hahmo.liike.add(hahmo.getLiike().getX(), hahmo.liike.getY() + KEYBOARD_MOVEMENT_DELTA);
                        break;
                    case LEFT:
                        hahmo.liike.add(hahmo.liike.getX() - KEYBOARD_MOVEMENT_DELTA, hahmo.getLiike().getY());
                        break;
                }
            }
        });
    }

}
