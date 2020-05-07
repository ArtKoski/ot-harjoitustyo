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
    boolean dead;
    
    public Score(double time, int round) {
        this.time = time;
        this.round = round;
    }

    public Score(String nimi, double time, int round) {
        this.nimi = nimi;
        this.time = time;
        this.round = round;
    }

    public double getTime() {
        return time;
    }

    public Integer getRound() {
        return round;
    }
    public boolean getDeadorAlive() {
        return dead;
    }
    public void setDeadOrAlive(boolean b) {
        this.dead=b;
    }

    @Override
    public String toString() {
        if (round < 4) {
            return nimi + ", Time: " + time + " seconds, round " + round;
        } else {
            return nimi + ", Time: " + time + " seconds, Boss Defeated! " + round;
        }
    }

    @Override
    public int compareTo(Score t) {
        return Comparator.comparing(Score::getRound).reversed()
                .thenComparing(Score::getTime)
                .compare(this, t);
    }

}
