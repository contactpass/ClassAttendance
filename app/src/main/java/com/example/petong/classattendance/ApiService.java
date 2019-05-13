package com.example.petong.classattendance;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface ApiService {
    @POST("API/User/checkUser")
    Call<User> getUser(@Body RequestBody body);
}
