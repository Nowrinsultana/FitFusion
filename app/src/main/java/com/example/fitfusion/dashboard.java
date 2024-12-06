package com.example.fitfusion;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitfusion.databinding.ActivityDashboardBinding;

public class dashboard extends AppCompatActivity {

    ActivityDashboardBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set default fragment
        replaceFragment(new HomeFragment());

        // Set listener for bottom navigation view
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.navigation_home) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.navigation_workout) {
                replaceFragment(new WorkoutFragment());
            } else if (id == R.id.navigation_advice) {
                replaceFragment(new AdviceFragment());
            } else if (id == R.id.navigation_settings) {
                replaceFragment(new SettingsFragment());
            } else {
                return false; // If no case matches
            }

            return true; // Successfully handled
        });

    }

    private void replaceFragment(Fragment fragment) {
        // Use support FragmentManager for transaction
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
