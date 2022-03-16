package com.project.wegourmet;

public class Restaurant {
    private final String restaurantName;
    private final Location location;
    private int restaurantImage;

    public Restaurant(String restaurantName, Location location, int restaurantImage) {
        this.restaurantName = restaurantName;
        this.location = location;
        this.restaurantImage = restaurantImage;
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
}
