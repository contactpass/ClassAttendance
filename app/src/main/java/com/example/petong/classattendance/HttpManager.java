package com.example.petong.classattendance;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpManager {
    private static HttpManager ourInstance = new HttpManager();
    private ApiService service;

    public static HttpManager getInstance() {
        if (ourInstance == null){
            ourInstance = new HttpManager();
        }
        return ourInstance;
    }

    private HttpManager() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sis.cmu.ac.th/cmusis/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiService.class);
    }

    public ApiService getService() {
        return service;
    }
}
