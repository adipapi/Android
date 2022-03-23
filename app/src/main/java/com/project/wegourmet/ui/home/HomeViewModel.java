package com.project.wegourmet.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.wegourmet.Repository.model.PostModel;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.Restaurant;

import java.util.List;

public class HomeViewModel extends ViewModel {

    public MutableLiveData<List<Restaurant>> restaurants = new MutableLiveData<>();

    public HomeViewModel() {}

    public void getRestaurants() {
        restaurants = RestaurantModel.instance.getAll();
    }
}