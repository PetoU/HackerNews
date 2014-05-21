package com.whatever.hackernews;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by PetoU on 12/05/14.
 */
public class AsyncParseComments extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {

        SQLiteDatabase database = JSONdatabaseHelper.getDatabase();

        Document doc = null;
        String link = params[0];
        try {
            doc = Jsoup.connect(link).get();
            Elements commentSection = doc.getElementsByClass("comment");

            for (Element commentSectionElement : commentSection ) {

                String commentText = commentSectionElement.text();

                Log.e("PKPKPKP", commentText);

                //update database
                ContentValues values = new ContentValues();
                values.put("commentsLink", link);
                values.put("comment", commentText);

                database.insert("comments", null, values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }
}
