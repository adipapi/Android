package com.project.wegourmet.ui.profile;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.FragmentProfileBinding;
import com.project.wegourmet.model.User;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;
    ImageView profileImage;
    Bitmap imageBitmap;
    EditText username;
    EditText pass;
    ImageButton camBtn;
    ImageButton galleryBtn;
    Button saveBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
            profileViewModel =
                new ViewModelProvider(this).get(ProfileViewModel.class);

            binding = FragmentProfileBinding.inflate(inflater, container, false);
            View root = binding.getRoot();

            profileImage = root.findViewById(R.id.profileImage);
            username = root.findViewById(R.id.usernameProfile);
            camBtn = root.findViewById(R.id.main_cam_btn);
            galleryBtn = root.findViewById(R.id.main_gallery_btn);
            saveBtn = root.findViewById(R.id.saveProfileBtn);

            profileViewModel.getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid());
            profileViewModel.user.observe(getViewLifecycleOwner(),(user) -> {
                username.setText(user.getUsername().toString());
                if(user.getProfileImageUrl() != null) {
                    Picasso.get().load(user.getProfileImageUrl()).into(profileImage);
                }
            });

            camBtn.setOnClickListener(v -> {
                openCam();
            });

            galleryBtn.setOnClickListener(v -> {
                openGallery();
            });

            saveBtn.setOnClickListener(v -> {
                save();
            });

            return root;
    }

    private void openGallery() {
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
                profileImage.setImageBitmap(imageBitmap);

            }
        }
    }

    private void save() {
//        progressBar.setVisibility(View.VISIBLE);
//        saveBtn.setEnabled(false);
//        cancelBtn.setEnabled(false);
//        camBtn.setEnabled(false);
//        galleryBtn.setEnabled(false);

        String name = username.getText().toString();

        User curUser = profileViewModel.user.getValue();
        curUser.setUsername(name);
        if (imageBitmap == null) {
            profileViewModel.setUser(curUser, () -> {
                Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
            });
        } else {
            UserModel.instance.saveImage(imageBitmap, curUser.getId() + ".jpg", url -> {
                curUser.setProfileImageUrl(url);
                profileViewModel.setUser(curUser, () -> {
                    Toast.makeText(getActivity().getApplicationContext(), "Saved data successfully!",Toast.LENGTH_SHORT).show();
                });
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}