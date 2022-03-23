package com.project.wegourmet.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.wegourmet.R;
import com.project.wegourmet.model.Restaurant;
import com.squareup.picasso.Picasso;

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
        holder.getRestaurantNameTextView().setText(restaurant.getName());
        // Set restaurant city
        holder.getRestaurantCityTextView().setText(restaurant.getAddress());
        // Set restaurant preview image
        Picasso.get().load(restaurant.getMainImageUrl()).into(holder.previewImageView);

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
        private final TextView restaurantAddressTextView;
        private final ImageView previewImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            restaurantNameTextView = itemView.findViewById(R.id.restaurantName);
            restaurantAddressTextView = itemView.findViewById(R.id.restaurantAddress);
            previewImageView = itemView.findViewById(R.id.restaurantPreviewImage);

        }

        public TextView getRestaurantNameTextView() {
            return restaurantNameTextView;
        }

        public TextView getRestaurantCityTextView() {
            return restaurantAddressTextView;
        }

        public ImageView getPreviewImageView() {
            return previewImageView;
        }
    }
}