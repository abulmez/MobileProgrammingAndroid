package com.example.abulm.mobileprogramming.service;

import android.content.Context;
import android.widget.Toast;

import com.example.abulm.mobileprogramming.MainActivity;
import com.example.abulm.mobileprogramming.api.MovieResource;
import com.example.abulm.mobileprogramming.localDatabase.AppDatabase;
import com.example.abulm.mobileprogramming.localDatabase.dao.MovieDAO;
import com.example.abulm.mobileprogramming.localDatabase.dao.UserDAO;
import com.example.abulm.mobileprogramming.model.Movie;
import com.example.abulm.mobileprogramming.utils.MyApp;

import java.util.List;
import java.util.Observable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieService extends Observable {

    private AppDatabase offlineDatabase;
    private MovieDAO movieDAO;

    public void initOfflineDatabase() {
        offlineDatabase = AppDatabase.getDatabase(MyApp.getContext());
        movieDAO = offlineDatabase.movieDAO();
    }

    public MovieService() {
        initOfflineDatabase();
    }

    public void updateMovie(Movie updatedMovie) {

        if (!LoginService.offlineMode) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MovieResource.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            MovieResource api = retrofit.create(MovieResource.class);
            Call<Boolean> call = api.updateMovie(LoginService.token, updatedMovie);
            call.enqueue(new Callback<Boolean>() {
                @Override
                public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                    updateMovieInLocalStorage(updatedMovie);
                    setChanged();
                    notifyObservers(response.body());
                }

                @Override
                public void onFailure(Call<Boolean> call, Throwable t) {
                    Toast.makeText(MyApp.getContext(), "Server error!", Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            updateMovieInLocalStorage(updatedMovie);
            setChanged();
            notifyObservers(true);
        }

    }

    private void updateMovieInLocalStorage(Movie updatedMovie){
        Thread movieUpdateThread = new Thread(new MovieUpdateThread(movieDAO, updatedMovie));
        movieUpdateThread.start();
        try {
            movieUpdateThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void replaceMovieListServerRequest(List<Movie> newMovieList) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MovieResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieResource api = retrofit.create(MovieResource.class);
        Call<Boolean> call = api.replaceMovieList(LoginService.token, newMovieList);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body().equals(false)) {
                    throw new RuntimeException("An error occurred while replacing server data with local data.");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                throw new RuntimeException("Server error!");
            }
        });
    }

    private static class MovieUpdateThread implements Runnable {

        private MovieDAO mThreadDao;
        private Movie updatedMovie;

        public MovieUpdateThread(MovieDAO mThreadDao, Movie updatedMovie) {
            this.mThreadDao = mThreadDao;
            this.updatedMovie = updatedMovie;
        }

        @Override
        public void run() {
            mThreadDao.update(updatedMovie);
        }
    }

}
