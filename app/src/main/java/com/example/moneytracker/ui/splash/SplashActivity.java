package com.example.moneytracker.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

import com.example.moneytracker.ui.main.HomeActivity;
import com.example.moneytracker.ui.onboarding.OnboardingActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // INSTALAR SPLASHSCREEN API
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        // Simular pequeña carga (necesario para evitar cierre inmediato)
        try { Thread.sleep(300); } catch (Exception ignored) {}

        boolean done = getSharedPreferences("settings", MODE_PRIVATE)
                .getBoolean("onboarding_complete", false);

        if (done) {
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            startActivity(new Intent(this, OnboardingActivity.class));
        }

        finish();
    }
}
