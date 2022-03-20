package com.project.wegourmet.Repository.model;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.wegourmet.Repository.modelFirbase.RestaurantModelFirebase;
import com.project.wegourmet.Repository.modelFirbase.UserModelFirebase;
import com.project.wegourmet.model.Restaurant;
import com.project.wegourmet.model.User;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RestaurantModel {
    public static final RestaurantModel instance = new RestaurantModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    RestaurantModelFirebase modelFirebase = new RestaurantModelFirebase();

    // Methods
    MutableLiveData<Restaurant> restaurant = new MutableLiveData<Restaurant>();

//    public MutableLiveData<User> getUserById(String id) {
//            executor.execute(() -> {
//                User userById = AppLocalDb.db.userDao().getUserById(id);
//
//                if(userById != null) {
//                    user.postValue(userById);
//                }
//            });
//
//            modelFirebase.getUserByID(id, (fbUser) -> {
//                executor.execute(() -> {
//                    AppLocalDb.db.userDao().insert((User) fbUser);
//                    user.postValue((User) fbUser);
//                });
//            });
//
//            return user;
//    }
//
    public void addRestaurant(Restaurant newRestaurant, OnFailureListener failureListener) {
        modelFirebase.addRestaurant(newRestaurant, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                executor.execute(() -> {
                    AppLocalDb.db.restaurantDao().insert(newRestaurant);
                    restaurant.postValue(newRestaurant);
                    onSuccess(o);
                });
            }
        }, failureListener);
    }
//
//    public void setUser(User editedUser, Runnable success) {
//        modelFirebase.setUser(editedUser, (e) -> {
//            executor.execute(() -> {
//                AppLocalDb.db.userDao().insert(editedUser);
//                user.postValue(editedUser);
//                mainThread.post(() -> {
//                    success.run();
//                });
//
//            });
//        });
//    }
//
    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }


}
