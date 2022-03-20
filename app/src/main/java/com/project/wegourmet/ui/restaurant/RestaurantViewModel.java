package com.project.wegourmet.ui.restaurant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.model.Restaurant;
import com.project.wegourmet.model.User;

public class RestaurantViewModel extends ViewModel {
    public MutableLiveData<Restaurant> restaurant = new MutableLiveData<>();

    public RestaurantViewModel() {}

    public void addRestaurant(Restaurant restaurant, OnFailureListener failureListener) {
        RestaurantModel.instance.addRestaurant(restaurant, failureListener);
    }



//    public LiveData<String> getText() {
//        return mText;
//    }
}