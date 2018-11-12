package com.example.djd.fingertest;

import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by djd on 18-8-30.
 */

public class MyServices extends Service {

    private MyBinder myBinder = new MyBinder();
    private final static String TAG = "Test_MyServices";
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "onBind: ");
        return myBinder;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: ");
        
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        super.unbindService(conn);
        Log.i(TAG, "unbindService: ");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }

    public class MyBinder extends Binder{
        public MyServices getServicer() {
            return MyServices.this;
        }
    }
    
}
