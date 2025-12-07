package com.example.moneytracker;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.moneytracker.ui.onboarding.OnboardingActivity;
import android.content.Intent;
import android.content.SharedPreferences;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        boolean done = prefs.getBoolean("onboarding_complete", false);

        if (!done) {
            startActivity(new Intent(this, OnboardingActivity.class));
            finish();
        }
    }

}