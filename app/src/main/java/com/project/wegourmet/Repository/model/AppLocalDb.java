package com.project.wegourmet.Repository.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.wegourmet.Repository.dao.PostDao;
import com.project.wegourmet.Repository.dao.RestaurantDao;
import com.project.wegourmet.Repository.dao.UserDao;
import com.project.wegourmet.WegourmetApplication;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.Restaurant;
import com.project.wegourmet.model.User;

@Database(entities = {User.class, Restaurant.class, Post.class}, version = 9)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract RestaurantDao restaurantDao();
    public abstract PostDao postDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(WegourmetApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
