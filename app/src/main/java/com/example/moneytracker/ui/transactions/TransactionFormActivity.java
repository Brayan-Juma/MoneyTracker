package com.example.moneytracker.ui.transactions;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


import com.example.moneytracker.R;
import com.example.moneytracker.data.db.CategoryDAO;
import com.example.moneytracker.data.db.TransactionDAO;
import com.example.moneytracker.data.models.Transaction;
import com.example.moneytracker.data.models.Category;

import java.text.SimpleDateFormat;
import java.util.*;

public class TransactionFormActivity extends AppCompatActivity {

    Spinner spinnerType, spinnerCategory;
    EditText inputAmount, inputDate, inputPayment;
    Button btnSave, btnConvert;

    TransactionDAO dao;
    CategoryDAO catDao;

    int editingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_form);

        spinnerType = findViewById(R.id.spinnerType);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        inputAmount = findViewById(R.id.inputAmount);
        inputDate = findViewById(R.id.inputDate);
        inputPayment = findViewById(R.id.inputPayment);
        btnSave = findViewById(R.id.btnSave);
        btnConvert = findViewById(R.id.btnConvert);

        dao = new TransactionDAO(this);
        catDao = new CategoryDAO(this);

        setupTypeSpinner();
        setupDate();
        checkIfEditing();

        btnSave.setOnClickListener(v -> saveTransaction());
        btnConvert.setOnClickListener(v -> convertCurrency());
    }

    private void setupTypeSpinner() {
        List<String> types = Arrays.asList("EXPENSE", "INCOME");

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, types);

        spinnerType.setAdapter(adapter);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                loadCategories(types.get(pos));
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void loadCategories(String type) {
        List<Category> cats = catDao.getByType(type);

        List<String> names = new ArrayList<>();
        for (Category c : cats) names.add(c.name);

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, names);

        spinnerCategory.setAdapter(adapter);
    }

    private void setupDate() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        inputDate.setText(today);
    }

    private void checkIfEditing() {
        if (getIntent().hasExtra("id")) {

            editingId = getIntent().getIntExtra("id", -1);

            // Cambiar el título del formulario
            ((TextView)findViewById(R.id.txtTitle)).setText("Editar Transacción");

            // Cargar datos de la transacción desde SQLite
            loadTransaction();
        }
    }


    private void saveTransaction() {

        String amountStr = inputAmount.getText().toString();
        String date = inputDate.getText().toString();

        if (amountStr.isEmpty()) {
            inputAmount.setError("Ingresa un monto");
            return;
        }

        double amount = Double.parseDouble(amountStr);

        if (amount <= 0) {
            inputAmount.setError("El monto debe ser mayor a 0");
            return;
        }

        if (!date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            inputDate.setError("Formato inválido (YYYY-MM-DD)");
            return;
        }

        if (inputPayment.getText().toString().trim().isEmpty()) {
            inputPayment.setError("Ingresa método de pago");
            return;
        }

        // Guardado seguro
        Transaction t = new Transaction();
        t.amount = amount;
        t.type = spinnerType.getSelectedItem().toString();
        t.category = spinnerCategory.getSelectedItem().toString();
        t.paymentMethod = inputPayment.getText().toString();
        t.date = date;
        t.description = "";
        t.createdAt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        if (editingId == -1) dao.insert(t);
        else {
            t.id = editingId;
            dao.update(t);
        }

        Toast.makeText(this, "Guardado correctamente", Toast.LENGTH_SHORT).show();
        finish();
    }


    // API EXCHANGE RATE
    private void convertCurrency() {

        String amountStr = inputAmount.getText().toString();

        if (amountStr.isEmpty()) {
            Toast.makeText(this, "Ingresa un monto primero", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String currency = prefs.getString("currency", "USD");

        String url = "https://api.exchangerate-api.com/v4/latest/USD";

        new Thread(() -> {
            try {
                java.net.URL req = new java.net.URL(url);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) req.openConnection();
                conn.setConnectTimeout(5000);
                conn.setReadTimeout(5000);
                conn.connect();

                int code = conn.getResponseCode();
                if (code != 200) throw new Exception("HTTP " + code);

                Scanner scan = new Scanner(conn.getInputStream());
                StringBuilder json = new StringBuilder();
                while (scan.hasNext()) json.append(scan.nextLine());

                double rate = new org.json.JSONObject(json.toString())
                        .getJSONObject("rates")
                        .getDouble(currency);

                double amount = Double.parseDouble(amountStr);
                double converted = amount * rate;

                runOnUiThread(() -> {
                    inputAmount.setText(String.format("%.2f", converted));
                    Toast.makeText(this,
                            "Convertido usando 1 USD = " + rate + " " + currency,
                            Toast.LENGTH_LONG).show();
                });

            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this,
                            "Error al conectarse. Verifica tu Internet.",
                            Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }

    private void loadTransaction() {
        Transaction t = dao.getById(editingId);

        inputAmount.setText(String.valueOf(t.amount));
        inputPayment.setText(t.paymentMethod);
        inputDate.setText(t.date);

        // Cargar tipo
        spinnerType.setSelection(t.type.equals("EXPENSE") ? 0 : 1);

        // Cargar categorías del tipo
        loadCategories(t.type);

        // Seleccionar categoría
        int pos = ((ArrayAdapter<String>) spinnerCategory.getAdapter())
                .getPosition(t.category);
        spinnerCategory.setSelection(pos);
    }

}
