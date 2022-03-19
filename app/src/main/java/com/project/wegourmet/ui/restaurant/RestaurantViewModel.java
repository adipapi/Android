package com.project.wegourmet.ui.restaurant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.wegourmet.Coordinate;
import com.project.wegourmet.Location;
import com.project.wegourmet.R;
import com.project.wegourmet.Restaurant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RestaurantViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Restaurant> restaurant; // TODO: Get somehow?

    public RestaurantViewModel() {
        this.mText = new MutableLiveData<>();
        this.restaurant = new MutableLiveData<>();
        this.restaurant.setValue(new Restaurant("Restaurant 1", new Location("1st Street", "Rishon LeZion", new Coordinate(2.999f, 2.0000f)), R.drawable.bg_img, new ArrayList<String>(Arrays.asList("A", "B")),true));
    }

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<Restaurant> getRestaurant() {
        return restaurant;
    }
}