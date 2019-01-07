package com.afina.emergencyshaker.UIActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.Service.SensorService;
import com.afina.emergencyshaker.UIFragment.HomeFragment;
import com.afina.emergencyshaker.UIFragment.PengaturanFragment;
import com.afina.emergencyshaker.UIFragment.TentangFragment;

public class LayoutActivity extends AppCompatActivity {
    private BottomNavigationView navBottom;
    private HomeFragment homeFragment;
    private TentangFragment tentangFragment;
    private PengaturanFragment pengaturanFragment;
    private int history = R.id.nav_home;


    private Intent mServiceIntent;
    private SensorService mSensorService;
    private Context ctx;


    private DbEmergencyShaker dbEmergencyShaker;
    private DbEmergencyShaker.Status stat;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        initUI();


        dbEmergencyShaker = new DbEmergencyShaker(getApplicationContext());
        dbEmergencyShaker.open();
        ctx = this;

        mSensorService = new SensorService(getApplicationContext());
        mServiceIntent = new Intent(ctx, mSensorService.getClass());

        checkService();

        //CALL PHONE PERMISSION
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);
        }

    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        dbEmergencyShaker.close();
        super.onDestroy();

    }

    public void initUI(){
        homeFragment = new HomeFragment();
        tentangFragment = new TentangFragment();
        pengaturanFragment = new PengaturanFragment();

        setFragment(homeFragment);

        navBottom = (BottomNavigationView) findViewById(R.id.nav_bottom);

        navBottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_home:
                        history = R.id.nav_home;
                        setFragment(homeFragment);
                        break;
                    case R.id.nav_pengaturan:
                        history = R.id.nav_pengaturan;
                        setFragment(pengaturanFragment);
                        break;
                    case R.id.nav_tentang:
                        history = R.id.nav_tentang;
                        setFragment(tentangFragment);
                        break;

                    case R.id.nav_keluar:
                        final AlertDialog alertDialog = new AlertDialog.Builder(LayoutActivity.this).create();
                        alertDialog.setTitle("Perhatian");
                        alertDialog.setMessage("Apakah anda ingin keluar dari aplikasi?");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "TIDAK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        alertDialog.dismiss();
                                        navBottom.setSelectedItemId(history);
                                    }
                                });
                        alertDialog.setOnShowListener( new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface arg0) {
                                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#627894"));
                                alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#627894"));

                            }
                        });

                        alertDialog.setCancelable(false);
                        alertDialog.show();
                        break;
                }

                return true;
            }
        });

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

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }

    public void checkService(){
        stat = dbEmergencyShaker.getLastStatus();
        if(stat != null){
            if(stat.status == 1){
                if(!isMyServiceRunning(SensorService.class)){
                    startService(mServiceIntent);
                }
            }
        }
    }

    public boolean getStatus(){
        stat = dbEmergencyShaker.getLastStatus();

        if(stat != null){
            if(stat.status == 1){
                return true;
            }else if(stat.status == 0){
                return  false;
            }
        }

        return false;
    }


    public void setStatusService(boolean setting){
        DbEmergencyShaker.Status st = new DbEmergencyShaker.Status();
        if(setting){
            st.status = 1;
            dbEmergencyShaker.insertStatus(st);

            if(!isMyServiceRunning(SensorService.class)){
                startService(mServiceIntent);
            }

        }else{
            st.status = 0;
            SensorService.isActive = false;
            dbEmergencyShaker.insertStatus(st);
        }

    }
}
