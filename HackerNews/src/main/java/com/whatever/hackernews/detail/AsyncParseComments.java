package com.whatever.hackernews.detail;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import com.whatever.hackernews.JSONdatabaseHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by PetoU on 12/05/14.
 */
public class AsyncParseComments extends AsyncTask<String, Void, String> {

    private AsyncParseListener listener;

    public AsyncParseComments(AsyncParseListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {

        SQLiteDatabase database = JSONdatabaseHelper.getDatabase();

        Document doc = null;
        String link = params[0];
//        try {
//            doc = Jsoup.connect(link).get();
//
//            Elements commentSection = doc.getElementsByClass("comment");
//
//            for (Element commentSectionElement : commentSection ) {
//
//                String commentText = commentSectionElement.text();
//
//                Log.e("PKPKPKP", commentText);
//
//                //update database
//                ContentValues values = new ContentValues();
//                values.put("commentsLink", link);
//                values.put("comment", commentText);
//
//                database.insert("comments", null, values);
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        /*
        * test parsing HTML
        * */

        try {
            doc = Jsoup.connect(link).get();

            ContentValues values = new ContentValues();


            Elements comments = doc.select("table tr table tr:has(table)");

            for (int i = 0; i < comments.size(); i++) {

                Elements commentElement = comments.get(i).getElementsByClass("comment");

                // remove "reply" from end of string
                String commentText = commentElement.get(0).text();

                if (commentText.length() > 4 &&
                        commentText.substring(commentText.length() - 5, commentText.length()).contains("reply")) {

                    StringBuilder b = new StringBuilder(commentText);
                    b.replace(commentText.lastIndexOf("reply"), commentText.lastIndexOf("reply") + 5, "");
                    commentText = b.toString();

                }

                Elements paddingElement = comments.get(i).getElementsByTag("img");

                // padding of comments is determined by transparent picture of width multiples of 40
                int padding = Integer.parseInt(paddingElement.get(0).attr("width")) / 40;

                values.put("padding", padding);
                values.put("commentsLink", link);
                values.put("comment", commentText);

                database.insert("comments", null, values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        listener.onParseCommentsComplete(s);
    }

    public interface AsyncParseListener {
        public void onParseCommentsComplete(String response);
    }
}
