package com.afina.emergencyshaker.UIActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.afina.emergencyshaker.Listeners.ShakeDetector;
import com.afina.emergencyshaker.Listeners.ShakeListener;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.Service.SensorService;

public class MainActivity extends AppCompatActivity {

    private Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    public static MainActivity instance;
    public static boolean sw = false;

    Switch s;
    public Context getCtx() {
        return ctx;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON|
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_main);



        mSensorService = new SensorService(getApplicationContext());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);

        }




        s = (Switch)findViewById(R.id.switchID);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sw = true;
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        startService(mServiceIntent);
                    }

                }else{
                    sw = false;
                }
            }
        });

        if(sw == true){
            s.setChecked(true);
        }else if(sw == false){
            s.setChecked(false);
        }



        instance = this;
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }


    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");

        if(sw == true){
            s.setChecked(true);
        }else if(sw == false){
            s.setChecked(false);
        }

        super.onDestroy();

    }




    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume

        if(sw == true){
            s.setChecked(true);
        }else if(sw == false){
            s.setChecked(false);
        }

    }

    @Override
    public void onRestart(){
        super.onRestart();

        if(sw == true){
            s.setChecked(true);
        }else if(sw == false){
            s.setChecked(false);
        }

    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
//        mSensorManager.unregisterListener(mShakeDetector);

        super.onPause();
        if(sw == true){
            s.setChecked(true);
        }else if(sw == false){
            s.setChecked(false);
        }

    }
}
