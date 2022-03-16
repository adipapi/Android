package com.project.wegourmet.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.wegourmet.R;
import com.project.wegourmet.Restaurant;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder> {

    private final ArrayList<Restaurant> restaurants;

    public RestaurantListAdapter() {
        this.restaurants = new ArrayList<Restaurant>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position); // TODO: Make sure final here doesn't screw things up

        // Set restaurant name
        holder.getRestaurantNameTextView().setText(restaurant.getRestaurantName());
        // Set restaurant city
        holder.getRestaurantCityTextView().setText(restaurant.getLocation().getCityName());
        // Set restaurant preview image
        // TODO: Make it take the image from the Restaurant object instead of being hardcoded as bg_img
        holder.getPreviewImageView().setImageResource(restaurant.getRestaurantImage());
    }
    public void updateRestaurantList(ArrayList<Restaurant> restaurants) {
        this.restaurants.clear();
        this.restaurants.addAll(restaurants);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return this.restaurants.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView restaurantNameTextView;
        private final TextView restaurantCityTextView;
        private final ImageView previewImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantNameTextView = itemView.findViewById(R.id.restaurantName);
            restaurantCityTextView = itemView.findViewById(R.id.restaurantCity);
            previewImageView = itemView.findViewById(R.id.restaurantPreviewImage);
        }

        public TextView getRestaurantNameTextView() {
            return restaurantNameTextView;
        }

        public TextView getRestaurantCityTextView() {
            return restaurantCityTextView;
        }

        public ImageView getPreviewImageView() {
            return previewImageView;
        }
    }
}
