/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applogic;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import static com.google.api.client.json.webtoken.JsonWebSignature.parser;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Properties;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

/**
 *
 * @author artkoski Class responsible for the leaderboard functionality.
 *
 */
public class SheetsLeaderBoards {

    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "dunno";
    private static String SPREADSHEET_ID;
    private static String CREDENTIALS_FILE_PATH;
    private static String EZ_RANGE;
    private static String HARD_RANGE;
    private static String TEST_RANGE;
    private static PriorityQueue<Score> topTenEz;
    private static PriorityQueue<Score> topTenHard;
    private static PriorityQueue<Score> topTenTest;

    private static Credential authorize() throws IOException, GeneralSecurityException {

        InputStream in = new FileInputStream("credentials.json");
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
                JacksonFactory.getDefaultInstance(), new InputStreamReader(in)
        );
        List<String> scopes = Arrays.asList(SheetsScopes.SPREADSHEETS);

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), JacksonFactory.getDefaultInstance(),
                clientSecrets, scopes)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens")))
                .setAccessType("online")
                .build();

        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver())
                .authorize("user");
        return credential;
    }

    private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    /**
     * Class for loading essential values from config.properties to variables.
     * Needs to be executed before attempting to read or write from database.
     */
    public void init() {
        try (InputStream input = new FileInputStream("config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            SPREADSHEET_ID = (prop.getProperty("SPREADSHEET_ID"));
            EZ_RANGE = (prop.getProperty("EZ_RANGE"));
            HARD_RANGE = (prop.getProperty("HARD_RANGE"));
            TEST_RANGE = (prop.getProperty("TEST_RANGE"));

        } catch (IOException ex) {
            System.out.println("Error loading config..");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {

    }

    /**
     * Searches the leaderboards for old scores and adds them into a
     * PriorityList, then adds the given Score in the parameters in the list as
     * well and updates the leaderboards. Two different tables exist for each
     * difficulty.
     *
     * @param name - Nickname given by the player
     * @param score - The score to be added to the leaderboards
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws ParseException
     */
    public void leaderboardUpdate(String name, Score score) throws IOException, GeneralSecurityException, ParseException {
        if (score.getDifficulty() == 1) {
            search(1);
            topTenEz.add(new Score(name, score.getTime(), score.getRound(), score.getPoints()));
            for (int i = 0; i < 10; i++) {
                if (topTenEz.isEmpty()) {
                    return;
                }
                Score pisteRivi = topTenEz.poll();
                update(pisteRivi.getName(), pisteRivi, "B" + (i + 2));
            }
        } else if (score.getDifficulty() == 2) {
            search(2);
            topTenHard.add(new Score(name, score.getTime(), score.getRound(), score.getPoints()));
            for (int i = 0; i < 10; i++) {
                if (topTenHard.isEmpty()) {
                    return;
                }
                Score pisteRivi = topTenHard.poll();
                update(pisteRivi.getName(), pisteRivi, "G" + (i + 2));
            }
        } else {
            search(5);
            topTenTest.add(new Score(name, score.getTime(), score.getRound(), score.getPoints()));
            for (int i = 0; i < 10; i++) {
                if (topTenTest.isEmpty()) {
                    return;
                }
                Score pisteRivi = topTenTest.poll();
                update(pisteRivi.getName(), pisteRivi, "M" + (i + 2));
            }
        }

    }

    /**
     * Update a specific row on the leaderboards
     *
     * @param name
     * @param score
     * @param paivitettavaKohta
     * @throws IOException
     * @throws GeneralSecurityException
     */
    public static void update(String name, Score score, String paivitettavaKohta) throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();

        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList(name, score.getTime(), score.getRound(), score.getPoints())
                ));

        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, paivitettavaKohta, body)
                .setValueInputOption("RAW")
                .execute();

    }

    /**
     * Searches a range on the spreadsheet relevant to the difficulty parameter.
     *
     * @param difficulty - 1: normal, 2: hard, 3: Test
     * @return - Returns a PriorityQueue with values from the specified range
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws ParseException
     */
    public PriorityQueue<Score> search(int difficulty) throws IOException, GeneralSecurityException, ParseException {
        String range = "B2:E5";
        sheetsService = getSheetsService();
        PriorityQueue<Score> top5 = new PriorityQueue<>();
        if (difficulty == 1) {
            topTenEz = new PriorityQueue<>();
            range = EZ_RANGE;
            top5 = topTenEz;
        } else if (difficulty == 2) {
            topTenHard = new PriorityQueue<>();
            range = HARD_RANGE;
            top5 = topTenHard;
        } else {
            topTenTest = new PriorityQueue<>();
            range = TEST_RANGE;
            top5 = topTenTest;
        }
        if (top5 == null) {
            System.out.println("Error fetching scores..");
        }

        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = response.getValues();

        if (values == null || values.isEmpty()) {
            return top5;
        } else {
            for (List row : values) {
                String a = (String) row.get(0); //Nimi
                String b = (String) row.get(1); //Aika
                String c = (String) row.get(2); //Kierros
                String d = (String) row.get(3); //Pisteet

                double val = Double.parseDouble(b.replaceAll(",", ".")); //slightly scuffed

                top5.add(new Score((String) a, val, Integer.valueOf(c), Integer.valueOf(d)));
            }
        }

        return top5;
    }
    /**
     * Searches the spreadsheet for both normal and hard difficulty data and builds a JavaFX component
     * out of it. 
     * @return - VBox JavaFX component
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws ParseException 
     */
    public VBox craftLeaderboard() throws IOException, GeneralSecurityException, ParseException {
        //LEADERBOARD TABLE
        VBox menuScores = new VBox();
        Label normalLabel = new Label("LEADERBOARD (NORMAL)");
        menuScores.getChildren().add(normalLabel);

        PriorityQueue<Score> tmpr = search(1);
        int ranking = 1;
        if (tmpr.isEmpty()) {
            menuScores.getChildren().add(new Text("No saved scores!"));
        } else {
            while (!tmpr.isEmpty()) {
                menuScores.getChildren().add(new Text(ranking + ". " + tmpr.poll().toString()));
                ranking++;
            }
        }
        Label hardLabel = new Label("LEADERBOARD (HARD)");
        menuScores.getChildren().add(hardLabel);

        tmpr = search(2);
        if (tmpr.isEmpty()) {
            menuScores.getChildren().add(new Text("No saved scores!"));
        } else {
            ranking = 1;
            while (!tmpr.isEmpty()) {
                menuScores.getChildren().add(new Text(ranking + ". " + tmpr.poll().toString()));
                ranking++;
            }
        }
        Label info = new Label("Two scores on the same round are ranked by enemies killed that round");
        info.setOpacity(0.8);
        menuScores.getChildren().add(info);
        menuScores.setAlignment(Pos.CENTER_RIGHT);
        return menuScores;

    }

}
