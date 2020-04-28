/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import javafx.scene.shape.Polygon;

/**
 * Hero sprite
 * <p>
 * Player-controlled sprite.
 * </p>
 *
 * @author artkoski
 */
public class Hero extends Sprite {

    //private static final String HERO_IMAGE_LOC =
    //      "http://icons.iconarchive.com/icons/raindropmemory/legendora/64/Hero-icon.png";
    //Image image;
    //ImageView imageView;
    /**
     * Spawn the hero.
     * <p>
     * Spawns the hero. Health is adjustable in configuration. (to be implemented)
     * </p>
     * @param x - spawn coordinate x
     * @param y - spawn coordinate y
     */
    public Hero(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y, 800);
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
         */
    }
}
