package com.example.fitfusion;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class onboarding extends AppCompatActivity {

    private EditText ageInput, weightInput, heightInput, goalInput;
    private Button continueButton;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        ageInput = findViewById(R.id.ageInput);
        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);
        goalInput = findViewById(R.id.goal);
        continueButton = findViewById(R.id.continueButton);

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String age = ageInput.getText().toString().trim();
                String weight = weightInput.getText().toString().trim();
                String height = heightInput.getText().toString().trim();
                String goal = goalInput.getText().toString().trim();

                if (age.isEmpty() || weight.isEmpty() || height.isEmpty() || goal.isEmpty()) {
                    Toast.makeText(onboarding.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    saveOnboardingDataToFirestore(age, weight, height, goal);
                }
            }
        });
    }

    private void saveOnboardingDataToFirestore(String age, String weight, String height, String goal) {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in. Please log in again.", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(onboarding.this, dashboard.class); // Replace with your login activity
            startActivity(loginIntent);
            finish();
            return;
        }

        // Get the current user's ID
        String userId = mAuth.getCurrentUser().getUid();

        // Prepare the data to be saved
        Map<String, Object> onboardingData = new HashMap<>();
        onboardingData.put("age", age);
        onboardingData.put("weight", weight);
        onboardingData.put("height", height);
        onboardingData.put("fitnessGoal", goal);

        // Save the data in the Firestore collection
        db.collection("users").document(userId)
                .set(onboardingData) // Use 'set' instead of 'update'
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(onboarding.this, "Details saved successfully", Toast.LENGTH_SHORT).show();

                    // Redirect to the dashboard
                    Intent intent = new Intent(onboarding.this, dashboard.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(onboarding.this, "Failed to save details. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}
