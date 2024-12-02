package com.example.fitfusion;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // UI Elements
        EditText fullNameInput = findViewById(R.id.full_name_input);
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);
        EditText confirmPasswordInput = findViewById(R.id.confirm_password_input);
        Button registerButton = findViewById(R.id.register_button);
        TextView loginRedirectText = findViewById(R.id.login_redirect_text);

        // Register Button Click Listener
        registerButton.setOnClickListener(v -> {
            String fullName = fullNameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();
            String confirmPassword = confirmPasswordInput.getText().toString().trim();

            if (TextUtils.isEmpty(fullName) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(register.this, "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(register.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create User with Email and Password
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Registration success
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                saveUserToFirestore(fullName, email, user.getUid());
                            }
                        } else {
                            // Registration failed
                            Log.e(TAG, "Registration failed", task.getException());
                            Toast.makeText(register.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Redirect to Login
        loginRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(register.this, login.class);
            startActivity(intent);
            finish();
        });
    }

    private void saveUserToFirestore(String fullName, String email, String userId) {
        // Create user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", fullName);
        userData.put("email", email);

        // Save user data to Firestore
        db.collection("users").document(userId)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "User profile saved successfully.");
                    Toast.makeText(register.this, "Registration successful!", Toast.LENGTH_SHORT).show();

                    // Redirect to Login or Home
                    Intent intent = new Intent(register.this, login.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving user profile", e);
                    Toast.makeText(register.this, "Failed to save user data. Please try again.", Toast.LENGTH_SHORT).show();
                });
    }
}
