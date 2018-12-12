package com.afina.emergencyshaker.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Target implements Parcelable {

    public int id;
    public String nama;
    public int jumlah_shake;
    public String telepon;
    public String notif;
    public String sms;
    public String email;

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

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
        dest.writeString(this.notif);
        dest.writeString(this.sms);
        dest.writeString(this.email);
    }

    protected Target(Parcel in) {
        this.id = in.readInt();
        this.nama = in.readString();
        this.jumlah_shake = in.readInt();
        this.telepon = in.readString();
        this.notif = in.readString();
        this.sms = in.readString();
        this.email = in.readString();
    }

    public static final Parcelable.Creator<Target> CREATOR = new Parcelable.Creator<Target>() {
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

