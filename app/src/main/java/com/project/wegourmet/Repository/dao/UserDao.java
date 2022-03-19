package com.project.wegourmet.Repository.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.wegourmet.model.User;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users where id = :id")
    User getUserById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);
}
