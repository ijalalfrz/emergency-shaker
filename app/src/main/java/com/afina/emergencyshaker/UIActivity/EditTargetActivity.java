package com.afina.emergencyshaker.UIActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Listeners.ShakeListener;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.R;

import java.util.ArrayList;
import java.util.List;

public class EditTargetActivity extends AppCompatActivity {
    DbEmergencyShaker dbEmergencyShaker;
    public static int EXTRA_ID = 0;

    LinearLayout layoutPlusEdit;

    Spinner spJenis;
    String item;

    Button btnUpdate;

    Target target, targetUpdate;

    EditText etNama, etTelepon, etJumlahShake;
    CheckBox cbTelepon, cbSms;
    ArrayList<Target> arr;

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_target);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_edit);
        setSupportActionBar(toolbar);
        setTitle("Ubah Kontak");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });


        dbEmergencyShaker = new DbEmergencyShaker(getApplicationContext());
        dbEmergencyShaker.open();


        id = getIntent().getIntExtra("EXTRA_ID", EXTRA_ID);
        target = new Target();
        target = dbEmergencyShaker.getTarget(id);

        etNama = (EditText)findViewById(R.id.et_nama_edit);
        etNama.setText(target.nama);
        etNama.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);


        etJumlahShake = (EditText)findViewById(R.id.et_jumlah_shake_edit);
        etJumlahShake.setText(String.valueOf(target.jumlah_shake));

        etTelepon = (EditText)findViewById(R.id.et_no_telp_edit);
        etTelepon.setText(target.telepon);


        cbSms = (CheckBox)findViewById(R.id.cb_sms_edit);
        if (target.yes_sms == 1){
            cbSms.setChecked(true);
        }

        btnUpdate = (Button)findViewById(R.id.btn_update);

        layoutPlusEdit = (LinearLayout)findViewById(R.id.layoutPlusEdit);

        spJenis = (Spinner)findViewById(R.id.sp_jenis_edit);
        List<String> categories = new ArrayList<String>();
        categories.add("Polisi");
        categories.add("Ambulans");
        categories.add("Pemadam Kebakaran");
        categories.add("Lainnya");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJenis.setAdapter(dataAdapter);

        int a = 0;
        for (String data: categories){
            if (target.jenis.equals(data)){
                spJenis.setSelection(a);
            }
            a++;
        }


        if (!target.jenis.equals("Lainnya")){
            layoutPlusEdit.setVisibility(View.INVISIBLE);

        }else {
            layoutPlusEdit.setVisibility(View.VISIBLE);
        }

        spJenis.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                item = adapterView.getItemAtPosition(i).toString();

                if (!item.equals("Lainnya")){
                    layoutPlusEdit.setVisibility(View.INVISIBLE);
                    if (item.equals("Polisi")){
                        etTelepon.setText("110");
                        if (!item.equals(target.jenis)){
                            etNama.setText(target.toString());
                        }
                    }else if (item.equals("Ambulans")){
                        etTelepon.setText("118");
                        if (!item.equals(target.jenis)){
                            etNama.setText(target.toString());
                        }
                    }else if (item.equals("Pemadam Kebakaran")){
                        etTelepon.setText("113");
                        if (!item.equals(target.jenis)){
                            etNama.setText(target.toString());
                        }
                    }
                }else{

                    if (!target.jenis.equals("Lainnya")){
                        etTelepon.setText("");
                        etNama.setText("");
                        cbTelepon.setChecked(false);
                    }else{
                        etTelepon.setText(target.telepon);
                        etNama.setText(target.nama);
                    }

                    layoutPlusEdit.setVisibility(View.VISIBLE);

                }

            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        arr = new ArrayList<>();
        arr = dbEmergencyShaker.getAllTarget();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{
                    targetUpdate = new Target();
                    targetUpdate.nama = etNama.getText().toString();
                    targetUpdate.jumlah_shake = Integer.parseInt((etJumlahShake.getText().toString()));
                    targetUpdate.telepon = etTelepon.getText().toString();
                    targetUpdate.yes_telepon = 1;
                    targetUpdate.yes_sms = 0;
                    targetUpdate.jenis = item;
                    int stat = 1;
                    String nama = "";

                    for (Target data: arr){
                        if (data.jumlah_shake == targetUpdate.jumlah_shake && data.id != id){
                            stat = 0;
                            nama = data.nama;
                        }
                    }

                    if (item.equals("Lainnya")){
                        if (cbSms.isChecked()){
                            targetUpdate.yes_sms = 1;
                        }


                    }
                    if (targetUpdate.jumlah_shake <= 5){
                        Toast toast = Toast.makeText(getApplicationContext(), "Jumlah shake harus lebh dari 5 kali.", Toast.LENGTH_SHORT);
                        toast.show();
                    }else if (stat == 0){
                        Toast toast = Toast.makeText(getApplicationContext(), "Jumlah shake sebanyak " + targetUpdate.jumlah_shake + " sudah dipakai oleh " + nama, Toast.LENGTH_SHORT);
                        toast.show();
                    }else{

                        update();

                    }

                }
                catch (Exception e){
                    Toast toast = Toast.makeText(getApplicationContext(), "Mohon mengisi data dengan benar.", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    public void update(){
        try{
            dbEmergencyShaker.updateTarget(targetUpdate, id);
            Toast toast = Toast.makeText(getApplicationContext(), targetUpdate.nama + " berhasil diubah", Toast.LENGTH_SHORT);
            toast.show();
            ShakeListener.updateTreshold();
            finish();
        }catch (Exception e){
//            Toast toast = Toast.makeText(getApplicationContext(), targetUpdate.nama + " gagal ditambahkan", Toast.LENGTH_SHORT);
            Toast toast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            toast.show();

        }
    }
}
