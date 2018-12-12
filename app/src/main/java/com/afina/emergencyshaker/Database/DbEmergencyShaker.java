package com.afina.emergencyshaker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.afina.emergencyshaker.Helper.OpenHelper;
import com.afina.emergencyshaker.Model.Target;

import java.util.ArrayList;

public class DbEmergencyShaker {

    private SQLiteDatabase db;
    private final OpenHelper dbHelper;

    public DbEmergencyShaker(Context c){
        dbHelper = new OpenHelper(c);
    }

    public void open(){
        db = dbHelper.getWritableDatabase();
    }

    public void close(){
        db.close();
    }

    public long insertTarget(Target target){
        ContentValues newValues = new ContentValues();
        newValues.put("NAMA", target.nama);
        newValues.put("JUMLAH_SHAKE", target.jumlah_shake);
        newValues.put("TELEPON", target.telepon);
        newValues.put("NOTIF", target.notif);
        newValues.put("SMS", target.sms);
        newValues.put("EMAIL", target.email);
        return db.insert("TARGET", null, newValues);
    }

    //ambil data mahasiswa berdasarkan nama
    public Target getTarget(String nama){
        Cursor cur = null;
        Target target = new Target();

        //kolom yang diambil
        String[] cols = new String[]{"ID","NAMA", "JUMLAH_SHAKE", "TELEPON", "NOTIF", "SMS", "EMAIL"};
        //parameter, akan mengganti ? pada NAMA=?
        String[] param = {nama};

        cur = db.query("TARGET",cols,"NAMA=?",param,null,null,null);

        if(cur.getCount()>0){
            cur.moveToFirst();
            target.id = cur.getInt(0);
            target.nama = cur.getString(1);
            target.jumlah_shake = cur.getInt(2);
            target.telepon = cur.getString(3);
            target.notif = cur.getString(4);
            target.sms = cur.getString(5);
            target.email = cur.getString(6);
        }
        cur.close();
        return target;
    }

    public ArrayList<Target> getAllTarget(){
        Cursor cur = null;
        ArrayList<Target> out = new ArrayList<>();

        cur = db.rawQuery("SELECT id, nama, jumlah_shake, telepon, notif, sms, email FROM TARGET Limit 10", null);
        if(cur.moveToFirst()){
            do {
                Target target = new Target();
                target.id = cur.getInt(0);
                target.nama = cur.getString(1);
                target.jumlah_shake = cur.getInt(2);
                target.telepon = cur.getString(3);
                target.notif = cur.getString(4);
                target.sms = cur.getString(5);
                target.email = cur.getString(6);
                out.add(target);
            }while (cur.moveToNext());
        }
        cur.close();
        return out;
    }

    public void deleteTarget(String xnama){
        String sql;
        sql = "DELETE FROM TARGET WHERE nama ='"+xnama+"'";
        db.execSQL(sql);

    }

    public void updateTarget(String namaNew, String namaOld){
        String sql;
        sql = "UPDATE TARGET SET nama = '"+namaNew+"' WHERE nama = '"+namaOld+"'";
        db.execSQL(sql);
    }
}



