package com.afina.emergencyshaker.UIActivity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Internet.APIService;
import com.afina.emergencyshaker.Internet.APIUtils;
import com.afina.emergencyshaker.Listeners.ShakeListener;
import com.afina.emergencyshaker.Model.Response;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.Service.SensorService;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;

public class ConfirmationActivity extends AppCompatActivity implements View.OnClickListener {
    public static boolean isActive = false;
    private TextView tvCounter,tvNama,tvMenelepon,tvShake;
    private Button btnBatal;
    private boolean isShake;
    private TimerTask timerTask;
    private Timer timer;
    private ArrayList<Target> listTarget;
    private Target temp;
    private long now;
    private Context ctx;
    int index=0;
    int condon=3;
    private int shakeCount = 0;
    private CountDownTimer countDownTimer;
    private DbEmergencyShaker db;

    private boolean isNext = false;
    private boolean foundContact = false;

    private APIService mAPIService;


    @Override
    protected void onStart() {
        super.onStart();
        SensorService.isActiveConfirm = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        SensorService.isActiveConfirm = false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        SensorService.isActiveConfirm = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SensorService.isActiveConfirm = false;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        ctx = this;
        db = new DbEmergencyShaker(this);
        db.open();

        mAPIService = APIUtils.getAPIService();


        listTarget = new ArrayList<>();
        listTarget = db.getAllTargetByShake();

        tvCounter = (TextView) findViewById(R.id.tv_counter);
        tvNama = (TextView) findViewById(R.id.tv_nama);
        tvMenelepon = (TextView) findViewById(R.id.tv_menelepon);
        tvShake = (TextView) findViewById(R.id.tv_shake);
        btnBatal = (Button) findViewById(R.id.btn_batal);
        btnBatal.setOnClickListener(this);
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
            shakeCount=count;

            if(listTarget.size()>index && listTarget.get(index)!=null){
                temp = listTarget.get(index);

                if(shakeCount==temp.jumlah_shake){
                    foundContact = true;

                    btnBatal.setVisibility(View.VISIBLE);
                    tvShake.setText("MENELEPON:");
                    tvCounter.setText(temp.nama);
                    tvMenelepon.setText("DALAM");
                    countDownTimer = new CountDownTimer(5000, 1000) {

                        public void onTick(long millisUntilFinished) {
                            if(isNext){
                                tvNama.setText("");
                            }else{
                                tvNama.setText(""+condon);
                            }
                            if(shakeCount>temp.jumlah_shake){
                                SensorService.isBatal = true;
                                foundContact=false;
                                isNext = true;

                                this.cancel();
                            }
                            condon-=1;
                        }

                        public void onFinish() {
                            if(temp.yes_sms==1 && !SensorService.isBatal){
                                String pesan = temp.nama +", Saya dalam situasi emergensi segeralah minta bantuan. Lokasi saya : http://www.google.com/maps/place/"+LayoutActivity.lat+","+LayoutActivity.ln+" pesan ini dikirim melalui aplikasi Emergency Shaker.";
                                sendPost(temp.telepon,pesan);
                            }

                            String phoneNumber = temp.telepon;
                            Intent dialPhoneIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                            dialPhoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                if(!SensorService.isBatal) ctx.startActivity(dialPhoneIntent);
                                SensorService.isBatal = false;
                                finish();
                            }
                        }

                    }.start();


                }else {
                    if(isNext){
                        condon=3;
                        index++;
                        isNext = false;
                    }
                    tvNama.setText("");

                    if(index<listTarget.size()){
                        temp = listTarget.get(index);
                        tvCounter.setText(String.valueOf(shakeCount+"/"+temp.jumlah_shake));
                        tvNama.setText(temp.nama);
                        tvNama.setGravity(Gravity.CENTER);
                    }
                    foundContact = false;
                    tvShake.setText("SHAKE COUNTER");
                    tvMenelepon.setText("MENELEPON");
                    btnBatal.setVisibility(View.INVISIBLE);
                }
            }else{
                foundContact = false;
            }

            now = System.currentTimeMillis();

        }
    };

    private void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                if(!foundContact){

                    if(System.currentTimeMillis() - now >= 3000){
                        index = 0;
                        shakeCount = 0;
                        if(Build.VERSION.SDK_INT>=16 && Build.VERSION.SDK_INT<21){
                            finishAffinity();
                        } else if(Build.VERSION.SDK_INT>=21){
                            finishAndRemoveTask();
                        }

                    }
                }
            }
        };
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask'mSwitch job
        initializeTimerTask();

        //schedule the timer, to wake up every 1 second
        timer.schedule(timerTask, 1000, 1000); //
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_batal){
            countDownTimer.cancel();
            shakeCount = 0;
            index = 0;
            SensorService.isBatal = true;
            if(Build.VERSION.SDK_INT>=16 && Build.VERSION.SDK_INT<21){
                finishAffinity();
            } else if(Build.VERSION.SDK_INT>=21){
                finishAndRemoveTask();
            }


        }
    }


    private String userkey = "wbvurq";
    private String passkey = "g5o7k5hhm7";

    public void sendPost(String nohp,String pesan){
        mAPIService.sendSMS(userkey,passkey,nohp,pesan).enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                Response res = response.body();
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Log.e("Response fail", t.getMessage());
            }
        });
    }

}
