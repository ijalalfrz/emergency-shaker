package com.afina.emergencyshaker.UIFragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afina.emergencyshaker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private boolean status = false;
    private ImageView ivSwitch;
    private TextView tvStatus;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        initUI(view);

        return view;
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
                }else{
                    status = false;
                    ivSwitch.setImageResource(R.drawable.ic_switch_off);
                    tvStatus.setText("OFF");
                    tvStatus.setTextColor(Color.parseColor("#777777"));

                }
            }
        });

    }

}
