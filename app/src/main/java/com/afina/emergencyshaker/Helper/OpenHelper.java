package com.afina.emergencyshaker.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "dbEmergencyShaker.db";
    public static final String TABLE_CREATE = "CREATE TABLE TARGET (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAMA TEXT, JUMLAH_SHAKE INTEGER, TELEPON TEXT, NOTIF TEXT, SMS TEXT, EMAIL TEXT)";

    public OpenHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create db
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //jika app diupgrade (diinstall yang baru) maka database akan dicreate ulang (data hilang)
        //jika tidak ingin hilang, bisa diproses disini

        sqLiteDatabase.execSQL("DROP TABLE IF EXIST MAHASISWA");
    }
}

