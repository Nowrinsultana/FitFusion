package com.example.fitfusion;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Set up UI elements
        EditText emailInput = findViewById(R.id.email_input);
        EditText passwordInput = findViewById(R.id.password_input);
        Button loginButton = findViewById(R.id.login_button);
        TextView registerText = findViewById(R.id.register_text);
        LinearLayout signInButton = findViewById(R.id.sign_in_button);

        // Set up Google Sign-In
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(false)
                                .build())
                .build();

        // Handle Google Sign-In button click
        signInButton.setOnClickListener(v -> signInWithGoogle());

        // Handle email login
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            handleLoginSuccess(mAuth.getCurrentUser());
                        } else {
                            Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                            Log.e(TAG, "Email login failed", task.getException());
                        }
                    });
        });

        // Redirect to Register
        registerText.setOnClickListener(v -> startActivity(new Intent(this, register.class)));
    }

    private void signInWithGoogle() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(result.getPendingIntent().getIntentSender(), 1001, null, 0, 0, 0);
                    } catch (Exception e) {
                        Log.e(TAG, "Error starting One Tap sign-in", e);
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "One Tap sign-in failed", e);
                    Toast.makeText(this, "Google sign-in failed", Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            try {
                SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
                String idToken = credential.getGoogleIdToken();
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken);
                }
            } catch (Exception e) {
                Log.e(TAG, "Error retrieving credential", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        handleLoginSuccess(mAuth.getCurrentUser());
                    } else {
                        Log.e(TAG, "Sign-in with credential failed", task.getException());
                        Toast.makeText(this, "Authentication Failed", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void handleLoginSuccess(FirebaseUser user) {
        if (user != null) {
            String userId = user.getUid();
            db.collection("users").document(userId).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fullName = documentSnapshot.getString("fullName");
                            Toast.makeText(this, "Welcome, " + (fullName != null ? fullName : "User"), Toast.LENGTH_SHORT).show();
                        } else {
                            saveNewUser(user);
                        }
                        redirectToDashboard();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Error fetching user data from Firestore", e);
                        Toast.makeText(this, "Failed to fetch user details.", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void saveNewUser(FirebaseUser user) {
        String userId = user.getUid();
        String fullName = user.getDisplayName();
        String email = user.getEmail();

        Map<String, Object> userData = new HashMap<>();
        userData.put("fullName", fullName != null ? fullName : "User");
        userData.put("email", email);

        db.collection("users").document(userId).set(userData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User data saved successfully."))
                .addOnFailureListener(e -> Log.e(TAG, "Error saving user data", e));
    }

    private void redirectToDashboard() {
        startActivity(new Intent(this, dashboard.class));
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(this, dashboard.class));
            finish();
        }
    }
}
