/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import com.sun.scenario.Settings;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import javax.swing.text.html.ImageView;

/**
 *
 * @author artkoski
 */
public class Hero extends Sprite {

    private static final double W = 600, H = 400;

    //private static final String HERO_IMAGE_LOC =
    //      "http://icons.iconarchive.com/icons/raindropmemory/legendora/64/Hero-icon.png";
    //Image image;
    //ImageView imageView;
    public Hero(int x, int y) {

        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);
        super.health=600;
    }
    
    @Override
      public void die() {        //PIENI ANIMAATIO??
        /*
        for (int i = 1; i <= 10; i++) {
            this.spritePolygon.setOpacity(spritePolygon.getOpacity() - 0.1);
        }
         *DoubleProperty scale = new SimpleDoubleProperty(1);
        super.spritePolygon.scaleXProperty().bind(scale);
        super.spritePolygon.scaleYProperty().bind(scale);

        Timeline beat = new Timeline(
                new KeyFrame(Duration.ZERO, event -> scale.setValue(1)),
                new KeyFrame(Duration.seconds(0.1), event -> scale.setValue(2))
        );
        beat.setAutoReverse(true);
        beat.setCycleCount(2);
        beat.play();
    */}

}
