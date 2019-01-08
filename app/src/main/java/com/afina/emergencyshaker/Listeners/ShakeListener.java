package com.afina.emergencyshaker.Listeners;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.Service.SensorService;
import com.afina.emergencyshaker.UIActivity.ConfirmationActivity;

import java.util.ArrayList;

public class ShakeListener implements ShakeDetector.OnShakeListener {

    public static final String COUNTER_SHAKE = "counter_shake";
    public static final String EXTRA_COUNT = "extra_count";
    private static int threshold;
    private static int jumlahData  = 0;
    private static Context ctx;
    private static DbEmergencyShaker db;
    public ShakeListener(Context ctx) {
        this.ctx = ctx;
        this.db = new DbEmergencyShaker(ctx);
        db.open();
        if(db.getAllTarget().size()>0) jumlahData = db.getAllTarget().size();
        db.close();
    }

    @Override
    public void onShake(int count) {


        if(count > 5 && jumlahData>0 && !SensorService.isActiveConfirm&& SensorService.isActive){
            Intent intent = new Intent(ctx,ConfirmationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
        sendCountBroadcast(count,SensorService.isActive);
    }

    public static void updateTreshold(){
//        db.open();
//        threshold = db.getFirstTarget().jumlah_shake;
//        jumlahData = db.getAllTarget().size();
//        db.close();
    }

    private void sendCountBroadcast(int count,boolean isActive) {
        if (count != 0 && isActive) {
            Intent intent = new Intent(COUNTER_SHAKE);
            intent.putExtra(EXTRA_COUNT, count);
            LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
        }
    }




}
