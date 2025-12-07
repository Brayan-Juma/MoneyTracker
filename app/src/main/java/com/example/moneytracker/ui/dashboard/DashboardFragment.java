package com.example.moneytracker.ui.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.moneytracker.R;
import com.example.moneytracker.data.db.TransactionDAO;
import com.example.moneytracker.ui.transactions.TransactionFormActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment {

    TextView txtUserName, txtBalance, txtIncome, txtExpense, txtBudgetStatus;
    ProgressBar progress;

    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // Enlaces con el XML
        txtUserName = v.findViewById(R.id.txtUserName);
        txtBalance = v.findViewById(R.id.txtBalance);
        txtIncome = v.findViewById(R.id.txtIncome);
        txtExpense = v.findViewById(R.id.txtExpense);
        txtBudgetStatus = v.findViewById(R.id.txtBudgetStatus);
        progress = v.findViewById(R.id.progressBudget);

        // Botón flotante para crear transacción
        FloatingActionButton btnAdd = v.findViewById(R.id.btnAddTransaction);
        btnAdd.setOnClickListener(view2 -> {
            Intent i = new Intent(getContext(), TransactionFormActivity.class);
            startActivity(i);
        });

        loadDashboardData();

        return v;
    }

    private void loadDashboardData() {

        SharedPreferences prefs = getContext().getSharedPreferences("settings", getContext().MODE_PRIVATE);

        String name = prefs.getString("name", "Usuario");
        float budget = prefs.getFloat("budget", 0);

        txtUserName.setText("Hola, " + name);

        TransactionDAO dao = new TransactionDAO(getContext());

        double totalIncome = dao.getTotalByType("INCOME");
        double totalExpense = dao.getTotalByType("EXPENSE");
        double balance = totalIncome - totalExpense;

        txtIncome.setText(String.format("$%.2f", totalIncome));
        txtExpense.setText(String.format("$%.2f", totalExpense));
        txtBalance.setText(String.format("$%.2f", balance));

        double percent = budget > 0 ? (totalExpense * 100) / budget : 0;

        progress.setProgress((int) percent);
        txtBudgetStatus.setText(String.format("%.0f%% del presupuesto usado", percent));

        if (percent >= 80)
            txtBudgetStatus.setTextColor(getResources().getColor(R.color.pastel_pink));
        else
            txtBudgetStatus.setTextColor(getResources().getColor(R.color.pastel_text_dark));
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDashboardData();
    }
}
