package com.afina.emergencyshaker.UIFragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.Service.SensorService;
import com.afina.emergencyshaker.UIActivity.LayoutActivity;
import com.afina.emergencyshaker.UIActivity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private boolean status = false;
    private ImageView ivSwitch;
    private TextView tvStatus;
    private Context ctx;
    private Activity Main;
    public HomeFragment() {

        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ctx = getActivity().getApplicationContext();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Main =  getActivity();
        status = ((LayoutActivity) Main).getStatus();




        //==========================

        initUI(view);
        setSwitch();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        status = ((LayoutActivity) Main).getStatus();

        setSwitch();

    }

    @Override
    public void onResume() {
        super.onResume();
        status = ((LayoutActivity) Main).getStatus();

        setSwitch();


    }

    @Override
    public void onPause() {
        super.onPause();
        status = ((LayoutActivity) Main).getStatus();

        setSwitch();


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        status = ((LayoutActivity) Main).getStatus();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void setSwitch(){
        if(status){
            ivSwitch.setImageResource(R.drawable.ic_switch_on);
            tvStatus.setText("ON");
            tvStatus.setTextColor(Color.parseColor("#CC1228"));
        }else{
            ivSwitch.setImageResource(R.drawable.ic_switch_off);
            tvStatus.setText("OFF");
            tvStatus.setTextColor(Color.parseColor("#777777"));
        }
    }

    public void initUI(View view){
        ivSwitch = (ImageView) view.findViewById(R.id.iv_switch);
        tvStatus = (TextView) view.findViewById(R.id.tv_status);

        ivSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!status){
                    status = true;
                    ivSwitch.setImageResource(R.drawable.ic_switch_on);
                    tvStatus.setText("ON");
                    tvStatus.setTextColor(Color.parseColor("#CC1228"));

                    ((LayoutActivity) Main).setStatusService(status);

                }else{
                    status = false;
                    ivSwitch.setImageResource(R.drawable.ic_switch_off);
                    tvStatus.setText("OFF");
                    tvStatus.setTextColor(Color.parseColor("#777777"));

                    ((LayoutActivity) Main).setStatusService(status);

                }
            }
        });

    }

}
