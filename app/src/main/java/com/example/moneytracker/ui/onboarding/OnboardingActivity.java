package com.example.moneytracker.ui.onboarding;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneytracker.R;
import com.example.moneytracker.ui.main.HomeActivity;

public class OnboardingActivity extends AppCompatActivity {

    EditText inputName, inputBudget, inputCurrency, inputStartDay;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        inputName = findViewById(R.id.inputName);
        inputBudget = findViewById(R.id.inputBudget);
        inputCurrency = findViewById(R.id.inputCurrency);
        inputStartDay = findViewById(R.id.inputStartDay);
        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(v -> saveSettings());
    }

    private void saveSettings() {

        String name = inputName.getText().toString().trim();
        String budgetStr = inputBudget.getText().toString().trim();
        String currency = inputCurrency.getText().toString().trim();
        String startStr = inputStartDay.getText().toString().trim();

        if (name.isEmpty() || budgetStr.isEmpty() || currency.isEmpty() || startStr.isEmpty()) {
            Toast.makeText(this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        float budget;
        int startDay;

        try {
            budget = Float.parseFloat(budgetStr);
            startDay = Integer.parseInt(startStr);
        } catch (Exception e) {
            Toast.makeText(this, "Presupuesto o Día deben ser números válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (startDay < 1 || startDay > 28) {
            Toast.makeText(this, "El día debe estar entre 1 y 28", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("name", name);
        editor.putFloat("budget", budget);
        editor.putString("currency", currency);
        editor.putInt("startDay", startDay);
        editor.putBoolean("onboarding_complete", true);
        editor.apply();

        // Ir al dashboard
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }
}
