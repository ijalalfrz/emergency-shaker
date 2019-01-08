package com.afina.emergencyshaker.Internet;

import com.afina.emergencyshaker.Model.Response;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIService {
    @POST("smsapi.php?")
    @FormUrlEncoded
    Call<Response> sendSMS(@Field("userkey") String userkey, @Field("passkey") String passkey, @Field("nohp") String nohp, @Field("pesan") String pesan);
}
