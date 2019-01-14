package com.example.abulm.mobileprogramming.localDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.abulm.mobileprogramming.localDatabase.dao.MovieDAO;
import com.example.abulm.mobileprogramming.localDatabase.dao.UserDAO;
import com.example.abulm.mobileprogramming.model.Movie;
import com.example.abulm.mobileprogramming.model.UserDTO;


@Database(entities = {Movie.class, UserDTO.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract MovieDAO movieDAO();

    public abstract UserDAO userDAO();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
