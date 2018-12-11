package com.afina.emergencyshaker.Listeners;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.afina.emergencyshaker.UIActivity.ConfirmationActivity;

public class ShakeListener implements ShakeDetector.OnShakeListener {

    public static final String COUNTER_SHAKE = "counter_shake";
    public static final String EXTRA_COUNT = "extra_count";

    private Context ctx;
    private int threshold = 10;
    public ShakeListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onShake(int count) {
        sendCountBroadcast(count);
        if(count == threshold ){
            Intent intent = new Intent(ctx,ConfirmationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);


        }

//
//        String phoneNumber = "085703830280";
//        Intent dialPhoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
//        dialPhoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.CALL_PHONE},1);
//
//        }else{
//            ctx.startActivity(dialPhoneIntent);
//        }
    }

    private void sendCountBroadcast(int count) {
        if (count != 0) {
            Intent intent = new Intent(COUNTER_SHAKE);
            intent.putExtra(EXTRA_COUNT, count);
            //ctx.sendBroadcast(intent);
            LocalBroadcastManager.getInstance(ctx).sendBroadcast(intent);
        }
    }
}
