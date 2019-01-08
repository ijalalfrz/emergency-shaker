package com.afina.emergencyshaker.Internet;

import com.afina.emergencyshaker.Client.RetrofitClient;

public class APIUtils {

    private APIUtils() {}

    public static final String BASE_URL = "https://reguler.zenziva.net/apps/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}