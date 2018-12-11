package com.afina.emergencyshaker.Listeners;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.afina.emergencyshaker.ShakeDetector;

public class ShakeListener implements ShakeDetector.OnShakeListener {
    private Context ctx;

    public ShakeListener(Context ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onShake(int count) {

        String phoneNumber = "085703830280";
        Intent dialPhoneIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
        dialPhoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(dialPhoneIntent);
    }
}
