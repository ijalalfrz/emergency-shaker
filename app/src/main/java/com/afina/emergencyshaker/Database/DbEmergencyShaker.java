package com.afina.emergencyshaker.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.afina.emergencyshaker.Helper.OpenHelper;
import com.afina.emergencyshaker.Model.Status;
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
        newValues.put("YES_TELEPON", target.yes_telepon);
        newValues.put("YES_SMS", target.yes_sms);
        return db.insert("TARGET", null, newValues);
    }

    public long insertStatus(Status st){
        ContentValues newValues = new ContentValues();
        newValues.put("STATUS", st.status);

        return  db.insert("STATUS", null, newValues);
    }

    public Status getLastStatus(){
        Cursor cur = null;
        ArrayList<Status> list = new ArrayList<>();

        cur = db.rawQuery("SELECT * FROM STATUS ORDER BY ID DESC Limit 1", null);
        if(cur.moveToFirst()){
            do {
                Status status = new Status();
                status.id = cur.getInt(0);
                status.status = cur.getInt(1);

                list.add(status);
            }while (cur.moveToNext());
        }
        cur.close();

        Status out = new Status();
        for (Status status: list) {
            out = status;
        }

        return out;

    }

    public void updateStatus(int statusNew, int id){
        String sql;
        sql = "UPDATE STATUS SET status = "+statusNew+" WHERE id = "+ id;
        db.execSQL(sql);
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
            target.yes_telepon = cur.getInt(4);
            target.yes_sms = cur.getInt(5);
        }
        cur.close();
        return target;
    }

    public ArrayList<Target> getAllTarget(){
        Cursor cur = null;
        ArrayList<Target> out = new ArrayList<>();

        cur = db.rawQuery("SELECT id, nama, jumlah_shake, telepon, yes_telepon, yes_sms FROM TARGET Limit 10", null);
        if(cur.moveToFirst()){
            do {
                Target target = new Target();
                target.id = cur.getInt(0);
                target.nama = cur.getString(1);
                target.jumlah_shake = cur.getInt(2);
                target.telepon = cur.getString(3);
                target.yes_telepon = cur.getInt(4);
                target.yes_sms = cur.getInt(5);
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

    public void deleteAllTarget(){
        String sql;
        sql = "DELETE FROM TARGET";
        db.execSQL(sql);
    }

    public void updateTarget(String namaNew, String namaOld){
        String sql;
        sql = "UPDATE TARGET SET nama = '"+namaNew+"' WHERE nama = '"+namaOld+"'";
        db.execSQL(sql);
    }
}



