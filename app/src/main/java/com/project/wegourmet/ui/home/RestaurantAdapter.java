package com.project.wegourmet.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.wegourmet.R;
import com.project.wegourmet.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantViewHolder>{

    public List<Restaurant> restaurants;
    OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    // data is passed into the constructor
    RestaurantAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        RestaurantViewHolder holder = new RestaurantViewHolder(view,listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        Restaurant restaurant = restaurants.get(position);
//        Restaurant restaurant = viewModel.getData().getValue().get(position);
        holder.bind(restaurant);
    }

    @Override
    public int getItemCount() {
        if(restaurants == null){
            return 0;
        }
        return restaurants.size();
    }
}

interface OnItemClickListener{
    void onItemClick(View v,int position);
}

class RestaurantViewHolder extends RecyclerView.ViewHolder{
    ImageView profilePicture;
    TextView name;
    TextView address;

    public RestaurantViewHolder(@NonNull View itemView, OnItemClickListener listener) {
        super(itemView);
        name = itemView.findViewById(R.id.restaurantName);
        address = itemView.findViewById(R.id.restaurantAddress);
        profilePicture = itemView.findViewById(R.id.restaurantPreviewImage);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = getAdapterPosition();
                listener.onItemClick(v,pos);
            }
        });
    }

    void bind(Restaurant restaurant){
        name.setText(restaurant.getName());
        address.setText(restaurant.getId());
        if (restaurant.getMainImageUrl() != null && !restaurant.getMainImageUrl().isEmpty()) {
            Picasso.get()
                    .load(restaurant.getMainImageUrl())
                    .into(profilePicture);
        }
    }
}
