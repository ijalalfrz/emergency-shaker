package com.afina.emergencyshaker.UIActivity;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

import android.widget.ImageView;
import android.widget.TextView;

import com.afina.emergencyshaker.R;


public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String deskripsi = "<b>Emergency Shaker</b> merupakan aplikasi yang dapat membantumu dalam kondisi darurat. <br> Perlu bantuan polisi? Ambulans? Atau kerabat terdekat? <br><b>Emergency Shaker</b> akan membantumu menghubungi mereka dengan lebih cepat!";

        TextView tvDeskripsi = (TextView)findViewById(R.id.tv_deskripsi);
        tvDeskripsi.setText(Html.fromHtml(deskripsi));



    }
}
