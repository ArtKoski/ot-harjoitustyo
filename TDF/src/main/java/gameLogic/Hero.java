/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import com.sun.scenario.Settings;
import java.awt.Image;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
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
    }

}
