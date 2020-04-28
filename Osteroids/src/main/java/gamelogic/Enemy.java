/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import javafx.scene.shape.Polygon;

/** 'Tutorial' enemy.
 *<p>
 * has its own shape, moves in a circle
 * </p>
 * @author artkoski
 */
public class Enemy extends Sprite {

    /**
     * Create a tutorial enemy.
     * @param x - spawn coordinate x
     * @param y - spawn coordinate y
     */
    public Enemy(int x, int y) {
        super(new Polygon(-15, -15, 10, 0, -15, 15), x, y, 500);
    }

    @Override
    public void patrol() {
        super.toRight(0.2);
        super.slowDownShift();
        super.accelerate(0.001);
        super.slowDownShift();
        super.toRight(0.2);
        super.accelerate(0.001);
        super.slowDownShift();
    }

}
