package com.afina.emergencyshaker.UIActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.UIFragment.HomeFragment;
import com.afina.emergencyshaker.UIFragment.PengaturanFragment;
import com.afina.emergencyshaker.UIFragment.TentangFragment;

public class LayoutActivity extends AppCompatActivity {
    private BottomNavigationView navBottom;
    private HomeFragment homeFragment;
    private TentangFragment tentangFragment;
    private PengaturanFragment pengaturanFragment;
    private int history = R.id.nav_home;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        initUI();
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

    public void setFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =  getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame,fragment);
        fragmentTransaction.commit();
    }
}
