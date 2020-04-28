/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import ui.GUI;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * Basis for all Sprites
 * <p>
 * All moving objects extend this class. Allows movement, rotation, logic behind
 * damage/dying, colours based on hp
 * </p>
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
    int tmpr;

    /**
     * Create a sprite.
     *
     * @param poly - shape of sprite (different object have different shapes)
     * @param x - spawn coordinate x
     * @param y - spawn coordinate y
     * @param hp - health when spawned
     */
    public Sprite(Polygon poly, int x, int y, int hp) {
        this.spritePolygon = poly;
        this.spritePolygon.setTranslateX(x);
        this.spritePolygon.setTranslateY(y);
        this.health = hp;
        this.spritePolygon.setFill(Color.GREEN);
        alive = true;

        this.movement = new Point2D(0, 0);
    }

    public Polygon getPoly() {
        return spritePolygon;
    }

    /**
     * Grants the sprite immunity to damage.
     * <p>
     * health is temporarily set to a high amount and returns to normal whenever
     * the counterpart method immunityOff() is called.
     * </p>
     */
    public void immunityOn() {
        tmpr = health;
        health = 10_000;
        spritePolygon.setFill(Color.CORAL);
        spritePolygon.setOpacity(80);
    }

    /**
     * Turns off immunity.
     * <p>
     * Health set back to amount before immunity.
     * </p>
     */
    public void immunityOff() {
        health = tmpr;
        spritePolygon.setOpacity(100);
        updateColour();
    }

    void hitColour() { //CHECK FUNCTIONALITY
        Paint colourATM = spritePolygon.getFill();
        for (int i = 0; i < 1000; i++) {

            this.spritePolygon.setFill(Color.LIGHTPINK);
            this.spritePolygon.setFill(colourATM);                          //TÄHÄN JOKU PIENI TIMER
        }
    }

    /**
     * Updates colour based on HP.
     */
    void updateColour() { //CHANGE TO PERCENTAGES!!!
        if (this.health >= 400) {
            spritePolygon.setFill(Color.GREEN);
        }
        if (this.health < 400) {
            spritePolygon.setFill(Color.YELLOW);
        }
        if (this.health < 200) {
            spritePolygon.setFill(Color.RED);
        }
        if (this.health < 50) {
            spritePolygon.setFill(Color.DARKRED);
        }
    }

    /**
     * Called when sprites health goes to zero.
     */
    public void die() {        //Needs something.

        for (int i = 1; i <= 100; i++) {
            this.spritePolygon.setOpacity(spritePolygon.getOpacity() - 0.01);
        }

    }

    /**
     * Detects collision between sprites.
     *
     * @param other - another sprite
     * @return true if collision detected with other sprite, otherwise false
     */
    public boolean intersect(Sprite other) {
        Shape tormaysalue = Shape.intersect(this.spritePolygon, other.getPoly());
        return tormaysalue.getBoundsInLocal().getWidth() != -1;
    }

    /**
     * Turns the sprite to the left by 'amount'.
     * @param amount - how much the sprite is turned left
     */
    public void toLeft(double amount) {
        this.spritePolygon.setRotate(this.spritePolygon.getRotate() - amount);
        slowDown();
    }
/**
 * Turns the sprite to the right by 'amount'.
 * @param amount - how much the sprite is turned right
 */
    public void toRight(double amount) {
        this.spritePolygon.setRotate(this.spritePolygon.getRotate() + amount);
        slowDown();

    }

    /**
     * Makes to sprite accelerate backwards movement.
     */
    public void accelerateBack() {
        double muutosX = Math.cos(Math.toRadians(this.spritePolygon.getRotate()) - Math.PI);
        double muutosY = Math.sin(Math.toRadians(this.spritePolygon.getRotate()) - Math.PI);

        muutosX *= 0.0005;
        muutosY *= 0.0005;

        if (!(muutosX > 0.05 || muutosY > 0.05)) {
            this.movement = this.movement.add(muutosX, muutosY);
        }
    }

    /**
     * Accelerate sprites movement forward.
     * <p>
     * calculates the sprite's 'front' and adds movement towards that direction.
     * Amount of acceleration is modified by parameter 'amount'.
     * </p>
     *
     * @param amount - acceleration amount relative to x and y (normal 0.0005)
     */
    public void accelerate(double amount) {

        double muutosX = amount * Math.cos(Math.toRadians(this.spritePolygon.getRotate()));
        double muutosY = amount * Math.sin(Math.toRadians(this.spritePolygon.getRotate()));

        this.movement = this.movement.add(muutosX, muutosY);

    }
/**
 * Slows down sprites movement a little.
 */
    public void slowDown() {
        this.movement = this.movement.subtract(movement.getX() * 0.0005, movement.getY() * 0.0005);
    }
/**
 * Slows down movement a lot. 
 */
    public void slowDownShift() {
        this.movement = this.movement.subtract(movement.getX() * 0.005, movement.getY() * 0.005);
    }

    public void setMovement(Point2D change) {
        this.movement = change;
    }

    public Point2D getMovement() {
        return movement;
    }

    /**
     * Basic sprite movement. Doesn't allow out of bounds movement.
     */
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

    /**
     * Sprite loses health.
     * <p>
     * sprite's health is reduced by x amount. Triggers other methods to update
     * sprite's colour based on the current health.
     * </p>
     *
     * @param x - amount of health lost
     * @return if the damage 'kills' the sprite return true, otherwise false
     * @see hitColour();, updateColour();
     */
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
/**
 * Method for sprites patrol movement. Different for every sprite, thus defined in sub-classes.
 */
    public void patrol() {
    }
}
