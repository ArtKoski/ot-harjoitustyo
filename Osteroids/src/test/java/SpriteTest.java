/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gamelogic.*;
import javafx.geometry.Point2D;
import launch.Main;

/**
 *
 * @author artkoski
 */
public class SpriteTest {

    Sprite sprite;
    Sprite sprite2;
    Sprite sprite3;
    Sprite sprite4;
    Sprite sprite5;

    public SpriteTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        sprite = new Hero(150, 100);
        sprite2 = new Enemy(150, 100);
        sprite3 = new Enemy1(150, 100);
        sprite4 = new Enemy2(150, 100);
        sprite5 = new Bullet(150, 100);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void RightTurnCorrectAmount() {
        sprite.toRight();
        sprite.toRight();
        assertEquals(0.6, sprite.getPoly().getRotate(), 0.001);
    }

    @Test
    public void LeftTurnCorrectAmount() {
        sprite.toLeft();
        sprite.toLeft();
        assertEquals(-0.6, sprite.getPoly().getRotate(), 0.001);
    }

    @Test
    public void spritesCanBeDamagedAndDie() {
        sprite3.damage(200);
        assertTrue(sprite3.getLiving());
        assertEquals(200, sprite3.getHealth());
        sprite3.damage(200);
        assertFalse(sprite3.getLiving());
    }

    @Test
    public void spritesCanIntersect() {
        assertTrue(sprite.intersect(sprite2));
        sprite2.spritePolygon.setTranslateX(100);
        sprite2.spritePolygon.setTranslateY(100);
        assertFalse(sprite.intersect(sprite2));
    }

    @Test
    public void spriteCanMove() {
        for (int i = 0; i < 5; i++) {
            sprite.move();
            sprite.toLeft();
            sprite.accelerate(5, 5);
            sprite.move();
        }
        //X and Y coords different from start point
        assertTrue(sprite.spritePolygon.getTranslateX() != 150 && sprite.spritePolygon.getTranslateY() != 100);

    }

    @Test
    public void spriteCanTurnMethod2() {
        double tmpr = sprite.spritePolygon.getRotate();

        sprite.toLeft(50);
        assertTrue(sprite.spritePolygon.getRotate() != tmpr);
        tmpr = sprite.spritePolygon.getRotate();

        sprite.toRight(50);
        assertTrue(sprite.spritePolygon.getRotate() != tmpr);
    }

    @Test
    public void bulletDiesWhenOB() {
        for (int i = 0; i < 50; i++) {
            sprite5.accelerate(10, 10);
            sprite5.move();
        }

        assertTrue(sprite5.getLiving() == false);
    }

    @Test
    public void accelerateBack() {
        
        
        sprite4.accelerateBack();
        sprite4.move();
        
        
        assertTrue(sprite4.getMovement().getX()<0 && sprite4.getMovement().getY()<0);
       
        
    }
    
    @Test
    public void immunityWorksProper() {
        int tmpr = sprite.getHealth();
        sprite.immunityOn();
        sprite.damage(3000);
        sprite.immunityOff();
        assertEquals(tmpr, sprite.getHealth());
        assertTrue(sprite.getLiving());
    }
    
    @Test
    public void spriteTriesToGoOB() {
        int rotateKerroin = 1;
        for(int i = 0; i<10000;i++){
        sprite.move();
        sprite.accelerate();
        if(i%2500==0) {
            sprite.setMovement(new Point2D(0,0));
            sprite.getPoly().setRotate(90*rotateKerroin);
            rotateKerroin++;
        }
        assertTrue(sprite.spritePolygon.getTranslateX()<=598 && sprite.spritePolygon.getTranslateX()>=2); //check that the mfer is inbounds
        assertTrue(sprite.spritePolygon.getTranslateY()<=398 && sprite.spritePolygon.getTranslateY()>=2);
        }
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
