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

    public static int LEVEYS = 600;
    public static int KORKEUS = 400;
    private Scene peliNakyma;
    private Scene alkuNakyma;

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

        Hahmo ukkeli = new Hahmo(150, 100);

        //moveCircleOnKeyPress(peliNakyma, ukkeli);
        peliRuutu.getChildren().add(ukkeli.getHahmo());

        peliNakyma = new Scene(peliRuutu);

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
                    ukkeli.vasemmalle();
                }

                if (painetutNapit.getOrDefault(KeyCode.RIGHT, false)) {
                    ukkeli.oikealle();
                }
                if (painetutNapit.getOrDefault(KeyCode.DOWN, false)) {
                    ukkeli.hidasta();
                }

                if (painetutNapit.getOrDefault(KeyCode.UP, false)) {
                    ukkeli.kiihdyta();
                    ukkeli.liiku();
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
    }

}
