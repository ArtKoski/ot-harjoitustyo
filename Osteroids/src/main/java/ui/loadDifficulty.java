/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author artkoski
 */
public class loadDifficulty {

    int difficulty;

    ArrayList<Double> returnList;

    public loadDifficulty(int setting) {
        this.difficulty = setting;
        returnList = new ArrayList<Double>();
    }

    public ArrayList<Double> difficultyConfig() {
        try (InputStream input = new FileInputStream("config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            if (difficulty == 1) {
                returnList.add(Double.valueOf(prop.getProperty("patrolSpeed")));
                returnList.add(Double.valueOf(prop.getProperty("bossAmmoFrequency")));
                returnList.add(Double.valueOf(prop.getProperty("enemyAmmoFrequency")));
                returnList.add(Double.valueOf(prop.getProperty("extraAcceleration")));
                returnList.add(Double.valueOf(prop.getProperty("hpMultiplier")));
            } else {
                returnList.add(Double.valueOf(prop.getProperty("patrolSpeedH")));
                returnList.add(Double.valueOf(prop.getProperty("bossAmmoFrequencyH")));
                returnList.add(Double.valueOf(prop.getProperty("enemyAmmoFrequencyH")));
                returnList.add(Double.valueOf(prop.getProperty("extraAccelerationH")));
                returnList.add(Double.valueOf(prop.getProperty("hpMultiplierH")));
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return returnList;
    }
}
