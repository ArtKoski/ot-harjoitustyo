/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import javafx.scene.shape.Polygon;

/**
 *
 * @author artkoski
 */
public class Enemy1 extends Sprite {

    public Enemy1(int x, int y) {
        super(new Polygon(-15, -15, 10, 0, -15, 15), x, y);
    }

    @Override
    public void patrol() {
        super.toLeft(0.4);
        super.slowDownShift();
        super.accelerate(0.001, 0.001);
        super.slowDownShift();
        super.toLeft(0.6);
        super.accelerate(0.001, 0.001);
        super.slowDownShift();
    }

}
