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
import tdf.tdf.Hahmo;
import launch.Main;

/**
 *
 * @author artkoski
 */
public class HeroTest {
    
    Hahmo hero;
    
    public HeroTest() {
    }
    @BeforeClass
    public static void setUpClass() {

    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        hero = new Hahmo(150, 100);
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void heroRightTurnCorrectAmount() {
        hero.oikealle();
        hero.oikealle();
        assertEquals(0.6, hero.getHahmo().getRotate(), 0.001);
    } 
    @Test
    public void heroLeftTurnCorrectAmount() {
        hero.vasemmalle();
        hero.vasemmalle();
        assertEquals(-0.6, hero.getHahmo().getRotate(), 0.001);
    } 
    
    
    
    

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
