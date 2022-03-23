package com.project.wegourmet.Repository.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.project.wegourmet.Repository.modelFirbase.RestaurantModelFirebase;
import com.project.wegourmet.WegourmetApplication;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.Restaurant;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RestaurantModel {
    public static final RestaurantModel instance = new RestaurantModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    RestaurantModelFirebase modelFirebase = new RestaurantModelFirebase();
    MutableLiveData<Restaurant> restaurant = new MutableLiveData<Restaurant>();


    public enum RestaurantListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<RestaurantListLoadingState> restaurantListLoadingState = new MutableLiveData<RestaurantListLoadingState>();

    public LiveData<RestaurantListLoadingState> getRestaurantListLoadingState() {
        return restaurantListLoadingState;
    }

//    private RestaurantModel() {
//        restaurantListLoadingState.setValue(RestaurantListLoadingState.loaded);
//    }

    MutableLiveData<List<Restaurant>> restaurantsList = new MutableLiveData<List<Restaurant>>();

    public MutableLiveData<List<Restaurant>> getAll() {
        restaurantListLoadingState.setValue(RestaurantListLoadingState.loading);
        executor.execute(() -> {
            List<Restaurant> rests = AppLocalDb.db.restaurantDao().getAll();

            if(rests != null) {
                restaurantsList.postValue(rests);
            }
        });

        modelFirebase.getAllRestaurants((fbRestaurants) -> {
            executor.execute(() -> {
                AppLocalDb.db.restaurantDao().insertMany((List<Restaurant>) fbRestaurants);
                restaurantsList.postValue((List<Restaurant>) fbRestaurants);
                mainThread.post(() -> {
                    restaurantListLoadingState.setValue(RestaurantListLoadingState.loaded);
                });
            });
        });
        if (restaurantsList.getValue() == null) {
//            refreshRestaurantList();
        }
        ;
        return restaurantsList;
    }


    MutableLiveData<List<Restaurant>> restaurantsByHost = new MutableLiveData<List<Restaurant>>();

    public MutableLiveData<List<Restaurant>> getRestaurantsByHost(String hostId) {

        executor.execute(() -> {
            List<Restaurant> rests = AppLocalDb.db.restaurantDao().getRestaurantsByHost(hostId);

            if(rests != null) {
                restaurantsByHost.postValue(rests);
            }
        });

        modelFirebase.getRestaurantsByHost(hostId,(fbRestaurants) -> {
            executor.execute(() -> {
                AppLocalDb.db.restaurantDao().insertMany((List<Restaurant>) fbRestaurants);
                restaurantsByHost.postValue((List<Restaurant>) fbRestaurants);
            });
        });

        return restaurantsByHost;
    }

    public void delete(Restaurant deletedRestaurant, Runnable success) {
        deletedRestaurant.setDeleted(true);
        modelFirebase.setRestaurant(deletedRestaurant, (e) -> {
            executor.execute(() -> {
                AppLocalDb.db.restaurantDao().delete(deletedRestaurant);
                restaurant.postValue(deletedRestaurant);
                mainThread.post(() -> {
                    success.run();
                });
            });
        });
    }


    public void addRestaurant(Restaurant addedRestaurant, Runnable success) {
        modelFirebase.addRestaurant(addedRestaurant, new OnSuccessListener() {
                 @Override
                public void onSuccess(Object o) {
                    executor.execute(() -> {
                        AppLocalDb.db.restaurantDao().insert(addedRestaurant);
                        restaurant.postValue(addedRestaurant);
                        mainThread.post(success);
                    });
                }
        });
//            listener.onSuccess();
//            refreshRestaurantList();
    }

    public void setRestaurant(Restaurant setRestaurant, Runnable success) {
        modelFirebase.setRestaurant(setRestaurant, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                executor.execute(() -> {
                    AppLocalDb.db.restaurantDao().insert(setRestaurant);
                    restaurant.postValue(setRestaurant);
                    mainThread.post(success);
                });
            }
        });
    }

    public interface GetRestaurantById {
        void onComplete(Restaurant restaurant);
    }

    public void getRestaurantById(String restaurantId, GetRestaurantById listener) {
        modelFirebase.getRestaurantById(restaurantId, listener);
    }


    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }

}
