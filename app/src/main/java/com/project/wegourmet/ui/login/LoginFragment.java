package com.project.wegourmet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.GuestActivity;
import com.project.wegourmet.HostActivity;
import com.project.wegourmet.R;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.FragmentLoginBinding;
import com.project.wegourmet.model.User;

public class LoginFragment extends Fragment{
    private FragmentLoginBinding binding;
    Button loginBtn;
    EditText email;
    EditText pass;
    Button registerBtn;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        LoginViewModel loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        loginBtn = root.findViewById(R.id.loginBtn);
        registerBtn = root.findViewById(R.id.registerFromLogin);
        email = root.findViewById(R.id.emailLogin);
        pass = root.findViewById(R.id.passLogin);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_navigation_login_to_navigation_register);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(email.getText().toString(),
                        pass.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                UserModel.instance.getUserById(authResult.getUser().getUid())
                                        .observe(getViewLifecycleOwner(), (user) -> {
                                            toMainActivity(user);
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity().getApplicationContext(), "Login failed",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        return root;
    }

    private void toMainActivity(User user) {
        Intent intent;
        if(user.isHost()) {
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