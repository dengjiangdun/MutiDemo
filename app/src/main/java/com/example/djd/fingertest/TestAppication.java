package com.example.djd.fingertest;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by djd on 18-11-6.
 */

public class TestAppication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        /*StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyLog().build());*/
    }
}
