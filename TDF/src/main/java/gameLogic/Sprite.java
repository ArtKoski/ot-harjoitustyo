/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameLogic;

import Ui.GUI;
import com.sun.scenario.Settings;
import java.awt.Image;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javax.swing.text.html.ImageView;

/**
 *
 * @author artkoski
 */
public abstract class Sprite {

    private static final double W = 600, H = 400;

    //private static final String HERO_IMAGE_LOC =
    //      "http://icons.iconarchive.com/icons/raindropmemory/legendora/64/Hero-icon.png";
    //Image image;
    //ImageView imageView;
    public Polygon spritePolygon;
    public Point2D movement;
    public int health;
    public boolean alive;

    public Sprite(int x, int y) {
        this.spritePolygon = new Polygon(-5, -5, 10, 0, -5, 5);
        this.spritePolygon.setTranslateX(x);
        this.spritePolygon.setTranslateY(y);
        this.health = 400;
        alive = true;

        this.movement = new Point2D(0, 0);
    }

    public Sprite(Polygon poly, int x, int y) {
        this.spritePolygon = poly;
        this.spritePolygon.setTranslateX(x);
        this.spritePolygon.setTranslateY(y);
        this.health = 400;
        alive = true;

        this.movement = new Point2D(0, 0);
    }

    public Polygon getPoly() {
        return spritePolygon;
    }

    void hitColour() {
        try {
            Paint tmpr = spritePolygon.getFill();
            for (int i = 0; i < 1000; i++) {

                this.spritePolygon.setFill(Color.LIGHTPINK);
                this.spritePolygon.setFill(tmpr);                          //TÄHÄN JOKU PIENI TIMER
            }
        } catch (Exception e) {
            System.out.println("lul");
        }
    }

    void updateColour() {
        if (this.health == 400) {
            spritePolygon.setFill(Color.BLACK);
        }
        if (this.health < 400) {
            spritePolygon.setFill(Color.GREY);
        }
        if (this.health < 200) {
            spritePolygon.setFill(Color.RED);
        }
        if (this.health < 50) {
            spritePolygon.setFill(Color.DARKRED);
        }
    }

    public void die() {        //PIENI ANIMAATIO??
        /*for(int i = 1; i<20; i++) {
            this.hahmo.setScaleX(1.25);
            this.hahmo.setScaleY(1.25);
        }*/
        for (int i = 1; i <= 10; i++) {
            this.spritePolygon.setOpacity(spritePolygon.getOpacity() - 0.1);
        }

    }

    public boolean intersect(Sprite other) {
        Shape tormaysalue = Shape.intersect(this.spritePolygon, other.getPoly());
        return tormaysalue.getBoundsInLocal().getWidth() != -1;
    }

    public void toLeft() {
        this.spritePolygon.setRotate(this.spritePolygon.getRotate() - 0.3);
        slowDown();

    }

    public void toRight() {
        this.spritePolygon.setRotate(this.spritePolygon.getRotate() + 0.3);
        slowDown();

    }

    public void toLeft(double amount) {
        this.spritePolygon.setRotate(this.spritePolygon.getRotate() - amount);
        slowDown();
    }

    public void toRight(double amount) {
        this.spritePolygon.setRotate(this.spritePolygon.getRotate() + amount);
        slowDown();

    }

    public void accelerate() {

        double muutosX = Math.cos(Math.toRadians(this.spritePolygon.getRotate()));
        double muutosY = Math.sin(Math.toRadians(this.spritePolygon.getRotate()));

        muutosX *= 0.0005;
        muutosY *= 0.0005;

        if (!(muutosX > 0.05 || muutosY > 0.05)) {
            this.movement = this.movement.add(muutosX, muutosY);
        }
    }

    public void accelerateBack() {
        double muutosX = Math.cos(Math.toRadians(this.spritePolygon.getRotate()) - Math.PI);
        double muutosY = Math.sin(Math.toRadians(this.spritePolygon.getRotate()) - Math.PI);

        muutosX *= 0.0005;
        muutosY *= 0.0005;

        if (!(muutosX > 0.05 || muutosY > 0.05)) {
            this.movement = this.movement.add(muutosX, muutosY);
        }
    }

    public void accelerate(double x, double y) {

        double muutosX = x * Math.cos(Math.toRadians(this.spritePolygon.getRotate()));
        double muutosY = y * Math.sin(Math.toRadians(this.spritePolygon.getRotate()));

        this.movement = this.movement.add(muutosX, muutosY);

    }

    public void slowDown() {
        this.movement = this.movement.subtract(movement.getX() * 0.0005, movement.getY() * 0.0005);
    }

    public void slowDownShift() {
        this.movement = this.movement.subtract(movement.getX() * 0.005, movement.getY() * 0.005);
    }

    public void setMovement(Point2D change) {
        this.movement = change;
    }

    public Point2D getMovement() {
        return movement;
    }

    public void move() {
        this.spritePolygon.setTranslateX(this.spritePolygon.getTranslateX() + this.movement.getX());
        this.spritePolygon.setTranslateY(this.spritePolygon.getTranslateY() + this.movement.getY());

        if (this.spritePolygon.getTranslateX() < 0 + 2) {
            this.spritePolygon.setTranslateX(this.spritePolygon.getTranslateX() + 2);
        }

        if (this.spritePolygon.getTranslateX() > GUI.LEVEYS - 2) {
            this.spritePolygon.setTranslateX(GUI.LEVEYS - 2);

        }

        if (this.spritePolygon.getTranslateY() < 0 + 2) {
            this.spritePolygon.setTranslateY(this.spritePolygon.getTranslateY() + 2);

        }

        if (this.spritePolygon.getTranslateY() > GUI.KORKEUS - 2) {
            this.spritePolygon.setTranslateY(GUI.KORKEUS - 2);
        }

    }

    public int getHealth() {
        return health;
    }

    public double getHealthPercentage() {
        return health / 400;
    }

    public boolean damage(int x) {
        this.health -= x;

        hitColour();
        updateColour();
        if (this.health <= 0) {
            setLiving(false);
            die();
            return true;
        }
        return false;
    }

    public void setLiving(boolean state) {
        this.alive = state;
    }

    public boolean getLiving() {
        return alive;
    }

}
