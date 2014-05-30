package com.whatever.hackernews.Detail;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import com.commonsware.cwac.loaderex.SQLiteCursorLoader;
import com.whatever.hackernews.JSONdatabaseHelper;
import com.whatever.hackernews.R;
import com.whatever.hackernews.model.IndentComment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PetoU on 19/05/14.
 */
public class CommentFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, AsyncParseComments.AsyncParseListener {


    private static final String ARG_SECTION_NUMBER = "section_number";
    private String commentsLink;
    private int positionInList;
    private JSONdatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private ExpandableListView expListView;
    private Bundle args;
    private ArrayList<ArrayList<IndentComment>> commentList;
    private ExpandableCommentsAdapter expCommentsAdapter;

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
//        commentsCursorAdapter = new SimpleCursorAdapter(getActivity(), R.layout.comments_group_row, null, new String[]{"comment"}, new int[]{R.id.comment}, 0);

        /*
        *
        *
        * expandable listview setup
        * */

        if (args != null) {

            rootView = inflater.inflate(R.layout.detail_comments_fragment, container, false);

            expListView = (ExpandableListView) rootView.findViewById(R.id.explistView);
            expListView.setAdapter(expCommentsAdapter);

        }

        return rootView;
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {

        String query = " SELECT _id, padding, commentsLink, comment" +
                " FROM " + "comments" +
                " WHERE commentsLink='" + commentsLink + "';";

        return new SQLiteCursorLoader(getActivity(), dbHelper, query, null);
    }


    @Override
    public void onLoadFinished(Loader loader, Cursor data) {

        commentList = new ArrayList<>();

        ArrayList<IndentComment> childList = null;

        while (data != null && data.moveToNext()) {

            if (data.getInt(data.getColumnIndex("padding")) == 0) {

                if(childList != null) {
                    commentList.add(childList);
                }
                childList = new ArrayList<>(5);
                childList.add(new IndentComment(data.getInt(data.getColumnIndex("padding")), data.getString(data.getColumnIndex("comment"))));

            }
            else{
                childList.add(new IndentComment(data.getInt(data.getColumnIndex("padding")), data.getString(data.getColumnIndex("comment"))));
            }

        }

        expCommentsAdapter = new ExpandableCommentsAdapter(getActivity(), commentList);
        expListView.setAdapter(expCommentsAdapter);

//        expCommentsAdapter.swapData(commentList);

//        if (data != null && data.moveToPosition(positionInList)) {
//            commentsCursorAdapter.swapCursor(data);
//            Log.e("countData", Integer.toString(data.getCount()));
//            commentsCursorAdapter.notifyDataSetChanged();
//        }

    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    @Override
    public void onParseCommentsComplete(String resposnse) {

        //restart Loaders
        getLoaderManager().restartLoader(0, args, this);
    }
}
