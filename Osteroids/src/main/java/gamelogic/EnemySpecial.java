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
public class EnemySpecial extends Sprite {

    public EnemySpecial(int x, int y) {
        super(new Polygon(-50, -50, 50, 50, -50, 50), x, y, 5000);
    }

    @Override
    public void patrol() {
        super.toLeft(0.0005);
    }

}
