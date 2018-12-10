package com.afina.emergencyshaker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");
//        Toast toast = Toast.makeText(context, "Shake", Toast.LENGTH_SHORT);
//        toast.show();

        context.startService(new Intent(context, SensorService.class));;
    }
}
