package com.afina.emergencyshaker.UIActivity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.afina.emergencyshaker.Model.Status;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.Service.SensorService;
import com.afina.emergencyshaker.UIFragment.HomeFragment;
import com.afina.emergencyshaker.UIFragment.PengaturanFragment;
import com.afina.emergencyshaker.UIFragment.TentangFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

public class LayoutActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {
    public static double lat;
    public static double ln;

    private BottomNavigationView navBottom;
    private HomeFragment homeFragment;
    private TentangFragment tentangFragment;
    private PengaturanFragment pengaturanFragment;
    private int history = R.id.nav_home;


    private Intent mServiceIntent;
    private SensorService mSensorService;
    private Context ctx;


    private DbEmergencyShaker dbEmergencyShaker;
    private Status stat;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        initUI();
        buildGoogleApiClient();
        createLocationRequest();

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
        Log.i("INFOOO","DESTROYED");
        stopService(mServiceIntent);

        dbEmergencyShaker.close();

        super.onDestroy();

    }

    @Override
    protected void onPause() {
        super.onPause();
        dbEmergencyShaker.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
        checkService();
        dbEmergencyShaker.open();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
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
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "YA",
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
        dbEmergencyShaker.open();
        stat = dbEmergencyShaker.getLastStatus();
        dbEmergencyShaker.close();
        if(stat != null){
            if(stat.status == 1){
                SensorService.isActive = true;
                if(!isMyServiceRunning(SensorService.class)){
                    startService(mServiceIntent);
                }
            }
        }
    }

    public boolean getStatus(){
        dbEmergencyShaker.open();

        stat = dbEmergencyShaker.getLastStatus();
        dbEmergencyShaker.close();
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
        dbEmergencyShaker.open();
        Status st = new Status();
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
        dbEmergencyShaker.close();

    }

    //LOCATION THINGS

    private static final int MY_PERMISSIONS_REQUEST = 92;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    Location lastLocation;

    protected synchronized void buildGoogleApiClient(){
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

    }

    protected void createLocationRequest(){
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


    }

    private void ambilLokasi(){
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST);

            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,locationRequest,this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        ambilLokasi();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        ln = location.getLongitude();
    }
}
