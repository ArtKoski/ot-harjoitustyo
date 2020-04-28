/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import javafx.scene.shape.Polygon;

/** First round enemy.
 * <p>
 * slightly different movement pattern from tutorial enemy
 * </p>
 * @author artkoski
 */
public class Enemy1 extends Sprite {

    /**
     * Create a round 1 enemy.
     * 
     * @param x - spawn coordinate x
     * @param y  -spawn coordinate y
     */
    public Enemy1(int x, int y) {
        super(new Polygon(-15, -15, 10, 0, -15, 15), x, y, 400);
    }

    @Override
    public void patrol() {
        super.toLeft(0.4);
        super.slowDownShift();
        super.accelerate(0.001);
        super.slowDownShift();
        super.toLeft(0.6);
        super.accelerate(0.001);
        super.slowDownShift();
    }

}
