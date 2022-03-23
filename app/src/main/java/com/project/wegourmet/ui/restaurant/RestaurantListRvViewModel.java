package com.project.wegourmet.ui.restaurant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.model.Restaurant;
import com.project.wegourmet.model.User;

import java.util.List;

public class RestaurantListRvViewModel extends ViewModel {
    LiveData<List<Restaurant>> data;

    public RestaurantListRvViewModel(){
        data = RestaurantModel.instance.getAll();
    }
    public LiveData<List<Restaurant>> getData() {
        return data;
    }
}