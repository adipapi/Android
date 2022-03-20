package com.project.wegourmet.ui.myRestaurants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.project.wegourmet.R;
import com.project.wegourmet.databinding.FragmentMyRestaurantsBinding;
import com.project.wegourmet.ui.dashboard.DashboardViewModel;

public class MyRestaurantFragment extends Fragment {
    private FragmentMyRestaurantsBinding binding;
    private ImageButton addRestaurantBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        DashboardViewModel dashboardViewModel =
//                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentMyRestaurantsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        addRestaurantBtn = root.findViewById(R.id.add_restaurant_btn);

        addRestaurantBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle arguments = Bundle();
//                arguments.apply {
//                    putString("CHAT_ID", chat_id);
//                    putInt("CHAT_INDEX", chat_index);
//                }
                Navigation.findNavController(view).navigate(R.id.action_navigation_my_restaurants_to_navigation_restaurant);
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
