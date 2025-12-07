package com.example.moneytracker.ui.settings;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moneytracker.R;
import com.example.moneytracker.data.db.DatabaseHelper;

public class SettingsActivity extends AppCompatActivity {

    EditText inputName, inputBudget, inputCurrency, inputStartDay;
    Button btnSave, btnUpdateRates, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        inputName = findViewById(R.id.inputName);
        inputBudget = findViewById(R.id.inputBudget);
        inputCurrency = findViewById(R.id.inputCurrency);
        inputStartDay = findViewById(R.id.inputStartDay);
        btnSave = findViewById(R.id.btnSave);
        btnUpdateRates = findViewById(R.id.btnUpdateRates);
        btnReset = findViewById(R.id.btnReset);

        loadSettings();

        btnSave.setOnClickListener(v -> saveSettings());
        btnUpdateRates.setOnClickListener(v -> updateRates());
        btnReset.setOnClickListener(v -> confirmReset());
    }

    private void loadSettings() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);

        inputName.setText(prefs.getString("name", ""));
        inputBudget.setText(String.valueOf(prefs.getFloat("budget", 0)));
        inputCurrency.setText(prefs.getString("currency", "USD"));
        inputStartDay.setText(String.valueOf(prefs.getInt("startDay", 1)));
    }

    private void saveSettings() {

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();

        ed.putString("name", inputName.getText().toString());
        ed.putFloat("budget", Float.parseFloat(inputBudget.getText().toString()));
        ed.putString("currency", inputCurrency.getText().toString());
        ed.putInt("startDay", Integer.parseInt(inputStartDay.getText().toString()));

        ed.apply();

        Toast.makeText(this, "Configuración guardada", Toast.LENGTH_SHORT).show();
    }

    private void updateRates() {

        String currency = inputCurrency.getText().toString();

        new Thread(() -> {
            try {
                java.net.URL u = new java.net.URL("https://api.exchangerate-api.com/v4/latest/USD");
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) u.openConnection();
                conn.connect();

                java.util.Scanner scan = new java.util.Scanner(conn.getInputStream());
                StringBuilder json = new StringBuilder();
                while (scan.hasNext()) json.append(scan.nextLine());

                org.json.JSONObject obj = new org.json.JSONObject(json.toString());
                double rate = obj.getJSONObject("rates").getDouble(currency);

                runOnUiThread(() -> {
                    Toast.makeText(this, "1 USD = " + rate + " " + currency, Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Error al actualizar tasas", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void confirmReset() {
        new AlertDialog.Builder(this)
                .setTitle("Restablecer datos")
                .setMessage("¿Seguro que deseas borrar todo?")
                .setPositiveButton("Sí", (dialog, which) -> resetApp())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void resetApp() {
        // Borrar base de datos
        DatabaseHelper helper = new DatabaseHelper(this);
        deleteDatabase(helper.DB_NAME);

        // Borrar configuración
        getSharedPreferences("settings", MODE_PRIVATE).edit().clear().apply();

        Toast.makeText(this, "Aplicación restablecida", Toast.LENGTH_SHORT).show();
    }
}
