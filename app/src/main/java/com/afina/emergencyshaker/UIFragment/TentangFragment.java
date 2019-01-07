package com.afina.emergencyshaker.UIFragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afina.emergencyshaker.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TentangFragment extends Fragment {
    TextView tvDeskripsi;



    public TentangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tentang, container, false);

        String deskripsi = "<b>Emergency Shaker</b> merupakan aplikasi yang dapat membantumu dalam kondisi darurat. " +
                "<br> Perlu bantuan polisi? Ambulans? Atau kerabat terdekat? <br><b>Emergency Shaker</b> akan membantumu menghubungi mereka dengan lebih cepat!";

        tvDeskripsi = (TextView)view.findViewById(R.id.tv_deskripsi);
        tvDeskripsi.setText(Html.fromHtml(deskripsi));

        return view;
    }

}
