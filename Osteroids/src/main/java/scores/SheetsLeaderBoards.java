/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scores;

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
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author artkoski
 */
public class SheetsLeaderBoards {

    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "dunno";
    private static String SPREADSHEET_ID;
    private static String CREDENTIALS_FILE_PATH;
    private static String EZ_RANGE;
    private static String HARD_RANGE;
    private static PriorityQueue<Score> topTenEz;
    private static PriorityQueue<Score> topTenHard;
    
    
    
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

    public static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        Credential credential = authorize();
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(),
                JacksonFactory.getDefaultInstance(), credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void init() {
        try (InputStream input = new FileInputStream("config.properties")) {

            Properties prop = new Properties();
            prop.load(input);

            SPREADSHEET_ID = (prop.getProperty("SPREADSHEET_ID"));
            EZ_RANGE = (prop.getProperty("EZ_RANGE"));
            HARD_RANGE = (prop.getProperty("HARD_RANGE"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, GeneralSecurityException {

    }

    public void leaderboardUpdate(String nimi, Score score) throws IOException, GeneralSecurityException, ParseException {
        if (score.difficulty == 1) {
            search(1);
            topTenEz.add(new Score(nimi, score.time, score.round, score.points));
            for (int i = 0; i < 10; i++) {
                if (topTenEz.isEmpty()) {
                    return;
                }
                Score pisteRivi = topTenEz.poll();
                update(pisteRivi.nimi, pisteRivi, "B" + (i + 2));
            }
        } else {
            search(2);
            topTenHard.add(new Score(nimi, score.time, score.round, score.points));
            for (int i = 0; i < 10; i++) {
                if (topTenHard.isEmpty()) {
                    return;
                }
                Score pisteRivi = topTenHard.poll();
                update(pisteRivi.nimi, pisteRivi, "G" + (i + 2));
            }
        }

    }

    public static void add(String nimi, Score score) throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();

        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList(nimi, score.round, score.time)
                ));

        AppendValuesResponse appendResult = sheetsService.spreadsheets().values()
                .append(SPREADSHEET_ID, "B2", body)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(Boolean.TRUE)
                .execute();

    }

    public static void update(String nimi, Score score, String paivitettavaKohta) throws IOException, GeneralSecurityException {
        sheetsService = getSheetsService();

        ValueRange body = new ValueRange()
                .setValues(Arrays.asList(
                        Arrays.asList(nimi, score.time, score.round, score.points)
                ));

        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, paivitettavaKohta, body)
                .setValueInputOption("RAW")
                .execute();

    }

    public PriorityQueue<Score> search(int difficulty) throws IOException, GeneralSecurityException, ParseException {
        String range = "B2:E5";
        sheetsService = getSheetsService();
        PriorityQueue<Score> top5 = new PriorityQueue<>();
        if (difficulty == 1) {
            topTenEz = new PriorityQueue<>();
            range = EZ_RANGE;
            top5 = topTenEz;
        } else {
            topTenHard = new PriorityQueue<>();
            range = HARD_RANGE;
            top5 = topTenHard;
        }
        if (top5 == null) {
            System.out.println("Error fetching scores..");
            throw new NullPointerException();
        }

        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, range)
                .execute();
        List<List<Object>> values = response.getValues();

        if (values == null || values.isEmpty()) {
            System.out.println("No saved scores!");
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
