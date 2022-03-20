package com.project.wegourmet.ui.register;

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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.GuestActivity;
import com.project.wegourmet.HostActivity;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.FragmentRegisterBinding;
import com.project.wegourmet.model.User;

public class RegisterFragment extends Fragment {
    private static final int REQUEST_CAMERA = 1;
    private FragmentRegisterBinding binding;
    private boolean isHost;
    ImageView profileImage;
    RadioGroup radioGroup;
    Button loginBtn;
    Bitmap imageBitmap;
    EditText email;
    EditText pass;
    EditText username;
    Button registerBtn;
    ImageButton camBtn;
    ImageButton galleryBtn;
    RadioButton guestRadio;
    RadioButton hostRadio;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RegisterViewModel registerViewModel =
                new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        radioGroup = (RadioGroup) root .findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.radio_guest:
                        isHost = false;
                        break;
                    case R.id.radio_host:
                        isHost = true;
                        break;
                }
            }
        });

        registerBtn = root.findViewById(R.id.register);
        email = root.findViewById(R.id.emailRegister);
        pass = root.findViewById(R.id.passRegister);
        username = root.findViewById(R.id.usernameRegister);
        guestRadio = root.findViewById(R.id.radio_guest);
        hostRadio = root.findViewById(R.id.radio_host);
        profileImage = root.findViewById(R.id.registerProfileImage);
        camBtn = root.findViewById(R.id.register_cam_btn);
        galleryBtn = root.findViewById(R.id.register_gallery_btn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(email.getText().toString().isEmpty() || username.getText().toString().isEmpty() ||
                            pass.getText().toString().isEmpty() ||
                            (!guestRadio.isChecked() && !hostRadio.isChecked())) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Please fill all the required fields",Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email.getText().toString(),
                                pass.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        save();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
            };
        });

        camBtn.setOnClickListener(v -> {
            openCam();
        });

        galleryBtn.setOnClickListener(v -> {
                openGallery();
        });

        return root;
    }

    private void openGallery() {
    }

    private void openCam() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,REQUEST_CAMERA);
    }

    private void save() {
        User user;
        String id = FirebaseAuth.getInstance().getUid().toString();
        user = new User(id, username.getText().toString(), isHost);
        if (imageBitmap == null) {
            saveUser(user);
        } else {
            UserModel.instance.saveImage(imageBitmap, user.getId() + ".jpg", url -> {
                user.setProfileImageUrl(url);
                saveUser(user);
            });
        }
    }

    private void saveUser(User user) {
        UserModel.instance.addUser(user, (e) -> {
            toMainActivity();
        }, (e) -> {
            Toast.makeText(getActivity().getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
        });
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

    private void toMainActivity() {
        Intent intent;
        if(isHost) {
            intent = new Intent(getContext(), HostActivity.class);
        } else {
            intent = new Intent(getContext(), GuestActivity.class);
        }

        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
