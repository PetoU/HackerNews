package com.whatever.hackernews;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.commonsware.cwac.loaderex.acl.SQLiteCursorLoader;
import com.whatever.hackernews.Detail.DetailActivity;
import com.whatever.hackernews.login.LoginActivity;


public class MainActivity extends ActionBarActivity implements Handler.Callback, LoaderManager.LoaderCallbacks<Cursor>, SwipeRefreshLayout.OnRefreshListener {

    private static final int LOGIN_REQUEST = 10;
    protected Handler handler;
    protected Messenger messenger;
    private Long insertResult;
    private JSONdatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private LoaderManager loaderManager;
    private SimpleCursorAdapter cursorAdapter;
    private SwipeRefreshLayout swipeLayout;
    private String commentsLink;
    private int positionInList;
    private Cursor cursor;
    private String sessionIDcookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // set swipe layout
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorScheme(android.R.color.holo_orange_dark, android.R.color.holo_orange_light, android.R.color.holo_orange_dark, android.R.color.holo_orange_light);

        handler = new Handler(this);
        messenger = new Messenger(handler);
        loaderManager = getSupportLoaderManager();

        //set database
        dbHelper = JSONdatabaseHelper.getInstance(this);
        database = JSONdatabaseHelper.getDatabase();

        // set listview and cursor for database
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.listrow, null, new String[]{"titleString", "commentsString"}, new int[]{R.id.title, R.id.comments}, SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(cursorAdapter);

        // on item click setup
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                positionInList = position;
                cursor.moveToPosition(positionInList);
                commentsLink = cursor.getString(5);

                // parse comments in advance enough
                AsyncParseComments loadComments = new AsyncParseComments();
                loadComments.execute(new String[]{commentsLink});

                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("commentsLink", commentsLink);
                intent.putExtra("position_in_list", positionInList);
                startActivity(intent);
            }
        });

        //refresh on first onCreate
        onRefresh();
    }

    @Override
    //
    // message after inserting data to database
    //
    public boolean handleMessage(Message msg) {

        // stop swipeRefresh indicator
        swipeLayout.setRefreshing(false);

        insertResult = (Long) msg.obj;
//        Log.e("insertResult", Long.toString(insertResult));

        // TODO
        // query database for data
        //

        loaderManager.restartLoader(1, null, this);
        return false;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String query = " SELECT _id, position, titleString, titleLink, points, commentsLink, commentsString" +
                       " FROM " + "news;";

//        String query = "SELECT * FROM comments GROUP BY commentsLink;";

        SQLiteCursorLoader loader = new SQLiteCursorLoader(this, dbHelper, query, null);
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //
        // gets data from onCreateLoader, from loader
        //
        Log.e("cursorDump", data.toString());

        cursor = data;


        if (data != null && data.moveToPosition(positionInList)) {
            cursorAdapter.swapCursor(data);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }


    @Override
    //
    // swipe to refresh
    //
    public void onRefresh() {

        // delete old data in database
        database.execSQL("DELETE FROM news");
        database.execSQL("DELETE FROM comments");

        Toast.makeText(this,"Refreshing", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(MainActivity.this, RefreshIntentService.class);
        intent.putExtra("messenger", messenger);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_refresh){
            onRefresh();
        }

        if (item.getItemId() == R.id.login){

            // start login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        sessionIDcookie = data.getStringExtra("sessionID");
    }
}
