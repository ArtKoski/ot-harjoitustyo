/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import ui.GUI;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

/**
 * Class for bullets in the game.
 * <p>
 * Can be manipulated like any other Sprite
 * </p>
 *
 * @author artkoski
 */
public class Bullet extends Sprite {

    /**
     * Create a bullet.
     *
     * @param x - spawn coordinate x
     * @param y - spawn coordinate y
     */
    public Bullet(int x, int y) {
        super(new Polygon(1.5, -1.5, 1.5, 1.5, -1.5, 1.5, -1.5, -1.5), x, y, 1);
    }

    @Override
    void updateColour() {
    }

    @Override
    public boolean damage(double x) {
        die();
        return true;
    }

    /**
     * Movement logic for bullets.
     * <p>
     * Only difference to the original method is that the bullet dies when it goes
     * out of bounds.
     * </p>
     */
    @Override
    public void move() {
        this.getPoly().setTranslateX(this.getPoly().getTranslateX() + this.getMovement().getX());
        this.getPoly().setTranslateY(this.getPoly().getTranslateY() + this.getMovement().getY());

        if (this.getPoly().getTranslateX() < 0 + 2) {
            die();
        }

        if (this.getPoly().getTranslateX() > GUI.LEVEYS - 2) {
            die();
        }

        if (this.getPoly().getTranslateY() < 0 + 2) {
            die();
        }

        if (this.getPoly().getTranslateY() > GUI.KORKEUS - 2) {
            die();
        }

    }
}
