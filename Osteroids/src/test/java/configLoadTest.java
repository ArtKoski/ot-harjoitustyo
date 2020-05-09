/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import applogic.SheetsLeaderBoards;
import applogic.Score;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import applogic.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import javafx.geometry.Point2D;
import launch.Main;
/**
 *
 * @author artkoski
 */
public class configLoadTest {
loadDifficulty config;

    public configLoadTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
     }

    @After
    public void tearDown() {
    }

    @Test
    public void loadNormal() {
        config = new loadDifficulty(1);
        ArrayList<Double> values = config.difficultyConfig();
        assertTrue(!values.isEmpty());
    }

    @Test
    public void loadHard() {
        config = new loadDifficulty(2);
        ArrayList<Double> values = config.difficultyConfig();
        assertTrue(!values.isEmpty());
    }
    

    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
