package com.project.wegourmet;

import android.media.Rating;

import java.util.List;

public class Restaurant {
    private final String restaurantName;
    private final Location location;
    private final int restaurantImage;
    private final List<String> tags;
    private final boolean isKosher;


    public Restaurant(String restaurantName, Location location, int restaurantImage, List<String> tags, boolean isKosher) {
        this.restaurantName = restaurantName;
        this.location = location;
        this.restaurantImage = restaurantImage;
        this.tags = tags;
        this.isKosher = isKosher;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public Location getLocation() {
        return location;
    }

    public int getRestaurantImage() {
        return restaurantImage;
    }

    public List<String> getTags() {
        return tags;
    }

    public boolean isKosher() {
        return isKosher;
    }
}
