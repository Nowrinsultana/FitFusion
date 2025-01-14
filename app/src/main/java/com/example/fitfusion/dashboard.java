package com.example.fitfusion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fitfusion.databinding.ActivityDashboardBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class dashboard extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private ActivityDashboardBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize view binding
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check if user has completed onboarding
        checkOnboardingStatus();
    }

    private void checkOnboardingStatus() {
        // Get current user ID
        String userId = mAuth.getCurrentUser().getUid();

        // Fetch user document from Firestore
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Check if onboarding fields exist
                        String age = documentSnapshot.getString("age");
                        String weight = documentSnapshot.getString("weight");
                        String height = documentSnapshot.getString("height");
                        String fitnessGoal = documentSnapshot.getString("fitnessGoal");


                        if (age == null || weight == null || height == null || fitnessGoal == null ) {
                            // Missing onboarding data, redirect to onboarding
                            Toast.makeText(this, "Please complete your profile information.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, onboarding.class));
                            finish();
                        } else {
                            // Onboarding data exists, proceed to dashboard
                            Log.d(TAG, "Onboarding data verified.");
                            setupDashboard();
                        }
                    } else {
                        // No user document, redirect to onboarding
                        Toast.makeText(this, "User details not found. Please complete your profile.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, onboarding.class));
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    // Firestore query failed, log and redirect
                    Log.e(TAG, "Error fetching user data", e);
                    Toast.makeText(this, "Error checking profile information. Please try again.", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, onboarding.class));
                    finish();
                });
    }

    private void setupDashboard() {
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
