package com.byacht.overlook.douyutv;

import android.os.Handler;

/**
 * Created by dn on 2017/8/23.
 */

public class TimeThread extends Thread {
    private long duration;
    private long startTime;
    private Handler mHandler;

    public TimeThread(long duration, Handler handler) {
        this.duration = duration;
        mHandler = handler;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    @Override
    public void run() {
        startTime = System.currentTimeMillis();
        while (true) {
            if (startTime + duration < System.currentTimeMillis()) {
                mHandler.sendEmptyMessage(0);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
