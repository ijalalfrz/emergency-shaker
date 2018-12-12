package com.afina.emergencyshaker.UIActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.afina.emergencyshaker.Adapter.ListTargetAdapter;
import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.R;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity {

    DbEmergencyShaker dbMhs;

    RecyclerView rvTargetList;
    public static ArrayList<Target> targetArrayList;

    Button btnAddTarget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        rvTargetList = (RecyclerView)findViewById(R.id.rv_list_target);
        rvTargetList.setHasFixedSize(true);

        targetArrayList = new ArrayList<Target>();

        dbMhs = new DbEmergencyShaker(getApplicationContext());
        dbMhs.open();
        targetArrayList = dbMhs.getAllTarget();

        btnAddTarget = (Button)findViewById(R.id.btn_add_target);
        btnAddTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this, AddTargetActivity.class);
                startActivity(intent);
            }
        });

        showRecyclerList();

    }

    private void showRecyclerList(){
        rvTargetList.setLayoutManager(new LinearLayoutManager(this));
        ListTargetAdapter listTargetAdapter = new ListTargetAdapter(this);
        listTargetAdapter.setListTarget(targetArrayList);
        rvTargetList.setAdapter(listTargetAdapter);
    }
}