package com.whatever.hackernews.ObserverPatternTesting;

/**
 * Created by PetoU on 14/05/14.
 */
public interface Observer {

    public void update();

    public void setSubject(Subject sub);
}
