/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import javafx.scene.shape.Polygon;

/** Round 2 enemy.
 *<p>
 * spawns in middle and only rotates, shoots in every direction 
 * </p>
 * @author artkoski
 */
public class Enemy2 extends Sprite {

    /**
     * Create a round 2 enemy.
     * @param x - spawn coordinate x
     * @param y  -spawn coordinate y
     * @param hp - amount of health
     */
    public Enemy2(int x, int y, double hp) {
        super(new Polygon(-20, -10, 10, 0, -20, 10), x, y, hp);
    }

    @Override
    public void patrol() {
        super.toLeft(0.3);
    }

}
