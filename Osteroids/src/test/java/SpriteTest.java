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
    Sprite enemy0;
    Sprite enemy1;
    Sprite enemy2;
    Sprite bullet;

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
        sprite = new Hero(150, 100, 800);
        enemy0 = new Enemy(150, 100);
        enemy1 = new Enemy1(150, 100, 400);
        enemy2 = new Enemy2(150, 100, 2000);
        bullet = new Bullet(150, 100);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void RightTurnCorrectAmount() {
        sprite.toRight(0.3);
        sprite.toRight(0.3);
        assertEquals(0.6, sprite.getPoly().getRotate(), 0.001);
    }

    @Test
    public void LeftTurnCorrectAmount() {
        sprite.toLeft(0.3);
        sprite.toLeft(0.3);
        assertEquals(-0.6, sprite.getPoly().getRotate(), 0.001);
    }

    @Test
    public void spritesCanBeDamagedAndDie() {
        enemy1.damage(200);
        assertTrue(enemy1.getLiving());
        assertEquals(200, enemy1.getHealth(), 0.1);
        enemy1.damage(200);
        assertFalse(enemy1.getLiving());
    }

    @Test
    public void spritesCanIntersect() {
        assertTrue(sprite.intersect(enemy0));
        enemy0.getPoly().setTranslateX(100);
        enemy0.getPoly().setTranslateY(100);
        assertFalse(sprite.intersect(enemy0));
    }

    @Test
    public void spriteCanMove() {
        for (int i = 0; i < 5; i++) {
            sprite.move();
            sprite.toLeft(0.3);
            sprite.accelerate(5);
            sprite.move();
        }
        //X and Y coords different from start point
        assertTrue(sprite.getPoly().getTranslateX() != 150 && sprite.getPoly().getTranslateY() != 100);

    }

    @Test
    public void spriteCanTurnMethod2() {
        double tmpr = sprite.getPoly().getRotate();

        sprite.toLeft(50);
        assertTrue(sprite.getPoly().getRotate() != tmpr);
        tmpr = sprite.getPoly().getRotate();

        sprite.toRight(50);
        assertTrue(sprite.getPoly().getRotate() != tmpr);
    }

    @Test
    public void bulletDiesWhenOB() {
        for (int i = 0; i < 50; i++) {
            bullet.accelerate(10);
            bullet.move();
        }

        assertTrue(bullet.getLiving() == false);
    }

    @Test
    public void accelerateBack() {

        enemy2.accelerateBack();
        enemy2.move();

        assertTrue(enemy2.getMovement().getX() < 0 && enemy2.getMovement().getY() < 0);

    }

    public boolean spriteTriesToGoOB(int rotate) {

        sprite.getPoly().setRotate(rotate);
        for (int i = 0; i < 10; i++) {
            sprite.move();
            sprite.accelerate(0.5);

        }
        if ((sprite.getPoly().getTranslateX() <= 599 && sprite.getPoly().getTranslateX() >= 1) && (sprite.getPoly().getTranslateY() <= 399 && sprite.getPoly().getTranslateY() >= 1)) {
            return true;
        } else {
            return false;
        }
    }

    @Test
    public void spriteGoesOB() {
        for (int i = 1; i < 361; i += 90) {
            sprite.setMovement(new Point2D(0,0));
            assertTrue(spriteTriesToGoOB(i));
        }
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
