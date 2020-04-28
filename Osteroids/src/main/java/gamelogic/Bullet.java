/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import ui.GUI;
import javafx.scene.shape.Polygon;

/** Class for bullets in the game.
 *<p>
 * Can be manipulated like any other Sprite
 * </p>
 * @author artkoski
 */
public class Bullet extends Sprite {

    /**
     * Create a bullet.
     * @param x - spawn coordinate x
     * @param y - spawn coordinate y
     */
    public Bullet(int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y, 5);
    }
/**
 * Movement logic for bullets.
 * <p>
 * Only difference to original method is that the bullet dies when it goes out of bounds.
 * </p>
 */
    @Override
    public void move() {
        this.spritePolygon.setTranslateX(this.spritePolygon.getTranslateX() + this.movement.getX());
        this.spritePolygon.setTranslateY(this.spritePolygon.getTranslateY() + this.movement.getY());

        if (this.spritePolygon.getTranslateX() < 0 + 2) {
            this.setLiving(false);
        }

        if (this.spritePolygon.getTranslateX() > GUI.LEVEYS - 2) {
            this.setLiving(false);
        }

        if (this.spritePolygon.getTranslateY() < 0 + 2) {
            this.setLiving(false);
        }

        if (this.spritePolygon.getTranslateY() > GUI.KORKEUS - 2) {
            this.setLiving(false);
        }

    }
}
