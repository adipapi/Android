package com.project.wegourmet.Repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;


import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.Restaurant;

import java.util.List;

@Dao
public interface RestaurantDao {

    @Query("SELECT * FROM restaurants")
    List<Restaurant> getAll();

    @Query("SELECT * FROM restaurants where hostId = :hostId")
    List<Restaurant> getRestaurantsByHost(String hostId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Restaurant restaurant);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Restaurant> restaurants);

    @Delete
    void delete(Restaurant restaurant);
//    void insertAll(Restaurant restaurant);
}
