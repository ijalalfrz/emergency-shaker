package com.afina.emergencyshaker;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.afina.emergencyshaker.Listeners.ShakeListener;

public class MainActivity extends AppCompatActivity {

    Intent mServiceIntent;
    private SensorService mSensorService;

    Context ctx;

    public Context getCtx() {
        return ctx;
    }

    static MainActivity instance;

    public static boolean sw = false;
    Switch s;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctx = this;
        setContentView(R.layout.activity_main);
        mSensorService = new SensorService(getApplicationContext());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if (!isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();

        s = (Switch)findViewById(R.id.switchID);
        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sw = true;
                    mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
                    shakeInit();
                }else{
                    sw = false;
                    mSensorManager.unregisterListener(mShakeDetector);
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
            mSensorManager.unregisterListener(mShakeDetector);
        }

        super.onDestroy();

    }

    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;



    protected void shakeInit() {

        // ShakeDetector initialization
//        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
//        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        mShakeDetector = new ShakeDetector();

        mShakeDetector.setOnShakeListener(new ShakeListener(ctx));
    }


    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume

        if(sw == true){
            s.setChecked(true);
        }else if(sw == false){
            s.setChecked(false);
            mSensorManager.unregisterListener(mShakeDetector);
        }

    }

    @Override
    public void onRestart(){
        super.onRestart();

        if(sw == true){
            s.setChecked(true);
        }else if(sw == false){
            s.setChecked(false);
            mSensorManager.unregisterListener(mShakeDetector);
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
            mSensorManager.unregisterListener(mShakeDetector);
        }

    }
}
