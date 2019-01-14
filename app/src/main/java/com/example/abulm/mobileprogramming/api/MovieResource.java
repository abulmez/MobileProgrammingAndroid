package com.example.abulm.mobileprogramming.api;

import com.example.abulm.mobileprogramming.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface MovieResource {
    String BASE_URL = "http://192.168.43.24:8080/";

    @GET("movies")
    Call<List<Movie>> getMovies(@Header("Authorization") String token);

    @PUT("movies/update")
    Call<Boolean> updateMovie(@Header("Authorization") String token,@Body Movie updatedMovie);

    @POST("movies/replaceAll")
    Call<Boolean> replaceMovieList(@Header("Authorization") String token,@Body List<Movie> newMovieList);
}

