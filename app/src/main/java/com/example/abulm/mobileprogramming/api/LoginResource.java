package com.example.abulm.mobileprogramming.api;

import com.example.abulm.mobileprogramming.model.Movie;
import com.example.abulm.mobileprogramming.model.UserDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface LoginResource {
    String BASE_URL = "http://192.168.43.24:8080/";

    @POST("login")
    Call<Void> login(@Body UserDTO loginData);
}
