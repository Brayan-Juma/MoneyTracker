package com.example.moneytracker.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.moneytracker.R;
import com.example.moneytracker.data.db.DatabaseHelper;

public class SettingsFragment extends Fragment {

    EditText inputName, inputBudget, inputCurrency, inputStartDay;
    Button btnSave, btnUpdateRates, btnReset;

    public SettingsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        inputName = v.findViewById(R.id.inputName);
        inputBudget = v.findViewById(R.id.inputBudget);
        inputCurrency = v.findViewById(R.id.inputCurrency);
        inputStartDay = v.findViewById(R.id.inputStartDay);
        btnSave = v.findViewById(R.id.btnSave);
        btnUpdateRates = v.findViewById(R.id.btnUpdateRates);
        btnReset = v.findViewById(R.id.btnReset);

        loadSettings();

        btnSave.setOnClickListener(view -> saveSettings());
        btnUpdateRates.setOnClickListener(view -> updateRates());
        btnReset.setOnClickListener(view -> confirmReset());

        return v;
    }

    private void loadSettings() {
        SharedPreferences prefs = getContext().getSharedPreferences("settings", getContext().MODE_PRIVATE);

        inputName.setText(prefs.getString("name", ""));
        inputBudget.setText(String.valueOf(prefs.getFloat("budget", 0)));
        inputCurrency.setText(prefs.getString("currency", "USD"));
        inputStartDay.setText(String.valueOf(prefs.getInt("startDay", 1)));
    }

    private void saveSettings() {
        SharedPreferences prefs = getContext().getSharedPreferences("settings", getContext().MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();

        ed.putString("name", inputName.getText().toString());
        ed.putFloat("budget", Float.parseFloat(inputBudget.getText().toString()));
        ed.putString("currency", inputCurrency.getText().toString());
        ed.putInt("startDay", Integer.parseInt(inputStartDay.getText().toString()));

        ed.apply();

        Toast.makeText(getContext(), "Configuración guardada", Toast.LENGTH_SHORT).show();
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

                double rate = new org.json.JSONObject(json.toString())
                        .getJSONObject("rates")
                        .getDouble(currency);

                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(),
                                "1 USD = " + rate + " " + currency,
                                Toast.LENGTH_LONG).show());

            } catch (Exception e) {
                getActivity().runOnUiThread(() ->
                        Toast.makeText(getContext(),
                                "Error al actualizar tasas",
                                Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    private void confirmReset() {
        new android.app.AlertDialog.Builder(getContext())
                .setTitle("Restablecer datos")
                .setMessage("¿Seguro que deseas borrar todo?")
                .setPositiveButton("Sí", (dialog, which) -> resetApp())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void resetApp() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        getContext().deleteDatabase(helper.DB_NAME);

        getContext().getSharedPreferences("settings", getContext().MODE_PRIVATE)
                .edit().clear().apply();

        Toast.makeText(getContext(), "Aplicación restablecida", Toast.LENGTH_SHORT).show();
    }
}
