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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.PostModel;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.FragmentHistoryBinding;
import com.project.wegourmet.databinding.FragmentPostBinding;
import com.project.wegourmet.model.Post;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class PostFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_CODE_LOAD_IMAGE = 0;
    ImageView postImage;
    EditText postText;
    Button savePostBtn;
    PostViewModel postViewModel;
    Bitmap imageBitmap;
    ImageButton camBtn;
    ImageButton galleryBtn;
    private FragmentPostBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        postViewModel =
                new ViewModelProvider(this).get(PostViewModel.class);

        binding = FragmentPostBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        savePostBtn = root.findViewById(R.id.savePostBtn);
        camBtn = root.findViewById(R.id.post_cam_btn);
        galleryBtn = root.findViewById(R.id.post_gallery_btn);
        postImage = root.findViewById(R.id.post_image);
        postText = root.findViewById(R.id.post_text);

        String postId = PostFragmentArgs.fromBundle(getArguments()).getPostId();
        String postMode = PostFragmentArgs.fromBundle(getArguments()).getPostMode();

        if(postId != null && !postId.isEmpty()) {
            postViewModel.getPostById(postId);
            postViewModel.post.observe(getViewLifecycleOwner(), (postById) -> {
                Picasso.get().load(postById.getImageUrl()).into(postImage);
                postText.setText(postById.getText());
            });
        }

       if(postMode == "VIEW") {
           savePostBtn.setVisibility(View.INVISIBLE);
           camBtn.setVisibility(View.INVISIBLE);
           galleryBtn.setVisibility(View.INVISIBLE);
           postText.setFocusable(false);
           postText.setFocusable(false);
       }

        savePostBtn.setOnClickListener(v -> {
            save();
        });

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
            openGallery();
        });


        return root;
    }

    private void save() {
        String postMode = PostFragmentArgs.fromBundle(getArguments()).getPostMode();

        String text = postText.getText().toString();
        Post curPost = postViewModel.post.getValue();
        if(postMode == "VIEW") {
            return;
        } else if(postMode == "EDIT") {
            curPost.setText(text);
            if (imageBitmap == null) {
                postViewModel.setPost(curPost, () -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
                });
            } else {
                PostModel.instance.saveImage(imageBitmap, curPost.getId() + ".jpg", url -> {
                    curPost.setImageUrl(url);
                    postViewModel.setPost(curPost, () -> {
                        Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
                    });
                });
            }
        } else {
            String restaurantId = PostFragmentArgs.fromBundle(getArguments()).getRestaurantId();
            Post newPost = new Post(restaurantId, text, false);
            if (imageBitmap == null) {
                PostModel.instance.addPost(newPost, (e) -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
                }, (e) -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Save fail!",Toast.LENGTH_SHORT).show();
                });
            } else {
                PostModel.instance.saveImage(imageBitmap, new BigInteger(256, new Random()) + ".jpg", url -> {
                    newPost.setImageUrl(url);
                    PostModel.instance.addPost(newPost, (e) -> {
                        Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
                    }, (e) -> {
                        Toast.makeText(getActivity().getApplicationContext(), "Save fail!",Toast.LENGTH_SHORT).show();
                    });
                });
            }
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
            postImage.setImageBitmap(imageBitmap);
        } else if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            postImage.setImageBitmap(imageBitmap);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
