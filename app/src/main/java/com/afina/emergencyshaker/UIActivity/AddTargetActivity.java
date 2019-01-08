package com.afina.emergencyshaker.UIActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Listeners.ShakeListener;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.R;
import com.afina.emergencyshaker.UIFragment.PengaturanFragment;

import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

public class AddTargetActivity extends AppCompatActivity{
    Button btnAdd;
    EditText etNama,etJumlahShake,etTelepon,etNotif,etSms,etEmail;
    DbEmergencyShaker dbEmergencyShaker;

    Spinner spJenis;

    ArrayList<Target> arrTarget;
    Target target;

    CheckBox cbTelepon, cbSms;

    PengaturanFragment pengaturanFragment;

    String item;

    LinearLayout layoutPlus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_target);
        dbEmergencyShaker = new DbEmergencyShaker(getApplicationContext());
        dbEmergencyShaker.open();
        loadData();

    }

    @Override
    protected void onResume() {
        super.onResume();
        dbEmergencyShaker = new DbEmergencyShaker(getApplicationContext());
        dbEmergencyShaker.open();
        loadData();
    }

    public void loadData(){
        spJenis = (Spinner)findViewById(R.id.sp_jenis);
        arrTarget = new ArrayList<>();


        layoutPlus = (LinearLayout)findViewById(R.id.layoutPlus);

        List<String> categories = new ArrayList<String>();
        categories.add("Polisi");
        categories.add("Ambulans");
        categories.add("Pemadam Kebakaran");
        categories.add("Lainnya");

        etJumlahShake = (EditText)findViewById(R.id.et_jumlah_shake);
        etTelepon = (EditText)findViewById(R.id.et_no_telp);
        etNama = (EditText)findViewById(R.id.et_nama);
        etNama.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        cbTelepon = (CheckBox)findViewById(R.id.cb_telepon);
        cbSms = (CheckBox)findViewById(R.id.cb_sms);
//        etSms = (EditText)findViewById(R.id.et_sms);

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJenis.setAdapter(dataAdapter);


        spJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Your code here


                item = adapterView.getItemAtPosition(i).toString();
//                Toast toast = Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT);
//                toast.show();

                if (!item.equals("Lainnya")){
                    layoutPlus.setVisibility(View.INVISIBLE);

                    if (item.equals("Polisi")){
                        etTelepon.setText("110");
                        etNama.setText("Polisi");
                    }else if (item.equals("Ambulans")){
                        etTelepon.setText("118");
                        etNama.setText("Ambulans");
                    }else if (item.equals("Pemadam Kebakaran")){
                        etTelepon.setText("113");
                        etNama.setText("Pemadam Kebakaran");
                    }

                }else{
                    etTelepon.setText("");
                    etNama.setText("");
                    layoutPlus.setVisibility(View.VISIBLE);




                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });




        btnAdd = (Button)findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    target = new Target();
                    target.nama = etNama.getText().toString();
                    target.jumlah_shake = Integer.parseInt((etJumlahShake.getText().toString()));
                    target.telepon = etTelepon.getText().toString();
                    target.yes_telepon = 0;
                    target.yes_sms = 0;
                    target.jenis = item;
                    ArrayList<Target> arr = new ArrayList<>();
                    arr = dbEmergencyShaker.getAllTarget();
                    int stat = 1;
                    String nama = "";

                    for (Target data: arr){
                        if (data.jumlah_shake == target.jumlah_shake){
                            stat = 0;
                            nama = data.nama;
                        }
                    }

                    if (item.equals("Lainnya")){
                        if (!cbTelepon.isChecked() && !cbSms.isChecked()){
                            Toast toast = Toast.makeText(getApplicationContext(), "Mohon memilih salah satu metode pemanggilan.", Toast.LENGTH_SHORT);
                            toast.show();
                        }else if ((cbTelepon.isChecked() && cbSms.isChecked()) || (cbTelepon.isChecked() || cbSms.isChecked())){
                            if (cbSms.isChecked()){
                                target.yes_sms = 1;
                            }
                            if (cbTelepon.isChecked()){
                                target.yes_telepon = 1;
                            }



                            if (target.jumlah_shake == 5){
                                Toast toast = Toast.makeText(getApplicationContext(), "Jumlah shake harus lebh dari 5 kali.", Toast.LENGTH_SHORT);
                                toast.show();
                            }else if (stat == 0){
                                Toast toast = Toast.makeText(getApplicationContext(), "Jumlah shake sebanyak " + target.jumlah_shake + " sudah dipakai oleh " + nama, Toast.LENGTH_SHORT);
                                toast.show();
                            }else{

                                insert();

                            }
                        }
                    }else{
                        target.yes_telepon = 1;
                        if (target.jumlah_shake <= 5){
                            Toast toast = Toast.makeText(getApplicationContext(), "Jumlah shake harus lebh dari 5 kali.", Toast.LENGTH_SHORT);
                            toast.show();
                        }else if (stat == 0){
                            Toast toast = Toast.makeText(getApplicationContext(), "Jumlah shake sebanyak " + target.jumlah_shake + " sudah dipakai oleh " + nama, Toast.LENGTH_SHORT);
                            toast.show();
                        }else{

                            insert();

                        }
                    }
                }
                catch (Exception e){
                    Toast toast = Toast.makeText(getApplicationContext(), "Mohon mengisi data dengan benar.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }

    public void insert(){
        try{
            dbEmergencyShaker.insertTarget(target);
            Toast toast = Toast.makeText(getApplicationContext(), target.nama + " berhasil ditambahkan", Toast.LENGTH_SHORT);
            toast.show();
            ShakeListener.updateTreshold();
            finish();
        }catch (Exception e){
            Toast toast = Toast.makeText(getApplicationContext(), target.nama + " gagal ditambahkan", Toast.LENGTH_SHORT);
            toast.show();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbEmergencyShaker.close();
    }
}
