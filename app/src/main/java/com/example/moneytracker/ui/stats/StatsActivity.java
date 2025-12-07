package com.example.moneytracker.ui.stats;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.example.moneytracker.R;
import com.example.moneytracker.data.db.TransactionDAO;

import java.util.ArrayList;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    PieChart chart;
    TextView txtTotal, txtPromedio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        chart = findViewById(R.id.chartCategories);
        txtTotal = findViewById(R.id.txtTotal);
        txtPromedio = findViewById(R.id.txtPromedio);

        loadStats();
    }

    private void loadStats() {

        TransactionDAO dao = new TransactionDAO(this);

        double totalExpense = dao.getTotalByType("EXPENSE");
        double averageDaily = dao.getAverageDailyExpense();
        List<PieEntry> entries = dao.getExpenseByCategory();  // función que creamos abajo

        txtTotal.setText("Total gastado: $" + String.format("%.2f", totalExpense));
        txtPromedio.setText("Promedio diario: $" + String.format("%.2f", averageDaily));

        setupPieChart(entries);
    }

    private void setupPieChart(List<PieEntry> entries) {

        PieDataSet set = new PieDataSet(entries, "");
        set.setSliceSpace(3f);
        set.setSelectionShift(6f);

        // Colores pastel
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getColor(R.color.pastel_pink));
        colors.add(getColor(R.color.pastel_blue));
        colors.add(getColor(R.color.pastel_green));
        colors.add(Color.LTGRAY);
        colors.add(Color.YELLOW);

        set.setColors(colors);

        PieData data = new PieData(set);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.DKGRAY);

        chart.setData(data);
        chart.setUsePercentValues(true);
        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.TRANSPARENT);
        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(45f);

        chart.getDescription().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(getColor(R.color.pastel_text_dark));

        chart.invalidate(); // refrescar
    }
}
