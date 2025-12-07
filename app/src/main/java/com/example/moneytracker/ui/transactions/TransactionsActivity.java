package com.example.moneytracker.ui.transactions;


import android.os.Bundle;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.view.View;

import com.example.moneytracker.R;
import com.example.moneytracker.data.db.TransactionDAO;
import com.example.moneytracker.data.models.Transaction;

import java.util.List;


public class TransactionsActivity extends AppCompatActivity {

    RecyclerView recycler;
    TransactionAdapter adapter;
    List<Transaction> list;
    TransactionDAO dao;
    TextView filterAll, filterIncome, filterExpense;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);

        filterAll = findViewById(R.id.filterAll);
        filterIncome = findViewById(R.id.filterIncome);
        filterExpense = findViewById(R.id.filterExpense);

        filterAll.setOnClickListener(v -> applyFilter("ALL"));
        filterIncome.setOnClickListener(v -> applyFilter("INCOME"));
        filterExpense.setOnClickListener(v -> applyFilter("EXPENSE"));


        recycler = findViewById(R.id.recyclerTransactions);

        dao = new TransactionDAO(this);
        list = dao.getAll(); // Ordenado por fecha descendente

        adapter = new TransactionAdapter(list, this::editTransaction, this);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        enableSwipeToDelete();
    }

    private void applyFilter(String type) {

        list.clear();

        if (type.equals("ALL")) {
            list.addAll(dao.getAll());
        } else {
            list.addAll(dao.getByType(type));
        }

        adapter.notifyDataSetChanged();
    }


    private void editTransaction(Transaction t) {
        Intent i = new Intent(this, TransactionFormActivity.class);
        i.putExtra("id", t.id);
        startActivity(i);
    }
    @Override
    protected void onResume() {
        super.onResume();
        list.clear();
        list.addAll(dao.getAll());
        adapter.notifyDataSetChanged();
    }


    private void enableSwipeToDelete() {

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
                0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView rv, RecyclerView.ViewHolder vh, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder vh, int direction) {
                int pos = vh.getAdapterPosition();
                int id = list.get(pos).id;

                dao.delete(id);
                list.remove(pos);
                adapter.notifyItemRemoved(pos);
            }
        };

        new ItemTouchHelper(callback).attachToRecyclerView(recycler);
    }
}
