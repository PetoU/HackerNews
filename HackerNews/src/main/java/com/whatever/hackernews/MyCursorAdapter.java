package com.whatever.hackernews;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by PetoU on 11/06/14.
 */
public class MyCursorAdapter extends CursorAdapter {

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.main_forum_listrow, parent, false);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(cursor.getString(cursor.getColumnIndex("titleString")));

        TextView comments = (TextView) view.findViewById(R.id.comments);
        comments.setText(cursor.getString(cursor.getColumnIndex("commentsString")));

        TextView upVotes = (TextView) view.findViewById(R.id.points);
        upVotes.setText(cursor.getString(cursor.getColumnIndex("points")).replace(" points", ""));

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        String url = cursor.getString(cursor.getColumnIndex("titleLink"));

        new AsyncImageParser(url, imageView).execute();

    }
}
