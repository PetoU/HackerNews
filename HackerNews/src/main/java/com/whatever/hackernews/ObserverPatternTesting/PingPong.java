package com.whatever.hackernews.ObserverPatternTesting;

import android.util.Log;

/**
 * Created by PetoU on 15/05/14.
 */
public class PingPong {

    public PingPong(){
        this.runPingPong();
    }

    private void runPingPong() {

        Thread pingThread1 = new Thread(new Runnable() {

            @Override
            public void run() {

                for(int i = 0; i < 10; i++) {
                    Log.e("PING", "ping" + Integer.toString(i));
                }
            }
        });

        Thread pongThread2 = new Thread(new Runnable() {
            @Override
            public void run() {

                for(int i = 0; i < 10; i++){
                    Log.e("PONG", "pong" + Integer.toString(i));
                }
            }
        });

        pingThread1.start();
        pongThread2.start();

        try {

            pingThread1.join();
            pongThread2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
