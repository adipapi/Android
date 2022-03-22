package com.project.wegourmet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.project.wegourmet.Repository.model.UserModel;
import com.project.wegourmet.databinding.ActivityUnsignedBinding;

public class UnsignedActivity extends AppCompatActivity {

    private ActivityUnsignedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUnsignedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            UserModel.instance.getUserById(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(this, (user) -> {
                Intent intent;

                if(user.isHost()) {
                    intent = new Intent(getApplicationContext(), HostActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), GuestActivity.class);
                }

                startActivity(intent);
            });
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_favorites, R.id.navigation_notifications)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}


