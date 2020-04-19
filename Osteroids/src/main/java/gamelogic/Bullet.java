/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import ui.GUI;
import javafx.scene.shape.Polygon;

/**
 *
 * @author artkoski
 */
public class Bullet extends Sprite {

    public Bullet(int x, int y) {
        super(new Polygon(2, -2, 2, 2, -2, 2, -2, -2), x, y);
    }

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
