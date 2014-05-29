package com.whatever.hackernews;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by PetoU on 25/04/14.
 */
public class JSONdatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "json.db";
    private static final int DATABASE_VERSION = 3;

    //singleton for one instance of database
    private static JSONdatabaseHelper Singleton;


    private JSONdatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private JSONdatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    private JSONdatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    synchronized public static JSONdatabaseHelper getInstance(final Context context) {
        if (Singleton == null) {
            Singleton = new JSONdatabaseHelper(context.getApplicationContext());
        }
        return Singleton;
    }

    synchronized public static SQLiteDatabase getDatabase(){
        return Singleton.getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE news ( " + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "position INTEGER, " +
                    "titleString TEXT, " +
                    "titleLink TEXT, " +
                    "points TEXT, " +
                    "commentsLink TEXT, " +
                    "commentsString TEXT, " +
                    "added TEXT )");

        db.execSQL("CREATE TABLE comments ( " + "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "padding INTEGER, "+
                    "commentsLink TEXT, " +
                    "comment TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS news");
        db.execSQL("DROP TABLE IF EXISTS comments");

        this.onCreate(db);
    }
}
