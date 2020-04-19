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
public class Enemy2 extends Sprite {

    public Enemy2(int x, int y) {
        super(new Polygon(-20, -10, 10, 0, -20, 10), x, y);
        super.health=2000;
    }

    @Override
    public void patrol() {
        super.toLeft();
    }

}
