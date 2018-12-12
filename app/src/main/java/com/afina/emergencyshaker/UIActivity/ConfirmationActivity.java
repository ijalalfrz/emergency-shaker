package com.afina.emergencyshaker.UIActivity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.afina.emergencyshaker.Listeners.ShakeListener;
import com.afina.emergencyshaker.R;

import java.util.Timer;
import java.util.TimerTask;

public class ConfirmationActivity extends AppCompatActivity {
    public static boolean isActive = false;
    private TextView tvCounter;
    private boolean isShake;
    private TimerTask timerTask;
    private Timer timer;

    private long now;
    private Context ctx;

    @Override
    protected void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ctx = this;
        tvCounter = (TextView) findViewById(R.id.tv_counter);
        LocalBroadcastManager.getInstance(this).registerReceiver(countReceiver, new IntentFilter(ShakeListener.COUNTER_SHAKE));

        startTimer();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }

    private BroadcastReceiver countReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int count = intent.getIntExtra(ShakeListener.EXTRA_COUNT,0);
            tvCounter.setText(String.valueOf(count));
            if(count==15){
                String phoneNumber = "085703830280";
                Intent dialPhoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                dialPhoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    ctx.startActivity(dialPhoneIntent);
                }
            }

            now = System.currentTimeMillis();

        }
    };

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                if(System.currentTimeMillis() - now >= 3000){
                    ((Activity)ctx).finish();
                }
            }
        };
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

}
