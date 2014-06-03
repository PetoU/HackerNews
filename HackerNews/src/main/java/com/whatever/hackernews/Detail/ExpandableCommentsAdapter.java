package com.whatever.hackernews.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import com.whatever.hackernews.R;
import com.whatever.hackernews.model.Comment;

import java.util.ArrayList;

/**
 * Created by PetoU on 29/05/14.
 */
public class ExpandableCommentsAdapter extends BaseExpandableListAdapter {

    private ArrayList<ArrayList<Comment>> commentList;
    private LayoutInflater inflater;
    private final float scale;

    public ExpandableCommentsAdapter(Context context, ArrayList<ArrayList<Comment>> commentList) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.commentList = commentList;
        this.scale = context.getResources().getDisplayMetrics().density;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String groupComment = commentList.get(groupPosition).get(0).comment;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.comments_row, null);
        }

        TextView commentTextView = (TextView) convertView.findViewById(R.id.commentTextView);
        commentTextView.setText(groupComment);

        int padding = (int) (8 * scale);
        convertView.setPadding(padding, 0, 0, 0);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String childComment = commentList.get(groupPosition).get(childPosition + 1).comment;
        int indentation = commentList.get(groupPosition).get(childPosition + 1).indentation;

        if(convertView == null){
            convertView = inflater.inflate(R.layout.comments_row, null);
        }

        TextView commentTextView = (TextView) convertView.findViewById(R.id.commentTextView);
        commentTextView.setText(childComment);

//        TODO set padding
//        convertView.setPadding();

        int padding = (int) ((indentation * 20 * scale) + (8 * scale));
        convertView.setPadding(padding, 0, 0, 0);

        return convertView;
    }

//    Class is calling all methods before any commentList available, therefore null pointer exception
//
//    public void swapData(ArrayList<ArrayList<Comment>> commentList){
//        this.commentList = commentList;
//    }

    @Override
    public int getGroupCount() {
        return commentList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return commentList.get(groupPosition).size() - 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return commentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return commentList.get(groupPosition).get(childPosition + 1);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
