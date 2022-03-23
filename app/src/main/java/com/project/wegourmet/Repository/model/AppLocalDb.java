package com.project.wegourmet.Repository.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.wegourmet.Repository.dao.RestaurantDao;
import com.project.wegourmet.Repository.dao.UserDao;
import com.project.wegourmet.WegourmetApplication;
import com.project.wegourmet.model.Restaurant;
import com.project.wegourmet.model.User;

@Database(entities = {User.class, Restaurant.class}, version = 7)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract RestaurantDao restaurantDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(WegourmetApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
