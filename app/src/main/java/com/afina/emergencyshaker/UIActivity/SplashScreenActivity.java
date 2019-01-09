package com.afina.emergencyshaker.UIActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.afina.emergencyshaker.R;


public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Start home activity
        startActivity(new Intent(getApplicationContext(), LayoutActivity.class));
        // close splash activity
        finish();
    }
}
