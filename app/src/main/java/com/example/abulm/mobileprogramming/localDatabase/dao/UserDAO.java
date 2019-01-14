package com.example.abulm.mobileprogramming.localDatabase.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.abulm.mobileprogramming.model.UserDTO;

@Dao
public interface UserDAO {

    @Query("SELECT COUNT(*) FROM user WHERE username = :username AND password = :password")
    Integer checkIfValidLogin(String username, String password);

    @Insert
    void insert(UserDTO userDTO);
}
