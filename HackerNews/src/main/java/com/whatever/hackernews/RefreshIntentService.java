package com.whatever.hackernews;

import android.app.Activity;
import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.*;
import android.util.Log;
import android.widget.Toast;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

/**
 * Created by PetoU on 25/04/14.
 */
public class RefreshIntentService extends IntentService {

    Handler handler;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public RefreshIntentService(String name) {
        super(name);
        handler = new Handler();
    }

    public RefreshIntentService(){
        super("name");
        handler = new Handler();
    }




    @Override
    protected void onHandleIntent(Intent intent) {
        

        // set database helper and database
        JSONdatabaseHelper databaseHelper = JSONdatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase database = databaseHelper.getWritableDatabase();

        // helper variables for parsing JSON
        StringBuilder response = null;
        JSONObject jsonObject = null;
        JSONArray jsonArray = null;
        String jsonArrayString = null;

        // get messenger, for later messages
        Messenger messenger = intent.getParcelableExtra("messenger");
        Message msg = Message.obtain();

        //
        // Http GET request for JSON data link
        //

        try {

            URL link = new URL(Consts.MAINPAGE_LINK_JSON);

            HttpURLConnection connection = (HttpURLConnection) link.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            response = new StringBuilder();

            while((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }

            in.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {

            if (e instanceof UnknownHostException) {

                e.printStackTrace();

                // toast on UI thread
                handler.post( new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Internet connection error", Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                e.printStackTrace();
            }
        }

        // parse whole string to JSONArray and JSONObject
        // + 15 because im too lazy to count from beginning

        if (response != null) {
            jsonArrayString = response.substring(response.indexOf("\"collection1\"") + 15, response.length() - 2);
        }

        try {

            jsonArray = new JSONArray(jsonArrayString);

            //
            // iterate through json parts
            //
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = (JSONObject) jsonArray.get(i);

                String position = jsonObject.getString("position");
                int threadPosition = Integer.parseInt(position.substring(0, position.indexOf(".")));
                String points = jsonObject.getString("points");

                JSONObject jsonTitle = jsonObject.getJSONObject("title");
                String titleLink = jsonTitle.getString("href");
                String titleString = jsonTitle.getString("text");

                String commentsLink = "";
                String commentsString = "";

                try {

                    JSONObject jsonComments = jsonObject.getJSONObject("comments");
                    commentsLink = jsonComments.getString("href");
                    commentsString = jsonComments.getString("text");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //
                // update database
                //
                ContentValues values = new ContentValues();
                values.put("points", points);
                values.put("titleLink", titleLink);
                values.put("titleString", titleString);
                values.put("commentsLink", commentsLink);
                values.put("commentsString", commentsString);

                // insert data to database and return Long data to arbitrary obj in message
                msg.obj = database.insert("news", null, values);

            }

            // send message to messenger to handler in UI activity
            messenger.send(msg);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }


    }
}
