package com.afina.emergencyshaker.UIFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.afina.emergencyshaker.Adapter.ListTargetAdapter;
import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.UIActivity.AddTargetActivity;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PengaturanFragment extends Fragment {

    DbEmergencyShaker dbMhs;

    RecyclerView rvTargetList;
    public static ArrayList<Target> targetArrayList;

    Button btnAddTarget;

    ListTargetAdapter adapter;

    View view;

    public PengaturanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_pengaturan, container, false);
        dbMhs = new DbEmergencyShaker(view.getContext());
        dbMhs.open();
        loadData();
        // Inflate the layout for this fragment
        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        dbMhs = new DbEmergencyShaker(view.getContext());
        dbMhs.open();
        loadData();
    }

    private void showRecyclerList(){
        rvTargetList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ListTargetAdapter listTargetAdapter = new ListTargetAdapter(view.getContext());
        listTargetAdapter.setListTarget(targetArrayList);
        rvTargetList.setAdapter(listTargetAdapter);
    }

    private void loadData(){


        adapter = new ListTargetAdapter(view.getContext());
        ArrayList<Target> arr = new ArrayList<>();
        arr = dbMhs.getAllTarget();
        adapter.setListTarget(arr);

        rvTargetList = (RecyclerView)view.findViewById(R.id.rv_list_target);
        rvTargetList.setHasFixedSize(true);

        targetArrayList = new ArrayList<Target>();

        targetArrayList = dbMhs.getAllTarget();

        btnAddTarget = (Button)view.findViewById(R.id.btn_add_target);
        btnAddTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), AddTargetActivity.class);
                startActivity(intent);
            }
        });

        Button btnHapus = (Button)view.findViewById(R.id.btn_hapus);
        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbMhs.deleteAllTarget();
                adapter.notifyDataSetChanged();

            }
        });

        showRecyclerList();
    }


//
    @Override
    public void onDestroy() {
        super.onDestroy();
        dbMhs.close();
    }
}
