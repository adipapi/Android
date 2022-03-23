package com.project.wegourmet.ui.restaurant;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.PostModel;
import com.project.wegourmet.Repository.model.RestaurantModel;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.FragmentCreateEditRestaurantBinding;
import com.project.wegourmet.model.Post;
import com.project.wegourmet.model.Restaurant;
import com.project.wegourmet.ui.home.RestaurantTypeEnum;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RestaurantFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CODE_LOAD_IMAGE = 0;
    private FragmentCreateEditRestaurantBinding binding;
    Button saveButton;
    ImageView restaurantImage;
    Bitmap imageBitmap;
    EditText name;
    EditText address;
    EditText phone;
    EditText restaurantType;
    EditText coordinateX;
    EditText coordinateY;
    Spinner restaurantTypeSpinner;
    EditText description;
    ImageButton camBtn;
    ImageButton galleryBtn;
    RecyclerView postsRv;
    PostAdapter adapter;
    List<Post> posts;
    SwipeRefreshLayout swipeRefresh;
    RestaurantViewModel restaurantViewModel;
    private FloatingActionButton addPostBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        restaurantViewModel =
                new ViewModelProvider(this).get(RestaurantViewModel.class);

        binding = FragmentCreateEditRestaurantBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        String restaurantMode = RestaurantFragmentArgs.fromBundle(getArguments()).getRestaurantMode();
        String restaurantId = RestaurantFragmentArgs.fromBundle(getArguments()).getRestaurantId();


        swipeRefresh = root.findViewById(R.id.posts_swiperefresh);
        swipeRefresh.setOnRefreshListener(() -> restaurantViewModel.getPostsByRestaurant(restaurantId));

        saveButton = root.findViewById(R.id.save_restaurant_btn);
        name = root.findViewById(R.id.restaurant_name);
        address = root.findViewById(R.id.restaurant_address);
        phone = root.findViewById(R.id.restaurant_phone);
        restaurantTypeSpinner = root.findViewById(R.id.choose_restaurant_type);
        coordinateX = root.findViewById(R.id.location_x);
        coordinateY = root.findViewById(R.id.location_y);
        restaurantImage = root.findViewById(R.id.restaurant_image);
        camBtn = root.findViewById(R.id.restaurant_cam_btn);
        galleryBtn = root.findViewById(R.id.restaurant_gallery_btn);
        description = root.findViewById(R.id.restaurant_description);
        addPostBtn = (FloatingActionButton) root.findViewById(R.id.restaurant_add_post);

        if(restaurantMode == "VIEW") {
            name.setClickable(false);
            name.setFocusable(false);
            address.setClickable(false);
            address.setFocusable(false);
            phone.setClickable(false);
            phone.setFocusable(false);
            restaurantTypeSpinner.setEnabled(false);
            restaurantTypeSpinner.setFocusable(false);
            coordinateX.setClickable(false);
            coordinateX.setFocusable(false);
            coordinateY.setClickable(false);
            coordinateY.setFocusable(false);
            description.setFocusable(false);
            description.setFocusable(false);
            camBtn.setVisibility(View.INVISIBLE);
            galleryBtn.setVisibility(View.INVISIBLE);
            saveButton.setVisibility(View.INVISIBLE);
        }

        if(restaurantMode != "EDIT") {
            addPostBtn.hide();
        }

        postsRv = root.findViewById(R.id.restaurant_posts_rv);
        postsRv.setHasFixedSize(true);

        postsRv.setLayoutManager(new GridLayoutManager(getContext(), 3));

        adapter = new PostAdapter(posts);
        postsRv.setAdapter(adapter);


        adapter.setOnEditClickListener(new OnPostClickListener() {
            @Override
            public void onItemClick(View v,int position) {
                String postId = posts.get(position).getId();
                if (Navigation.findNavController(getView()).getCurrentDestination().getId() == R.id.navigation_restaurant) {
                    Navigation.findNavController(getView()).navigate(RestaurantFragmentDirections.actionNavigationRestaurantToNavigationPost(postId,restaurantMode, restaurantId));
                }
            }
        });

        if(restaurantMode != "VIEW") {
            adapter.setOnDeleteClickListener(new OnPostClickListener() {
                @Override
                public void onItemClick(View v,int position) {
                    restaurantViewModel.deleteRestaurantPost(posts.get(position), position, () -> {
                        Toast.makeText(getActivity().getApplicationContext(), "Saved successfully",Toast.LENGTH_SHORT).show();
                    });
                    restaurantViewModel.posts.observe(getViewLifecycleOwner(), (updatedPosts) -> {
                        adapter.posts = updatedPosts;
                        adapter.notifyDataSetChanged();
                    });
                }
            });
        }

        if(!restaurantId.isEmpty()) {
            restaurantViewModel.getRestaurantById(restaurantId, (rest -> {
                name.setText(rest.getName());
                address.setText(rest.getAddress());
                phone.setText(rest.getPhone());
                restaurantTypeSpinner.setSelection(RestaurantTypeEnum.valueOf(rest.getType()).ordinal());
                coordinateX.setText(rest.getLocation_x().toString());
                coordinateY.setText(rest.getLocation_y().toString());
                description.setText(rest.getDescription());
                if (rest.getMainImageUrl() != null && !rest.getMainImageUrl().isEmpty()) {
                    Picasso.get()
                            .load(rest.getMainImageUrl())
                            .into(restaurantImage);
                }
            }));

            restaurantViewModel.getPostsByRestaurant(restaurantId);
            restaurantViewModel.posts.observe(getViewLifecycleOwner(), (postsByRestaurants) -> {
                posts = postsByRestaurants;
                adapter.posts = postsByRestaurants;
                adapter.notifyDataSetChanged();
            });

            swipeRefresh.setRefreshing(PostModel.instance.getPostListLoadingState().getValue() == PostModel.PostListLoadingState.loading);
            PostModel.instance.getPostListLoadingState().observe(getViewLifecycleOwner(), postListLoadingState -> {
                if (postListLoadingState == PostModel.PostListLoadingState.loading){
                    swipeRefresh.setRefreshing(true);
                }else{
                    swipeRefresh.setRefreshing(false);
                }

            });
        }

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });

        addPostBtn.setOnClickListener(v -> {
            Navigation.findNavController(getView())
                    .navigate(RestaurantFragmentDirections.actionNavigationRestaurantToNavigationPost("","ADD",restaurantId));
        });


        initspinnerfooter(restaurantTypeSpinner);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(name.getText().toString().isEmpty() || address.getText().toString().isEmpty() ||
                phone.getText().toString().isEmpty() || description.getText().toString().isEmpty() ||
                        coordinateX.getText().toString().isEmpty() || coordinateY.getText().toString().isEmpty()))
                {
                    Restaurant restaurant = new Restaurant(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            name.getText().toString(), address.getText().toString(), phone.getText().toString(),
                            restaurantTypeSpinner.getSelectedItem().toString(), description.getText().toString(),
                            Double.parseDouble(coordinateX.getText().toString()),
                            Double.parseDouble(coordinateY.getText().toString()), false);
                    if (restaurantMode == "EDIT") {
                        restaurant.setId(restaurantViewModel.restaurant.getValue().getId());
                        saveRestaurant(restaurant);
                    } else if (restaurantMode == "ADD") {
                        addRestaurant(restaurant);
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please fill all details",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    private void saveRestaurant(Restaurant restaurant) {
        if (imageBitmap == null) {
            RestaurantModel.instance.setRestaurant(restaurant, () -> {
                Toast.makeText(getActivity().getApplicationContext(), "Saved successfully",Toast.LENGTH_SHORT).show();
            });
        } else {
            RestaurantModel.instance.saveImage(imageBitmap, restaurant.getHostId() +
                    restaurant.getName() + ".jpg", url -> {
                restaurant.setMainImageUrl(url);
                RestaurantModel.instance.setRestaurant(restaurant, () -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
                });
            });
        }
        restaurantViewModel.restaurant.observe(getViewLifecycleOwner(), (updatedRestaurant) -> {
//                    FragmentManager fm = getFragmentManager();
            Toast.makeText(getActivity().getApplicationContext(), "Saved successfully",Toast.LENGTH_SHORT).show();
        });
    }

    private void addRestaurant(Restaurant restaurant) {
        if (imageBitmap == null) {
            restaurantViewModel.addRestaurant(restaurant, () -> {
                Toast.makeText(getActivity().getApplicationContext(), "Saved successfully",Toast.LENGTH_SHORT).show();
            });
        } else {
            RestaurantModel.instance.saveImage(imageBitmap, restaurant.getHostId() +
                    restaurant.getName() + ".jpg", url -> {
                restaurant.setMainImageUrl(url);
                restaurantViewModel.addRestaurant(restaurant, () -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_LOAD_IMAGE);
    }

    private void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
            restaurantImage.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            restaurantImage.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void initspinnerfooter(Spinner dropdown) {
        ArrayList<String> items = new  ArrayList<>();
        for(RestaurantTypeEnum type : RestaurantTypeEnum.values()) {
            items.add(type.toString());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, items);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }
}
