/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applogic;

import java.util.Comparator;
import ui.GUI;

/**
 *
 * @author artkoski
 *
 * Class for scores
 *
 */
public class Score implements Comparable<Score> {

    private String name;
    private double time;
    private int round;
    private int points;
    private int difficulty;

    /**
     * Constructor used in GUI for scores during a session
     *
     * @param time
     * @param round
     * @param points
     * @param difficulty
     */
    public Score(double time, int round, int points, int difficulty) {
        this.time = time;
        this.round = round;
        this.points = points;
        this.difficulty = difficulty;
    }

    /**
     * Constructor used whenever a score is uploaded to or downloaded 
     * from the leaderboards.
     *
     * @param name
     * @param time
     * @param round
     * @param points
     */
    public Score(String name, double time, int round, int points) {
        this.name = name;
        this.time = time;
        this.round = round;
        this.points = points;
    }

    public double getTime() {
        return time;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getRound() {
        return round;
    }

    public int getPoints() {
        return points;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        if (round < 4) {
            return name + ", Time: " + time + " seconds, round " + round;
        } else {
            return name + ", Time: " + time + " seconds, Boss Defeated! ";
        }
    }

    public String toStringNoName() {
        if (round < 4) {
            return "Time: " + time + " seconds, round " + round;
        } else {
            return "Time: " + time + " seconds, Boss Defeated! ";
        }
    }

    @Override
    public int compareTo(Score t) {
        return Comparator.comparing(Score::getRound)
                //.thenComparing(Score::getDifficulty)
                .thenComparing(Score::getPoints).reversed()
                .thenComparing(Score::getTime)
                .compare(this, t);
    }

}
