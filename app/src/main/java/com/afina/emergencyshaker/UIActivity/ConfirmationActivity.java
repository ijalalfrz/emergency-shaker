package com.afina.emergencyshaker.UIActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.afina.emergencyshaker.Listeners.ShakeListener;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.Service.SensorService;

public class ConfirmationActivity extends AppCompatActivity {

    TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        tvCounter = (TextView) findViewById(R.id.tv_counter);
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter(ShakeListener.COUNTER_SHAKE));


    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int count = intent.getIntExtra(ShakeListener.EXTRA_COUNT,0);
            tvCounter.setText(String.valueOf(count));
        }
    };
}
