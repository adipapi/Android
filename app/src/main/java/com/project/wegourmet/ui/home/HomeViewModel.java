package com.project.wegourmet.ui.home;

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

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<ArrayList<Restaurant>> restaurants; // TODO: Get from firebase

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        restaurants = new MutableLiveData<>();
        restaurants.setValue(new ArrayList<Restaurant>(Arrays.asList(
                new Restaurant("Restaurant 1", new Location("1st Street", "Rishon LeZion", new Coordinate(2.999f, 2.0000f)), R.drawable.bg_img, new ArrayList<String>(Arrays.asList("A", "B")),true),
                new Restaurant("Restaurant 2", new Location("2nd Street", "Holon", new Coordinate(1.999f, 2.0000f)), R.drawable.hamburger, new ArrayList<String>(Arrays.asList("C", "B")),true),
                new Restaurant("Restaurant 3", new Location("Nice Avenue", "Tel Avivo", new Coordinate(4.2069f, 1.0323f)), R.drawable.bg_img, new ArrayList<String>(Arrays.asList("D", "B")),false),
                new Restaurant("Restaurant 4", new Location("KKK Avenue", "Tel Avivo", new Coordinate(4.2069f, 1.0323f)), R.drawable.hamburger, new ArrayList<String>(Arrays.asList("A", "B")),false)
        )));
        mText.setValue("This is home fragment");
    }

    public MutableLiveData<ArrayList<Restaurant>> getRestaurants() {
        return restaurants;
    }

    public LiveData<String> getText() {
        return mText;
    }
}