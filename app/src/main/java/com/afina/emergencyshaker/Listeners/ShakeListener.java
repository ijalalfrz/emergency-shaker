package com.afina.emergencyshaker.Listeners;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.Service.SensorService;
import com.afina.emergencyshaker.UIActivity.ConfirmationActivity;

import java.util.ArrayList;

public class ShakeListener implements ShakeDetector.OnShakeListener {

    public static final String COUNTER_SHAKE = "counter_shake";
    public static final String EXTRA_COUNT = "extra_count";
    private ArrayList<Target> listTarget;

    private int index = 0;
    private static Context ctx;
    private int threshold;
    public ShakeListener(Context ctx) {
        this.ctx = ctx;

        listTarget = new ArrayList<>();
        Target target = new Target();
        target.jumlah_shake = 10;
        target.nama = "Polisi";
        listTarget.add(target);

        threshold = listTarget.get(index).jumlah_shake;
    }

    @Override
    public void onShake(int count) {


        if(count > 5 && !ConfirmationActivity.isActive && SensorService.isActive){
            Intent intent = new Intent(ctx,ConfirmationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
        sendCountBroadcast(count,SensorService.isActive);
    }

    private void sendCountBroadcast(int count,boolean isActive) {
        if (count != 0 && isActive) {
            Intent intent = new Intent(COUNTER_SHAKE);
            intent.putExtra(EXTRA_COUNT, count);
            LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
        }
    }




}
