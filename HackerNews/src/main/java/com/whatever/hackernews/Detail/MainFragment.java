package com.whatever.hackernews.Detail;

import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.LoaderManager;
import android.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.whatever.hackernews.AsyncParseComments;
import com.whatever.hackernews.JSONdatabaseHelper;
import com.whatever.hackernews.R;


/**
 * Created by PetoU on 30/04/14.
 */
public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private int slidePosition;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private SQLiteDatabase database;
    private JSONdatabaseHelper dbHelper;
    private Cursor newsTableCursor;
    private int section;
//    private SimpleCursorAdapter commentsCursorAdapter;
    private String titleString = null;
    private String titleLink = null;
    private TextView titleText;
    private TextView linkText;
    private WebView webView;
    private String commentsLink;
    private int positionInList;


    public static Fragment newInstance(int sectionNumber, String commentsLink, int positionInList) {

        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt("position_in_list", positionInList);
        args.putString("commentsLink", commentsLink);
        fragment.setArguments(args);

        return fragment;
    }

    public MainFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;
        Bundle args = getArguments();
        positionInList = args.getInt("position_in_list");
        commentsLink = args.getString("commentsLink");

        // database setup
        dbHelper = JSONdatabaseHelper.getInstance(getActivity());
        database = JSONdatabaseHelper.getDatabase();

        //adapter for comments session
//        commentsCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.comment_row, null, new String[]{"comment"}, new int[]{R.id.comment}, SimpleCursorAdapter.IGNORE_ITEM_VIEW_TYPE);

        if (args != null) {
            section = args.getInt(ARG_SECTION_NUMBER);

            //start loader
            getLoaderManager().restartLoader(section, null, this);

            rootView = inflater.inflate(R.layout.fragment_image1, container, false);

            titleText = (TextView) rootView.findViewById(R.id.title);

            linkText = (TextView) rootView.findViewById(R.id.link);

            webView = (WebView) rootView.findViewById(R.id.webView);
            webView.getSettings().setJavaScriptEnabled(true);


        }

        return rootView;
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String query = " SELECT _id, position, titleString, titleLink, points, commentsLink, commentsString" +
                " FROM " + "news;";

        return new SQLiteCursorLoader(getActivity(), dbHelper, query, null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

            newsTableCursor = data;

            if (newsTableCursor != null && newsTableCursor.moveToPosition(positionInList)) {

                titleString = newsTableCursor.getString(2);
                titleLink = newsTableCursor.getString(3);

                titleText.setText(titleString);
                linkText.setText(titleLink);
                webView.loadUrl(titleLink);

            } else {
                Log.e("PK", "newsTableCursor null");
            }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
//            commentsCursorAdapter.swapCursor(null);
    }


}
