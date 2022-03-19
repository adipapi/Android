package com.project.wegourmet.Repository.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.wegourmet.Repository.dao.UserDao;
import com.project.wegourmet.WegourmetApplication;
import com.project.wegourmet.model.User;

@Database(entities = {User.class}, version = 2)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract UserDao userDao();
}

public class AppLocalDb{
    static public AppLocalDbRepository db =
            Room.databaseBuilder(WegourmetApplication.getContext(),
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}
