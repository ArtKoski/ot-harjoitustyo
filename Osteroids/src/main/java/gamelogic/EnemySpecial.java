/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gamelogic;

import javafx.scene.shape.Polygon;

/** 'Boss' enemy.
 *<p>
 * big one, more hp, doesn't shoot.
 * </p>
 * @author artkoski
 */
public class EnemySpecial extends Sprite {
    
    double enrage;

    /**
     * Spawn a boss enemy. 
     * @param x - spawn coordinate x
     * @param y - spawn coordinate y
     */
    public EnemySpecial(int x, int y, double hp) {
        super(new Polygon(-50, -50, 50, 50, -50, 50), x, y, hp);
    }

    @Override
    public void patrol() {
    }
    
    public void setEnrage(double val) {
        this.enrage = val;
    }
    public double getEnrage() {
        return this.enrage;
    }
    

}
