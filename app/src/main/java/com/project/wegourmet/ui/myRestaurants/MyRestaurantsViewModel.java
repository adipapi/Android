package com.project.wegourmet.ui.myRestaurants;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.wegourmet.Repository.model.PostModel;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.Restaurant;

import java.util.List;

public class MyRestaurantsViewModel extends ViewModel {

    public MutableLiveData<List<Restaurant>> restaurants = new MutableLiveData<>();

    public MyRestaurantsViewModel() {}

    public void getRestaurantsByHost(String hostId) {
        restaurants = RestaurantModel.instance.getRestaurantsByHost(hostId);
    }

    public void deleteRestaurant(Restaurant restaurant, Integer pos, Runnable success) {
        RestaurantModel.instance.delete(restaurant, () -> {
            List<Restaurant> updated = restaurants.getValue();
            updated.remove(restaurant);
            restaurants.postValue(updated);
        });
    }
}