package com.whatever.hackernews.ObserverPatternTesting;

/**
 * Created by PetoU on 14/05/14.
 */
public interface Subject {

    public void register(Observer obj);
    public void unregister(Observer obj);


    public void notifyObservers();

    public Object getUpdate(Observer obj);
}
