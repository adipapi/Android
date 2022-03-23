package com.project.wegourmet.Repository.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.User;

import java.util.List;

@Dao
public interface PostDao {

    @Query("SELECT * FROM posts")
    List<Post> getAll();

    @Query("SELECT * FROM posts where id = :id")
    Post getById(String id);

    @Query("SELECT * FROM posts where restaurantName = :restaurantName")
    List<Post> getPostsByRestaurant(String restaurantName);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMany(List<Post> posts);

    @Delete
    void delete(Post post);
}
