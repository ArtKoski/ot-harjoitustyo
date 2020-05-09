/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applogic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author artkoski
 * Loads the configuration file for the game settings.
 */
public class loadDifficulty {

    private int difficulty;
    private ArrayList<Double> returnList;
    /**
     * Constructor defining which settings are to be loaded.
     * @param setting - 1: normal, 2: hard
     */
    public loadDifficulty(int setting) {
        this.difficulty = setting;
        returnList = new ArrayList<Double>();
    }

    /**
     * Reads the configuration file and returns a list of values relevant to the difficulty setting.
     * @return 
     */
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
            System.out.println("Problems with config..");
            ex.printStackTrace();
        }
        return returnList;
    }
}
