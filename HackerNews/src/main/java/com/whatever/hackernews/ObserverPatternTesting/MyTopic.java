package com.whatever.hackernews.ObserverPatternTesting;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PetoU on 14/05/14.
 */
public class MyTopic implements Subject {


    private List<Observer> observerList;
    private String message;
    private boolean changed;
    private final Object MUTEX = new Object();

    public MyTopic() {
        this.observerList = new ArrayList<>();
    }

    @Override
    public void register(Observer obj) {

        if (obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
            if (!observerList.contains(obj)) {
                observerList.add(obj);
            }
        }
    }

    @Override
    public void unregister(Observer obj) {
        synchronized (MUTEX) {
            observerList.remove(obj);
        }
    }

    @Override
    public void notifyObservers() {
        List<Observer> observersLocal = null;

        synchronized (MUTEX) {
            if (!changed) return;

            observersLocal = new ArrayList<>(this.observerList);
            this.changed = false;

        }

        for (Observer obj : observersLocal) {
            obj.update();
        }
    }

    @Override
    public Object getUpdate(Observer obj) {

        return this.message;
    }


    public void postMessage(String msg) {
        this.message = msg;
        this.changed = true;
        notifyObservers();
    }



}