package com.project.wegourmet.ui.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.FragmentRestaurantBinding;
import com.project.wegourmet.model.Restaurant;

public class RestaurantFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private FragmentRestaurantBinding binding;
    Button saveButton;
    ImageView restaurantImage;
    RadioGroup radioGroup;
    Button loginBtn;
    Bitmap imageBitmap;
    EditText name;
    EditText address;
    EditText phone;
    EditText restaurantType;
    ImageButton camBtn;
    ImageButton galleryBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RestaurantViewModel restaurantViewModel =
                new ViewModelProvider(this).get(RestaurantViewModel.class);

        binding = FragmentRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textDashboard;
//        dashboardViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        saveButton = root.findViewById(R.id.save_restaurant_btn);
        name = root.findViewById(R.id.restaurant_name);
        address = root.findViewById(R.id.restaurant_address);
        phone = root.findViewById(R.id.restaurant_phone);
        restaurantType = root.findViewById(R.id.restaurant_type);
        restaurantImage = root.findViewById(R.id.restaurant_image);
        camBtn = root.findViewById(R.id.restaurant_cam_btn);
//        galleryBtn = root.findViewById(R.id.register_gallery_btn);

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Restaurant restaurant = new Restaurant(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        name.getText().toString(), address.getText().toString(), phone.getText().toString(),
                        restaurantType.getText().toString());
                if (imageBitmap == null) {
                    restaurantViewModel.addRestaurant(restaurant, (e) -> {
                        Toast.makeText(getActivity().getApplicationContext(), "Saved successfully",Toast.LENGTH_SHORT).show();
                    });
                } else {
                    RestaurantModel.instance.saveImage(imageBitmap, restaurant.getHostId() +
                            restaurant.getName() + ".jpg", url -> {
                        restaurant.setMainImageUrl(url);
                        restaurantViewModel.addRestaurant(restaurant, (e) -> {
                            Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
                        });
                    });
                }
                restaurantViewModel.restaurant.observe(getViewLifecycleOwner(), (updatedRestaurant) -> {
//                    FragmentManager fm = getFragmentManager();
                    Toast.makeText(getActivity().getApplicationContext(), "Saved successfully",Toast.LENGTH_SHORT).show();
                });
            }
        });

        return root;
    }

    private void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA){
            if (resultCode == Activity.RESULT_OK){
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                restaurantImage.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
