package com.whatever.hackernews;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import com.commonsware.cwac.loaderex.ContentChangingTask;
import com.commonsware.cwac.loaderex.SQLiteCursorLoader;

/**
 * Created by PetoU on 20/05/14.
 */
public class MyCursorLoader extends SQLiteCursorLoader {
    public MyCursorLoader(Context context, SQLiteOpenHelper db, String rawQuery, String[] args) {
        super(context, db, rawQuery, args);
    }

    @Override
    protected ContentChangingTask buildUpdateTask(SQLiteCursorLoader loader) {

        return super.buildUpdateTask(loader);
    }
}
