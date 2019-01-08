package com.afina.emergencyshaker.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Target implements Parcelable {

    public int id;
    public String nama;
    public int jumlah_shake;
    public String telepon;
    public int yes_telepon;
    public int yes_sms;
    public String jenis;

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getYes_telepon() {
        return yes_telepon;
    }

    public void setYes_telepon(int yes_telepon) {
        this.yes_telepon = yes_telepon;
    }

    public int getYes_sms() {
        return yes_sms;
    }

    public void setYes_sms(int yes_sms) {
        this.yes_sms = yes_sms;
    }

    public Target(){
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getJumlah_shake() {
        return jumlah_shake;
    }

    public void setJumlah_shake(int jumlah_shake) {
        this.jumlah_shake = jumlah_shake;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.nama);
        dest.writeInt(this.jumlah_shake);
        dest.writeString(this.telepon);
        dest.writeInt(this.yes_telepon);
        dest.writeInt(this.yes_sms);
        dest.writeString(this.jenis);
    }

    protected Target(Parcel in) {
        this.id = in.readInt();
        this.nama = in.readString();
        this.jumlah_shake = in.readInt();
        this.telepon = in.readString();
        this.yes_telepon = in.readInt();
        this.yes_sms = in.readInt();
        this.jenis = in.readString();
    }

    public static final Creator<Target> CREATOR = new Creator<Target>() {
        @Override
        public Target createFromParcel(Parcel source) {
            return new Target(source);
        }

        @Override
        public Target[] newArray(int size) {
            return new Target[size];
        }
    };
}

