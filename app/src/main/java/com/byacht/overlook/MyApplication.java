package com.byacht.overlook;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

/**
 * Created by dn on 2017/9/21.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        boolean nightMode = false;
        AppCompatDelegate.setDefaultNightMode(nightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO);
    }

}
