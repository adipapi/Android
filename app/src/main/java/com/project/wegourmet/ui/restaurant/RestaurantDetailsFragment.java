package com.project.wegourmet.ui.restaurant;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.model.Restaurant;
import com.squareup.picasso.Picasso;


public class RestaurantDetailsFragment extends Fragment {
    TextView name;
    TextView description;
    ImageView profilePicture;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_details, container, false);

        String rsId = RestaurantDetailsFragmentArgs.fromBundle(getArguments()).getRestaurantName();

        RestaurantModel.instance.getRestaurantById(rsId, new RestaurantModel.GetRestaurantById() {
            @Override
            public void onComplete(Restaurant restaurant) {
                name.setText(restaurant.getName());
                description.setText(restaurant.getId());
                if (restaurant.getMainImageUrl() != null) {
                    Picasso.get().load(restaurant.getMainImageUrl()).into(profilePicture);
                }
            }
        });

        name = view.findViewById(R.id.restaurantName);
        description = view.findViewById(R.id.description);
        profilePicture = view.findViewById(R.id.restaurantProfile);

        return view;
    }
}