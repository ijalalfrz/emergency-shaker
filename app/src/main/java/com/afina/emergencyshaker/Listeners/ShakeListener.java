package com.afina.emergencyshaker.Listeners;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

public class ShakeListener implements ShakeDetector.OnShakeListener {
    private Context ctx;

    public ShakeListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onShake(int count) {

        String phoneNumber = "085703830280";
        Intent dialPhoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        dialPhoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.CALL_PHONE},1);

        }else{
            ctx.startActivity(dialPhoneIntent);

        }
    }
}
