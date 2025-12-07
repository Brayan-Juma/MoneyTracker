package com.example.moneytracker.ui.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moneytracker.R;
import com.example.moneytracker.data.db.TransactionDAO;

public class DashboardActivity extends AppCompatActivity {

    TextView txtUserName, txtBalance, txtIncome, txtExpense, txtBudgetStatus;
    ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        txtUserName = findViewById(R.id.txtUserName);
        txtBalance = findViewById(R.id.txtBalance);
        txtIncome = findViewById(R.id.txtIncome);
        txtExpense = findViewById(R.id.txtExpense);
        txtBudgetStatus = findViewById(R.id.txtBudgetStatus);
        progress = findViewById(R.id.progressBudget);

        loadDashboardData();
    }

    private void loadDashboardData() {

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);

        String name = prefs.getString("name", "Usuario");
        float budget = prefs.getFloat("budget", 0);

        txtUserName.setText("Hola, " + name);

        TransactionDAO dao = new TransactionDAO(this);

        double totalIncome = dao.getTotalByType("INCOME");
        double totalExpense = dao.getTotalByType("EXPENSE");
        double balance = totalIncome - totalExpense;

        txtIncome.setText(String.format("$%.2f", totalIncome));
        txtExpense.setText(String.format("$%.2f", totalExpense));
        txtBalance.setText(String.format("$%.2f", balance));

        // Porcentaje presupuesto
        double percent = 0;

        if (budget > 0) {
            percent = (totalExpense * 100) / budget;
        }

        progress.setProgress((int) percent);

        txtBudgetStatus.setText(String.format("%.0f%% del presupuesto usado", percent));

        // ALERTA si supera 80%
        if (percent >= 80 && percent < 100) {
            txtBudgetStatus.setTextColor(getResources().getColor(R.color.pastel_pink));
        } else if (percent >= 100) {
            txtBudgetStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        }
    }
}
