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
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.FileInputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

/**
 *
 * @author artkoski
 */
public class SheetsLeaderBoards {

    private static Sheets sheetsService;
    private static String APPLICATION_NAME = "dunno";
    private static String SPREADSHEET_ID = "1qHyr-Vnvm3_V0qbh2DLfD5lwYXCld1mMnWPYDPH47Ro";
    private static String CREDENTIALS_FILE_PATH = "/home/artkoski/ot-harjoitustyo/Osteroids/src/main/java/resources/credentials.json";
    private static PriorityQueue<Score> topTen;

    private static Credential authorize() throws IOException, GeneralSecurityException {
        //InputStream in = SheetsLeaderBoards.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH);
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
                .setAccessType("offline")
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

    public static void main(String[] args) throws IOException, GeneralSecurityException {

    }

    public void leaderboardUpdate(String nimi, Score score) throws IOException, GeneralSecurityException, ParseException {
        search();
        topTen.add(new Score(nimi, score.time, score.round));
        for (int i = 0; i < 10; i++) {
            if (topTen.isEmpty()) {
                return;
            }
            Score pisteRivi = topTen.poll();
            update(pisteRivi.nimi, pisteRivi, "B" + (i + 2));
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
                        Arrays.asList(nimi, score.time, score.round)
                ));

        UpdateValuesResponse result = sheetsService.spreadsheets().values()
                .update(SPREADSHEET_ID, paivitettavaKohta, body)
                .setValueInputOption("RAW")
                .execute();

    }

    public PriorityQueue<Score> search() throws IOException, GeneralSecurityException, ParseException {
        topTen = new PriorityQueue<>();
        sheetsService = getSheetsService();
        String range = "B2:D5";  //More?
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

                double val = Double.parseDouble(b.replaceAll(",", ".")); //slightly scuffed

                topTen.add(new Score((String) a, val, Integer.valueOf(c)));
            }
        }
        return topTen;
    }

}
