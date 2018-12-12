package com.afina.emergencyshaker.UIActivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afina.emergencyshaker.Database.DbEmergencyShaker;
import com.afina.emergencyshaker.Model.Target;
import com.afina.emergencyshaker.R;

public class AddTargetActivity extends AppCompatActivity {

    Button btnAdd;
    EditText etNama,etJumlahShake,etTelepon,etNotif,etSms,etEmail;
    DbEmergencyShaker dbEmergencyShaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_target);

        dbEmergencyShaker = new DbEmergencyShaker(getApplicationContext());

        etNama = (EditText)findViewById(R.id.et_nama);
        etJumlahShake = (EditText)findViewById(R.id.et_jumlah_shake);
        etTelepon = (EditText)findViewById(R.id.et_telepon);
        etNotif = (EditText)findViewById(R.id.et_notif);
        etSms = (EditText)findViewById(R.id.et_sms);
        etEmail = (EditText)findViewById(R.id.et_email);

        btnAdd = (Button)findViewById(R.id.btn_add);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Target target = new Target();
                target.nama = etNama.getText().toString();
                target.jumlah_shake = Integer.parseInt((etJumlahShake.getText().toString()));
                target.notif = etNotif.getText().toString();
                target.sms = etSms.getText().toString();
                target.telepon = etTelepon.getText().toString();
                target.email = etEmail.getText().toString();

                dbEmergencyShaker.open();
                if(dbEmergencyShaker.insertTarget(target) != -1){
                    Toast.makeText(getApplicationContext(),String.format("%s Berhasil ditambahkan",target.nama,target.jumlah_shake), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),String.format("Gagal %s",target.jumlah_shake), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
