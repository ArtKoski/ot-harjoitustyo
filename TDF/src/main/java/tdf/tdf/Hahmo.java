/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tdf.tdf;

import com.sun.scenario.Settings;
import java.awt.Image;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javax.swing.text.html.ImageView;

/**
 *
 * @author artkoski
 */
public class Hahmo {

    private static final double W = 600, H = 400;

    //private static final String HERO_IMAGE_LOC =
    //      "http://icons.iconarchive.com/icons/raindropmemory/legendora/64/Hero-icon.png";
    //Image image;
    //ImageView imageView;
    public Polygon hahmo;
    public Point2D liike;

    public Hahmo(int x, int y) {
        this.hahmo = new Polygon(-5, -5, 10, 0, -5, 5);
        this.hahmo.setTranslateX(x);
        this.hahmo.setTranslateY(y);

        this.liike = new Point2D(0, 0);
    }

    public Polygon getHahmo() {
        return hahmo;
    }

    public void vasemmalle() {
        this.hahmo.setRotate(this.hahmo.getRotate() - 0.3);
        hidasta();
//kiihdyta();
    }

    public void oikealle() {
        this.hahmo.setRotate(this.hahmo.getRotate() + 0.3);
        hidasta();
//kiihdyta();
    }

    public void kiihdyta() {

        double muutosX = Math.cos(Math.toRadians(this.hahmo.getRotate()));
        double muutosY = Math.sin(Math.toRadians(this.hahmo.getRotate()));

        muutosX *= 0.0001;
        muutosY *= 0.0001;

        if (!(muutosX > 0.05 || muutosY > 0.05)) {
            this.liike = this.liike.add(muutosX, muutosY);
        }
    }

    public void hidasta() {
        this.liike = this.liike.subtract(liike.getX(), liike.getY());
    }

    public void liiku() {
        this.hahmo.setTranslateX(this.hahmo.getTranslateX() + this.liike.getX());
        this.hahmo.setTranslateY(this.hahmo.getTranslateY() + this.liike.getY());

        if (this.hahmo.getTranslateX() < 0 + 2) {
            this.hahmo.setTranslateX(this.hahmo.getTranslateX() + 2);
        }

        if (this.hahmo.getTranslateX() > SovellusEhka.LEVEYS - 2) {
            this.hahmo.setTranslateX(SovellusEhka.LEVEYS - 2);

        }

        if (this.hahmo.getTranslateY() < 0 + 2) {
            this.hahmo.setTranslateY(this.hahmo.getTranslateY() + 2);

        }

        if (this.hahmo.getTranslateY() > SovellusEhka.KORKEUS - 2) {
            this.hahmo.setTranslateY(SovellusEhka.KORKEUS - 2);
        }

    }

    public Point2D getLiike() {
        return this.liike;
    }

}
