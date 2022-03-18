package com.project.wegourmet.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.MainActivity;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.FragmentLoginBinding;
import com.project.wegourmet.databinding.FragmentRegisterBinding;
import com.project.wegourmet.model.User;
import com.project.wegourmet.ui.login.LoginViewModel;

public class RegisterFragment extends Fragment {
    private FragmentRegisterBinding binding;
    private boolean isHost;
    RadioGroup radioGroup;
    Button loginBtn;
    EditText email;
    EditText pass;
    EditText username;
    Button registerBtn;
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
                        // switch to fragment 1
                        break;
                    case R.id.radio_host:
                        isHost = true;
                        // Fragment 2
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
                                        User user;
                                        String id = FirebaseAuth.getInstance().getUid().toString();
                                        user = new User(id, username.getText().toString(), isHost);
                                        UserModel.instance.addUser(user, (e) -> {
                                            toMainActivity();
                                        }, (e) -> {
                                            Toast.makeText(getActivity().getApplicationContext(), "Register failed", Toast.LENGTH_SHORT).show();
                                        });
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

        return root;
    }

    private void toMainActivity() {
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
