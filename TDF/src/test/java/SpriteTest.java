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
import gamelogic.Hero;
import launch.Main;
import gamelogic.Enemy;
import gamelogic.Sprite;

/**
 *
 * @author artkoski
 */
public class SpriteTest {

    Sprite sprite;
    Sprite sprite2;
    Sprite sprite3;
    

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
        sprite2 = new Hero(150, 100);
        sprite3 = new Enemy(150, 100);  //Test this b
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
        sprite.damage(200);
        assertTrue(sprite.getLiving());
        assertEquals(200, sprite.getHealth());
        sprite.damage(200);
        assertFalse(sprite.getLiving());
    }

    @Test
    public void spritesCanIntersect() {
        assertTrue(sprite.intersect(sprite2));
        sprite2.spritePolygon.setTranslateX(100);
        sprite2.spritePolygon.setTranslateY(100);
        assertFalse(sprite.intersect(sprite2));
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
