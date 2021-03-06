package com.afina.emergencyshaker.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ServiceInfo;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Listeners.ShakeDetector;
import com.afina.emergencyshaker.Listeners.ShakeListener;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.UIActivity.MainActivity;
import com.afina.emergencyshaker.Broadcaster.SensorRestarterBroadcastReceiver;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class SensorService extends Service {
    public static boolean isActiveConfirm = false;
    public static boolean isActive=true;
    public static boolean isBatal = false;

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private DbEmergencyShaker db;
    MainActivity activity = MainActivity.instance;
    public Context ctx;
    public int counter=0;
    public SensorService(Context applicationContext) {
        super();

        ctx = applicationContext;

        Log.i("HERE", "here I am!");
    }

    public SensorService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();

        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
        ShakeListener shakeListener = new ShakeListener( this);
        mShakeDetector.setOnShakeListener(shakeListener);


        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("EXIT", "ondestroy!");
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
        if(isActive){
            sendBroadcast(broadcastIntent);


            Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
            restartServiceIntent.setPackage(getPackageName());
            PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent,PendingIntent.FLAG_ONE_SHOT);
            AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            alarmService.set(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime() + 1000,restartServicePendingIntent);
        }else{
            mSensorManager.unregisterListener(mShakeDetector);
        }
        stoptimertask();
        super.onDestroy();


    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("TASKREMOVED", "TRUE!");
        Log.e("FLAGX : ", ServiceInfo.FLAG_STOP_WITH_TASK + "");
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent,PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(AlarmManager.ELAPSED_REALTIME,SystemClock.elapsedRealtime() + 1000,restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);

    }

    private Timer timer;
    private TimerTask timerTask;
    long oldTime=0;
    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    /**
     * it sets the timer to print the counter every x seconds
     */
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                Log.i("in timer", "in timer ++++  "+ (counter++));
            }
        };
    }

    /**
     * not needed
     */
    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
