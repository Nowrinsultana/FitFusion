package com.example.fitfusion;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomeFragment extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextView ageTextView, weightTextView, heightTextView, goalTextView, bodyTypeTextView, bmiTextView, healthConditionTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize TextViews
        ageTextView = view.findViewById(R.id.ageTextView);
        weightTextView = view.findViewById(R.id.weightTextView);
        heightTextView = view.findViewById(R.id.heightTextView);
        goalTextView = view.findViewById(R.id.goalTextView);
        bodyTypeTextView = view.findViewById(R.id.bodyTypeTextView);
        bmiTextView = view.findViewById(R.id.bmiTextView);
        healthConditionTextView = view.findViewById(R.id.healthConditionTextView);

        // Fetch and display user data
        fetchUserData();

        return view;
    }

    private void fetchUserData() {
        String userId = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Retrieve data
                        String age = documentSnapshot.getString("age");
                        String weight = documentSnapshot.getString("weight");
                        String height = documentSnapshot.getString("height");
                        String goal = documentSnapshot.getString("fitnessGoal");
                        String bodyType = documentSnapshot.getString("bodyType");

                        // Display data
                        ageTextView.setText("Age: " + age);
                        weightTextView.setText("Weight: " + weight + " kg");
                        heightTextView.setText("Height: " + height + " cm");
                        goalTextView.setText("Goal: " + goal);
                        bodyTypeTextView.setText("Body Type: " + bodyType);

                        // Calculate and display BMI and health condition
                        if (weight != null && height != null) {
                            calculateBMI(weight, height);
                        }
                    } else {
                        Toast.makeText(getContext(), "No user data found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to fetch data", Toast.LENGTH_SHORT).show();
                    Log.e("HomeFragment", "Error fetching data", e);
                });
    }

    private void calculateBMI(String weight, String height) {
        try {
            // Convert weight and height to numeric values
            double weightInKg = Double.parseDouble(weight);
            double heightInCm = Double.parseDouble(height);
            double heightInMeters = heightInCm / 100.0;

            // Calculate BMI
            double bmi = weightInKg / (heightInMeters * heightInMeters);
            bmiTextView.setText(String.format("BMI: %.2f", bmi));

            // Determine health condition
            String healthCondition;
            if (bmi < 18.5) {
                healthCondition = "Underweight";
            } else if (bmi >= 18.5 && bmi < 24.9) {
                healthCondition = "Normal weight";
            } else if (bmi >= 25 && bmi < 29.9) {
                healthCondition = "Overweight";
            } else {
                healthCondition = "Obese";
            }
            healthConditionTextView.setText("Health Condition: " + healthCondition);

        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Invalid weight or height format", Toast.LENGTH_SHORT).show();
            Log.e("HomeFragment", "Error parsing weight or height", e);
        }
    }
}
