/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scores;

import java.util.Comparator;
import ui.GUI;

/**
 *
 * @author artkoski
 */
public class Score implements Comparable<Score> {

    String nimi;
    double time;
    int round;
    int points;
    int difficulty;
    
    public Score(double time, int round, int points, int difficulty) {
        this.time = time;
        this.round = round;
        this.points = points;
        this.difficulty=difficulty;
    }

    public Score(String nimi, double time, int round, int points) {
        this.nimi = nimi;
        this.time = time;
        this.round = round;
        this.points = points;
    }

    public double getTime() {
        return time;
    }
    public double getDifficulty() {
        return difficulty;
    }

    public Integer getRound() {
        return round;
    }
    public Integer getPoints() {
        return round;
    }

    @Override
    public String toString() {
        if (round < 4) {
            return nimi + ", Time: " + time + " seconds, round " + round;
        } else {
            return nimi + ", Time: " + time + " seconds, Boss Defeated! ";
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
        return Comparator.comparing(Score::getRound).reversed()
                .thenComparing(Score::getDifficulty)
                .thenComparing(Score::getPoints)
                .thenComparing(Score::getTime)
                .compare(this, t);
    }

}
