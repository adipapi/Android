package com.project.wegourmet.ui.restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.model.Restaurant;
import com.squareup.picasso.Picasso;

public class RestaurantListRvFragment {
  RestaurantListRvViewModel viewModel;
    MyAdapter adapter;
    SwipeRefreshLayout swipeRefresh;

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        viewModel = new ViewModelProvider(this).get(RestaurantListRvViewModel.class);
//    }

    @Nullable
//    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);

        swipeRefresh = view.findViewById(R.id.restaurantlist_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> RestaurantModel.instance.refreshRestaurantList());

        RecyclerView list = view.findViewById(R.id.restaurantlist_rv);
        list.setHasFixedSize(true);

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                String stId = viewModel.getData().getValue().get(position).getId();
                Navigation.findNavController(v).navigate(RestaurantListRvFragmentDirections.actionRestaurantListRvFragmentToRestaurantDetailsFragment(stId));

            }
        });

        setHasOptionsMenu(true);
        viewModel.getData().observe(getViewLifecycleOwner(), list1 -> refresh());
        swipeRefresh.setRefreshing(RestaurantModel.instance.getRestaurantListLoadingState().getValue() == RestaurantModel.RestaurantListLoadingState.loading);
        RestaurantModel.instance.getRestaurantListLoadingState().observe(getViewLifecycleOwner(), restaurantListLoadingState -> {
            if (studentListLoadingState == RestaurantModel.RestaurantListLoadingState.loading){
                swipeRefresh.setRefreshing(true);
            }else{
                swipeRefresh.setRefreshing(false);
            }

        });
        return view;
    }

    private Context getContext() {
    }

    private void refresh() {
        adapter.notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView profilePicture;
        TextView name;
        TextView description;

        public MyViewHolder(@NonNull View itemView, OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurantName);
            description = itemView.findViewById(R.id.description);
            profilePicture = itemView.findViewById(R.id.restaurantProfile);

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
            description.setText(restaurant.getId());
            if (restaurant.getMainImageUrl() != null) {
                Picasso.get()
                        .load(restaurant.getMainImageUrl())
                        .into(profilePicture);
            }
        }
    }

    interface OnItemClickListener{
        void onItemClick(View v,int position);
    }
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{

        OnItemClickListener listener;
        public void setOnItemClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.list_item,parent,false);
            MyViewHolder holder = new MyViewHolder(view,listener);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Restaurant restaurant = viewModel.getData().getValue().get(position);
            holder.bind(restaurant);
        }

        @Override
        public int getItemCount() {
            if(viewModel.getData().getValue() == null){
                return 0;
            }
            return viewModel.getData().getValue().size();
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.restaurant_list_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.addRestaurantFragment){
            Log.d("TAG","ADD...");
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}