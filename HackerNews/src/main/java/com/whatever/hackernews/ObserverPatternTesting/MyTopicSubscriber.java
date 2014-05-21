package com.whatever.hackernews.ObserverPatternTesting;

import android.util.Log;

/**
 * Created by PetoU on 14/05/14.
 */
public class MyTopicSubscriber implements Observer {

    private String name;
    private Subject topic;

    public MyTopicSubscriber(String nm) {
        this.name = nm;
    }



    @Override
    public void update() {
        String msg = (String) topic.getUpdate(this);
        if (msg == null) {
            Log.e("pk", "no new messages");
        } else {
            Log.e("pk", msg);
        }

    }

    @Override
    public void setSubject(Subject sub) {
            this.topic = sub;
    }
}
