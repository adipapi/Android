package com.project.wegourmet.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.project.wegourmet.MapActivity;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.databinding.FragmentHomeBinding;
import com.project.wegourmet.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

private FragmentHomeBinding binding;

    private List<Restaurant> restaurants;
    RestaurantAdapter adapter;
    RecyclerView restaurantsRv;
    ImageButton mapViewBtn;
    SearchView searchBar;
    SwipeRefreshLayout swipeRefresh;

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

        swipeRefresh = root.findViewById(R.id.restaurants_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> homeViewModel.getRestaurants());

        searchBar = root.findViewById(R.id.search_bar);

        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                adapter.getFilter().filter(text);
                return true;
            }
        });

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                String rsId = homeViewModel.restaurants.getValue().get(position).getId();
                Navigation.findNavController(v).navigate(HomeFragmentDirections.actionNavigationHomeToNavigationRestaurant("VIEW",rsId));
            }
        }, null);
        Spinner dropdown;
        dropdown = root.findViewById(R.id.restaurant_type_spinner);
        initspinnerfooter(dropdown);

        homeViewModel.getRestaurants();
        homeViewModel.restaurants.observe(getViewLifecycleOwner(), (rests) -> {
            restaurants = rests;
            adapter.restaurants = rests;
            adapter.originalRestaurants = rests;
            adapter.restaurantTypeSpinner = dropdown;
            adapter.notifyDataSetChanged();
        });

        swipeRefresh.setRefreshing(RestaurantModel.instance.getRestaurantListLoadingState().getValue() == RestaurantModel.RestaurantListLoadingState.loading);
        RestaurantModel.instance.getRestaurantListLoadingState().observe(getViewLifecycleOwner(), restaurantListLoadingState -> {
            if (restaurantListLoadingState == RestaurantModel.RestaurantListLoadingState.loading){
                swipeRefresh.setRefreshing(true);
            }else{
                swipeRefresh.setRefreshing(false);
            }

        });

        mapViewBtn = root.findViewById(R.id.view_in_map);

        mapViewBtn.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), MapActivity.class);
            startActivity(intent);
            getActivity().finish();
        });


        return root;
    }

private void initspinnerfooter(Spinner dropdown) {
    ArrayList<String> items = new  ArrayList<>();
    items.add("All");
    for(RestaurantTypeEnum type : RestaurantTypeEnum.values()) {
        items.add(type.toString());
    }

    ArrayAdapter<String> dropdownAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
    dropdown.setAdapter(dropdownAdapter);
    dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            adapter.getFilter().filter(searchBar.getQuery().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // TODO Auto-generated method stub
        }
    });
}

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}