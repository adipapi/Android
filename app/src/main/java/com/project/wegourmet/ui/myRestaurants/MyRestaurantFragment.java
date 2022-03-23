package com.project.wegourmet.ui.myRestaurants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firestore.v1.FirestoreGrpc;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.FragmentMyRestaurantsBinding;
import com.project.wegourmet.model.Restaurant;
import com.project.wegourmet.ui.home.HomeFragmentDirections;
import com.project.wegourmet.ui.home.OnItemClickListener;
import com.project.wegourmet.ui.home.RestaurantAdapter;

import java.util.List;

public class MyRestaurantFragment extends Fragment {
    private List<Restaurant> restaurants;
    private FragmentMyRestaurantsBinding binding;
    private ImageButton addRestaurantBtn;
    ImageButton deleteBtn;
    RestaurantAdapter adapter;
    RecyclerView restaurantsRv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MyRestaurantsViewModel myRestaurantsViewModel =
                new ViewModelProvider(this).get(MyRestaurantsViewModel.class);

        binding = FragmentMyRestaurantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        restaurantsRv = root.findViewById(R.id.myrestaurantlist_rv);
        restaurantsRv.setHasFixedSize(true);

        restaurantsRv.setLayoutManager(new LinearLayoutManager(container.getContext()));

        adapter = new RestaurantAdapter(restaurants);
        restaurantsRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                String rsId = myRestaurantsViewModel.restaurants.getValue().get(position).getId();
                Navigation.findNavController(v).navigate(MyRestaurantFragmentDirections.actionNavigationMyRestaurantsToNavigationRestaurant("EDIT", rsId));
            }
        }, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                myRestaurantsViewModel.deleteRestaurant(restaurants.get(position), position, () -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Saved successfully",Toast.LENGTH_SHORT).show();
                });
                myRestaurantsViewModel.restaurants.observe(getViewLifecycleOwner(), (updatedRests) -> {
                    adapter.restaurants = updatedRests;
                    adapter.notifyDataSetChanged();
                });
            }
        });

        myRestaurantsViewModel.getRestaurantsByHost(FirebaseAuth.getInstance().getCurrentUser().getUid());
        myRestaurantsViewModel.restaurants.observe(getViewLifecycleOwner(), (rests) -> {
            restaurants = rests;
            adapter.restaurants = rests;
            adapter.notifyDataSetChanged();
        });

        addRestaurantBtn = root.findViewById(R.id.add_restaurant_btn);

        addRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle arguments = Bundle();
//                arguments.apply {
//                    putString("CHAT_ID", chat_id);
//                    putInt("CHAT_INDEX", chat_index);
//                }
                Navigation.findNavController(view)
                        .navigate(MyRestaurantFragmentDirections.actionNavigationMyRestaurantsToNavigationRestaurant("ADD", ""));
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
