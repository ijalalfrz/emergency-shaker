package com.afina.emergencyshaker.UIActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Model.Status;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.Service.SensorService;

public class MainActivity extends AppCompatActivity {
    public static boolean inBackground = false;
    private Intent mServiceIntent;
    private SensorService mSensorService;
    private Context ctx;
    public static MainActivity instance;
    public static boolean sw = false;

    Switch mSwitch;
    public Context getCtx() {
        return ctx;
    }

    Button button;

    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private TextView tvPower;

    private DbEmergencyShaker dbEmergencyShaker;
    private Status stat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        inBackground = false;
        dbEmergencyShaker = new DbEmergencyShaker(getApplicationContext());
        dbEmergencyShaker.open();


        ctx = this;

        setTitle("");
        setContentView(R.layout.activity_main);

        // Navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch(id) {
                    case R.id.about:
                        Toast.makeText(MainActivity.this, "About", Toast.LENGTH_SHORT).show();
                    case R.id.settings:
                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                    case R.id.main_home:
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                    default:

                        return true;
                }
            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);






        // End of Navigation drawer


        mSensorService = new SensorService(getApplicationContext());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());

        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE},1);

        }


        tvPower = (TextView)findViewById(R.id.tvPower);
        mSwitch = (Switch)findViewById(R.id.switchID);



        stat = dbEmergencyShaker.getLastStatus();
        if(stat != null){
            if(stat.status == 1){
                startService(mServiceIntent);
                mSwitch.setChecked(true);
                tvPower.setText("Power: ON");
            }else if(stat.status == 0){
                mSwitch.setChecked(false);
                tvPower.setText("Power: OFF");

            }
        }

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sw = true;
                    if (!isMyServiceRunning(mSensorService.getClass())) {
                        startService(mServiceIntent);
                    }
                    tvPower.setText("Power: ON");

                    Status st = new Status();
                    st.status = 1;
                    SensorService.isActive = true;


                    if (dbEmergencyShaker.getLastStatus() != null){
                        dbEmergencyShaker.insertStatus(st);
                    }else{
                        int id = dbEmergencyShaker.getLastStatus().id;
                        dbEmergencyShaker.updateStatus(st.status, id);
                    }


                }else{
                    sw = false;
                    tvPower.setText("Power: OFF");

                    Status st = new Status();
                    st.status = 0;
                    SensorService.isActive = false;

                    if (dbEmergencyShaker.getLastStatus() != null){
                        dbEmergencyShaker.insertStatus(st);
                    }else{
                        int id = dbEmergencyShaker.getLastStatus().id;
                        dbEmergencyShaker.updateStatus(st.status, id);
                    }

                }
                dbEmergencyShaker.close();

            }
        });



        instance = this;


    }

    @Override
    public void onBackPressed(){
        if(mDrawerLayout.isDrawerOpen(navigationView)){
            //mDrawerLayout.closeDrawer(navigationView);

        }else {
            //finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);

                return true;
        }

        return super.onOptionsItemSelected(item);
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

        stat = dbEmergencyShaker.getLastStatus();

        if(stat.status == 1){
            mSwitch.setChecked(true);
        }else if(stat.status == 0){
            mSwitch.setChecked(false);

        }
        dbEmergencyShaker.close();
        super.onDestroy();

    }




    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume

        stat = dbEmergencyShaker.getLastStatus();
        if(stat.status == 1){
            mSwitch.setChecked(true);
        }else if(stat.status == 0){
            mSwitch.setChecked(false);

        }

    }

    @Override
    public void onRestart(){
        super.onRestart();

        stat = dbEmergencyShaker.getLastStatus();

        if(stat.status == 1){
            mSwitch.setChecked(true);
        }else if(stat.status == 0){
            mSwitch.setChecked(false);

        }

    }



    @Override
    public void onPause() {
        super.onPause();

        stat = dbEmergencyShaker.getLastStatus();

        if(stat.status == 1){
            mSwitch.setChecked(true);
        }else if(stat.status == 0){
            mSwitch.setChecked(false);

        }

    }

}
