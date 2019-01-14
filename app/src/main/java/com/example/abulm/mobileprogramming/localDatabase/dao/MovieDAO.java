package com.example.abulm.mobileprogramming.localDatabase.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.abulm.mobileprogramming.model.Movie;

import java.util.List;

@Dao
public interface MovieDAO {

    @Update
    void update(Movie updateMovie);

    @Insert
    void insert(Movie newMovie);

    @Query("SELECT * FROM movie")
    LiveData<List<Movie>> getAllMovies();

    @Query("DELETE FROM movie")
    void deleteAll();
}
