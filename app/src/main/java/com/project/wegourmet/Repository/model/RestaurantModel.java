package com.project.wegourmet.Repository.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.core.os.HandlerCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.project.wegourmet.Repository.modelFirbase.RestaurantModelFirebase;
import com.project.wegourmet.WegourmetApplication;
import com.project.wegourmet.model.Restaurant;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RestaurantModel {
    public static final RestaurantModel instance = new RestaurantModel();
    public Executor executor = Executors.newFixedThreadPool(1);
    public Handler mainThread = HandlerCompat.createAsync(Looper.getMainLooper());
    RestaurantModelFirebase modelFirebase = new RestaurantModelFirebase();

    public enum RestaurantListLoadingState {
        loading,
        loaded
    }

    MutableLiveData<RestaurantListLoadingState> restaurantListLoadingState = new MutableLiveData<RestaurantListLoadingState>();

    public LiveData<RestaurantListLoadingState> getRestaurantListLoadingState() {
        return restaurantListLoadingState;
    }

    private RestaurantModel() {
        restaurantListLoadingState.setValue(RestaurantListLoadingState.loaded);
    }

    MutableLiveData<List<Restaurant>> restaurantsList = new MutableLiveData<List<Restaurant>>();

    public LiveData<List<Restaurant>> getAll() {
        if (restaurantsList.getValue() == null) {
            refreshRestaurantList();
        }
        ;
        return restaurantsList;
    }

    public void refreshRestaurantList() {
        restaurantListLoadingState.setValue(RestaurantListLoadingState.loading);

        // get last local update date
        Long lastUpdateDate = WegourmetApplication.getContext().getSharedPreferences("TAG", Context.MODE_PRIVATE).getLong("RestaurantsLastUpdateDate", 0);

        executor.execute(() -> {
            List<Restaurant> stList = AppLocalDb.db.restaurantDao().getAll();
            restaurantsList.postValue(stList);
        });

        // firebase get all updates since lastLocalUpdateDate
        modelFirebase.getAllRestaurants(lastUpdateDate, new RestaurantModelFirebase.GetAllRestaurantsListener() {
            @Override
            public void onComplete(List<Restaurant> list) {
                // add all records to the local db
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        Long lud = new Long(0);
                        Log.d("TAG", "fb returned " + list.size());
                        for (Restaurant restaurant : list) {
                            AppLocalDb.db.restaurantDao().insertAll(restaurant);
                            if (lud < restaurant.getUpdateDate()) {
                                lud = restaurant.getUpdateDate();
                            }
                        }
                        // update last local update date
                        WegourmetApplication.getContext()
                                .getSharedPreferences("TAG", Context.MODE_PRIVATE)
                                .edit()
                                .putLong("RestaurantsLastUpdateDate", lud)
                                .commit();

                        //return all data to caller
                        List<Restaurant> stList = AppLocalDb.db.restaurantDao().getAll();
                        restaurantsList.postValue(stList);
                        restaurantListLoadingState.postValue(RestaurantListLoadingState.loaded);
                    }
                });
            }
        });
    }


    public interface AddRestaurantListener {
        void onComplete();
    }

    public void addRestaurant(Restaurant restaurant, AddRestaurantListener listener) {
        modelFirebase.addRestaurant(restaurant, () -> {
            listener.onComplete();
            refreshRestaurantList();
        });
    }

    public interface GetRestaurantById {
        void onComplete(Restaurant restaurant);
    }

    public Restaurant getRestaurantById(String restaurantId, GetRestaurantById listener) {
        modelFirebase.getRestaurantById(restaurantId, listener);
        return null;
    }


    public interface SaveImageListener {
        void onComplete(String url);
    }

    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
        modelFirebase.saveImage(imageBitmap, imageName, listener);
    }

    /**
     * Authentication
     */

    public boolean isSignedIn() {
        return modelFirebase.isSignedIn();
    }

//    // Methods
//    MutableLiveData<Restaurant> restaurant = new MutableLiveData<Restaurant>();
//    MutableLiveData<List<Restaurant>> restaurants = new MutableLiveData<List<Restaurant>>();
//
//    public MutableLiveData<List<Restaurant>> getRestaurantsList() {
//            executor.execute(() -> {
//                List<Restaurant> restaurants = AppLocalDb.db.restaurantDao().getAll();
//
//                if(restaurants != null) {
//                    restaurants.postValue(restaurants);
//                }
//            });
//
//            return restaurants;
//    }
//
//    public void addRestaurant(Restaurant newRestaurant, OnFailureListener failureListener) {
//        modelFirebase.addRestaurant(newRestaurant, new OnSuccessListener() {
//            @Override
//            public void onSuccess(Object o) {
//                executor.execute(() -> {
//                    AppLocalDb.db.restaurantDao().insert(newRestaurant);
//                    restaurant.postValue(newRestaurant);
//                    onSuccess(o);
//                });
//            }
//        }, failureListener);
//    }
//
//    public interface SaveImageListener {
//        void onComplete(String url);
//    }
//
//    public void saveImage(Bitmap imageBitmap, String imageName, SaveImageListener listener) {
//        modelFirebase.saveImage(imageBitmap, imageName, listener);
//    }


}
