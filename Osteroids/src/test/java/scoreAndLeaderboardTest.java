/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import applogic.Score;
import applogic.SheetsLeaderBoards;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import gamelogic.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.util.PriorityQueue;
import javafx.geometry.Point2D;
import launch.Main;

/**
 *
 * @author artkoski
 */
public class scoreAndLeaderboardTest {

    Score score1;
    Score score2;
    SheetsLeaderBoards hiScores;

    public scoreAndLeaderboardTest() {
    }

    @BeforeClass
    public static void setUpClass() {

    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        hiScores = new SheetsLeaderBoards();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void makeNewScore() {
        double time = 3.5;
        int round = 2;
        int points = 30;
        int difficulty = 1;
        score1 = new Score(time, round, points, difficulty);
        assertEquals(time, score1.getTime(), 0.01);
        assertEquals(round, score1.getRound());
        assertEquals(points, score1.getPoints());
        assertEquals(difficulty, score1.getDifficulty());
    }

    @Test
    public void scoresCompareProper() {
        PriorityQueue<Score> pQueue = new PriorityQueue<Score>();
        pQueue.add(new Score(15, 1, 20, 1));
        pQueue.add(new Score(10, 2, 30, 1));
        pQueue.add(new Score(5, 2, 20, 1));
        pQueue.add(new Score(100, 4, 10, 1));

        assertEquals(100, pQueue.poll().getTime(), 0.1);
        assertEquals(10, pQueue.poll().getTime(), 0.01);
        assertEquals(5, pQueue.poll().getTime(), 0.01);
        assertEquals(15, pQueue.poll().getTime(), 0.01);

    }

    @Test
    public void scoreToStringFunctions() {
        score1 = new Score("nub", 25.3, 3, 40);
        assertEquals("nub, Time: 25.3 seconds, round 3", score1.toString());
        score2 = new Score(25.3, 3, 40, 2);
        assertEquals("Time: 25.3 seconds, round 3", score1.toStringNoName());
    }
    
    @Test
    public void ableToLoadConfigAndSearchLeaderboard() throws IOException, GeneralSecurityException, ParseException {
        hiScores.init();
        //Normal mode table
        hiScores.search(1);
        //Hard mode table
        hiScores.search(2);
    }

    @Test
    public void updateLeaderboardByAddingScoreThenRemoving() throws IOException, GeneralSecurityException, ParseException {
        hiScores.init();
        //ADD NEW BEST SCORE
        hiScores.leaderboardUpdate("king", new Score(0, 4, 150, 5));
        PriorityQueue<Score> q = hiScores.search(5);
        assertEquals(0, q.peek().getTime(), 0.01);
        
        //ADD NEW SCORE ON TOP OF OLD BEST
        hiScores.update("", new Score(1, 0, 0, 0), "M2");
        q = hiScores.search(5);
        assertEquals(1, q.peek().getTime(), 0.01);
    }
    
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
}
