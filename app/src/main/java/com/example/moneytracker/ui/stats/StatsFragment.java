package com.example.moneytracker.ui.stats;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import android.graphics.Color;
import android.widget.TextView;

import com.example.moneytracker.R;
import com.example.moneytracker.data.db.TransactionDAO;

import java.util.ArrayList;
import java.util.List;

public class StatsFragment extends Fragment {

    PieChart chart;
    TextView txtTotal, txtPromedio;

    public StatsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_stats, container, false);

        chart = v.findViewById(R.id.chartCategories);
        txtTotal = v.findViewById(R.id.txtTotal);
        txtPromedio = v.findViewById(R.id.txtPromedio);

        loadStats();

        return v;
    }

    private void loadStats() {

        TransactionDAO dao = new TransactionDAO(getContext());

        double totalExpense = dao.getTotalByType("EXPENSE");
        double averageDaily = dao.getAverageDailyExpense();
        List<PieEntry> entries = dao.getExpenseByCategory();

        txtTotal.setText("Total gastado: $" + String.format("%.2f", totalExpense));
        txtPromedio.setText("Promedio diario: $" + String.format("%.2f", averageDaily));

        setupPieChart(entries);
    }

    private void setupPieChart(List<PieEntry> entries) {

        PieDataSet set = new PieDataSet(entries, "");
        set.setSliceSpace(3f);
        set.setSelectionShift(6f);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getContext().getColor(R.color.pastel_pink));
        colors.add(getContext().getColor(R.color.pastel_blue));
        colors.add(getContext().getColor(R.color.pastel_green));
        colors.add(Color.LTGRAY);

        set.setColors(colors);

        PieData data = new PieData(set);
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.DKGRAY);

        chart.setData(data);
        chart.setUsePercentValues(true);
        chart.setDrawHoleEnabled(true);

        chart.getDescription().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(getContext().getColor(R.color.pastel_text_dark));

        chart.invalidate();
    }
}
