package com.project.wegourmet.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.wegourmet.R;
import com.project.wegourmet.databinding.FragmentHomeBinding;
import com.project.wegourmet.model.Restaurant;
import com.project.wegourmet.ui.restaurant.RestaurantListRvFragment;

import java.util.List;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;

    private List<Restaurant> restaurants;
    RestaurantAdapter adapter;
    RecyclerView restaurantsRv;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        restaurantsRv = root.findViewById(R.id.restaurantlist_rv);
        restaurantsRv.setHasFixedSize(true);

        restaurantsRv.setLayoutManager(new LinearLayoutManager(container.getContext()));

        adapter = new RestaurantAdapter(restaurants);
        restaurantsRv.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                String rsId = homeViewModel.restaurants.getValue().get(position).getId();
                Navigation.findNavController(v).navigate(HomeFragmentDirections.actionNavigationHomeToNavigationRestaurantDetails(rsId));
            }
        });

        homeViewModel.getRestaurants();
        homeViewModel.restaurants.observe(getViewLifecycleOwner(), (rests) -> {
            restaurants = rests;
            adapter.restaurants = rests;
            adapter.notifyDataSetChanged();
        });

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}