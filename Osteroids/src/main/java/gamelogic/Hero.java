/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import javafx.scene.shape.Polygon;

/**
 * Hero sprite
 * <p>
 * Player-controlled sprite.
 * </p>
 *
 * @author artkoski
 */
public class Hero extends Sprite {

    /**
     * Spawn the hero.
     * <p>
     * Spawns the hero.
     * </p>
     *
     * @param x - spawn coordinate x
     * @param y - spawn coordinate y
     * @hp - amount of health
     */
    public Hero(int x, int y, double hp) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y, hp);
    }
}
