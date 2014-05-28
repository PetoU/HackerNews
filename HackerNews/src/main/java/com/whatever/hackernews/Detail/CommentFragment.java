package com.whatever.hackernews.Detail;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.whatever.hackernews.JSONdatabaseHelper;
import com.whatever.hackernews.R;

/**
 * Created by PetoU on 19/05/14.
 */
public class CommentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AsyncParseComments.AsyncParseListener {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private String commentsLink;
    private int positionInList;
    private JSONdatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private SimpleCursorAdapter commentsCursorAdapter;
    private TextView titleText;
    private TextView linkText;
    private ListView listView;
    private Bundle args;

    public static Fragment newInstance(int sectionNumber, String commentsLink, int positionInList) {

        CommentFragment commentFragment = new CommentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        args.putInt("position_in_list", positionInList);
        args.putString("commentsLink", commentsLink);

        commentFragment.setArguments(args);

        return commentFragment;
    }

    public CommentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = null;
        args = getArguments();
        positionInList = args.getInt("position_in_list");
        commentsLink = args.getString("commentsLink");

        // parse comments in advance enough
        AsyncParseComments loadComments = new AsyncParseComments(this);
        loadComments.execute(new String[]{commentsLink});

        // database setup
        dbHelper = JSONdatabaseHelper.getInstance(getActivity());
        database = JSONdatabaseHelper.getDatabase();

        //adapter for comments session
        commentsCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.comment_row, null, new String[]{"comment"}, new int[]{R.id.comment}, 0);
        if (args != null) {

            rootView = inflater.inflate(R.layout.fragment_image2, container, false);

            titleText = (TextView) rootView.findViewById(R.id.title);

            linkText = (TextView) rootView.findViewById(R.id.link);

            listView = (ListView) rootView.findViewById(R.id.listView);
            listView.setAdapter(commentsCursorAdapter);
            Log.e("listview count", Integer.toString(listView.getCount()));

        }


        return rootView;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String testQuery = "SELECT * FROM comments GROUP BY commentsLink;";

        String query = " SELECT _id, commentsLink, comment" +
                " FROM " + "comments" +
                " WHERE commentsLink='" + commentsLink + "';";

        return new SQLiteCursorLoader(getActivity(), dbHelper, query, null);
    }


    @Override
    public void onLoadFinished(Loader loader, Cursor data) {


        if (data != null && data.moveToPosition(positionInList)) {
            commentsCursorAdapter.swapCursor(data);
            Log.e("countData", Integer.toString(data.getCount()));
            commentsCursorAdapter.notifyDataSetChanged();

//            titleText.setText(data.getString(2));
//            linkText.setText(data.getString(3));

        }

    }

    @Override
    public void onLoaderReset(Loader loader) {
        commentsCursorAdapter.swapCursor(null);
        commentsCursorAdapter.notifyDataSetChanged();
    }

    @Override
    public void onParseCommentsComplete(String resposnse) {

        //restart Loaders
        getLoaderManager().restartLoader(0, args, this);
    }
}
