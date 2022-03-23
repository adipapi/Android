package com.project.wegourmet.ui.restaurant;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.project.wegourmet.Repository.model.PostModel;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.Restaurant;

import java.util.List;

public class RestaurantViewModel extends ViewModel {
    public MutableLiveData<Restaurant> restaurant = new MutableLiveData<>();
    public MutableLiveData<List<Post>> posts = new MutableLiveData<>();

    public RestaurantViewModel() {}

    public void addRestaurant(Restaurant restaurant, Runnable success) {
        RestaurantModel.instance.addRestaurant(restaurant, success);
    }

    public void getRestaurantById(String restId, RestaurantModel.GetRestaurantById success) {
        RestaurantModel.instance.getRestaurantById(restId, success);
    }

    public void deleteRestaurantPost(Post post, Integer pos, Runnable success) {
        PostModel.instance.delete(post, () -> {
            List<Post> updated = posts.getValue();
            updated.remove(post);
            posts.postValue(updated);
        });
    }

    public void getPostsByRestaurant(String restaurantName) {
        posts = PostModel.instance.getPostsByRestaurant(restaurantName);
    }
}