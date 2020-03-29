/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tdf.tdf;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;

/**
 *
 * @author artkoski
 */
public class Hahmo {
    private Polygon hahmo;
    public Point2D liike;
    
    public Hahmo(int x, int y) {
        this.hahmo = new Polygon(-15,15, 10, 0, 15, 15);
        this.hahmo.setTranslateX(x);
        this.hahmo.setTranslateY(y);
        
        this.liike = new Point2D(0, 0);
    }
    
    public Polygon getHahmo() {
        return hahmo;
    }
    
    public void vasemmalle() {
        this.hahmo.setRotate(this.hahmo.getRotate() - 2);
    }
    public void oikealle() {
        this.hahmo.setRotate(this.hahmo.getRotate() + 2);
    }
    
    public void liiku() {
        this.hahmo.setTranslateX(this.hahmo.getTranslateX() + this.liike.getX());
        this.hahmo.setTranslateX(this.hahmo.getTranslateY() + this.liike.getY());
    }
    public Point2D getLiike() {
        return this.liike;
    }
    
}
