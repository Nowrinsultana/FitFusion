package com.example.fitfusion;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Display GIF in ImageView
        ImageView gifImageView = findViewById(R.id.gifImageView);
        Glide.with(this).load(R.drawable.splash).into(gifImageView);

        // Delay for 3 seconds, then go to Login Activity
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(splash.this, login.class);
            startActivity(intent);
            finish();  // Close Splash Activity
        }, 3000); // 3000 ms delay (3 seconds)
    }
}
