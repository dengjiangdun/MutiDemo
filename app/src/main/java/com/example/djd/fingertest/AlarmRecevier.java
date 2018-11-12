package com.example.djd.fingertest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by djd on 18-11-2.
 */

public class AlarmRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"Alarm >_<",Toast.LENGTH_SHORT).show();
        Log.d("AlarmReceiver", "log log log");
    }
}
