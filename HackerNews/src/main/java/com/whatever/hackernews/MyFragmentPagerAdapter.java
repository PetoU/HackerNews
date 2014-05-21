package com.whatever.hackernews;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;
import com.whatever.hackernews.Detail.CommentFragment;
import com.whatever.hackernews.Detail.MainFragment;


import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * Created by PetoU on 27/03/14.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    private final WeakReference<Context> m_Context;
    private final int positionInList;
    private String commentsLink;

    public MyFragmentPagerAdapter(FragmentManager fm, Context context, String commentsLink, int positionInList) {
        super(fm);
        this.commentsLink = commentsLink;
        m_Context = new WeakReference<Context>(context);

        this.positionInList = positionInList;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        if (position == 0) {
            return MainFragment.newInstance(position + 1, commentsLink, positionInList);
        } else if (position == 1) {
            return CommentFragment.newInstance(position + 2, commentsLink, positionInList);
        }

        return null;
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();

        final Context c = m_Context.get();
        if (c == null) {
            return "";
        }

        switch (position) {
            case 0:
                return c.getResources().getString(R.string.main).toUpperCase(l);

            case 1:
               return c.getResources().getString(R.string.comments).toUpperCase(l);
        }

        return "";
    }
}