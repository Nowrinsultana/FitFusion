package com.example.fitfusion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.fitfusion.databinding.ActivityDashboardBinding;

public class dashboard extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private ActivityDashboardBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);

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
                        String bodyType = documentSnapshot.getString("bodyType");

                        if (age == null || weight == null || height == null || fitnessGoal == null || bodyType == null) {
                            // Missing onboarding data, redirect to onboarding
                            Toast.makeText(this, "Please complete your profile information.", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this, onboarding.class));
                            finish();
                        } else {
                            // Onboarding data exists, proceed to dashboard
                            Log.d(TAG, "Onboarding data verified.");
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
}
