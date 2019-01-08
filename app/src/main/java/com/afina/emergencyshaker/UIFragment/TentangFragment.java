package com.afina.emergencyshaker.UIFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.UIActivity.AboutDevActivity;
import com.afina.emergencyshaker.UIActivity.HowToActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TentangFragment extends Fragment implements View.OnClickListener{
    TextView tvDeskripsi;
    Button btnHowTo, btnAboutDev;



    public TentangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tentang, container, false);
        btnHowTo = (Button) view.findViewById(R.id.btn_how_to);
        btnHowTo.setOnClickListener(this);

        btnAboutDev = (Button)view.findViewById(R.id.btn_about_dev);
        btnAboutDev.setOnClickListener(this);

        String deskripsi = "<b>Emergency Shaker</b> merupakan aplikasi yang dapat membantumu dalam kondisi darurat. " +
                "<br> Perlu bantuan polisi? Ambulans? Atau kerabat terdekat? <br><b>Emergency Shaker</b> akan membantumu menghubungi mereka dengan lebih cepat!";

        tvDeskripsi = (TextView)view.findViewById(R.id.tv_deskripsi);
        tvDeskripsi.setText(Html.fromHtml(deskripsi));

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_how_to){
            Intent i = new Intent(getActivity(),HowToActivity.class);
            startActivity(i);
        }else if(v.getId() == R.id.btn_about_dev){
            Intent i = new Intent(getActivity(), AboutDevActivity.class);
            startActivity(i);
        }
    }

}
