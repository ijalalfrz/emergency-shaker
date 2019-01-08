package com.afina.emergencyshaker.UIFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private DbEmergencyShaker dbMhs;
    private RecyclerView rvTargetList;
    private Toolbar toolbar;

    private ListTargetAdapter adapter;

    private View view;

    public PengaturanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.toolbar_pengaturan_menu,menu);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_pengaturan, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar_pengaturan);
        toolbar.setTitle("Daftar Kontak");

        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


        dbMhs = new DbEmergencyShaker(view.getContext());
        dbMhs.open();

        rvTargetList = (RecyclerView)view.findViewById(R.id.rv_list_target);
        rvTargetList.setHasFixedSize(true);


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



    private void loadData(){


        adapter = new ListTargetAdapter(view.getContext(), dbMhs);
        ArrayList<Target> arr = new ArrayList<>();
        arr = dbMhs.getAllTargetByShake();
        adapter.setListTarget(arr);

        rvTargetList.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvTargetList.setAdapter(adapter);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.btn_add_target){
            Intent intent = new Intent(view.getContext(), AddTargetActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    //
    @Override
    public void onDestroy() {
        super.onDestroy();
        dbMhs.close();
    }
}
