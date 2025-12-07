package com.example.moneytracker.ui.transactions;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.moneytracker.R;
import com.example.moneytracker.data.db.TransactionDAO;
import com.example.moneytracker.data.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionsFragment extends Fragment {

    RecyclerView recycler;
    TransactionAdapter adapter;
    List<Transaction> list;
    TransactionDAO dao;

    Button btnAll, btnIncome, btnExpense;

    public TransactionsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_transactions, container, false);

        // -------------------------
        // REFERENCIAS DEL XML
        // -------------------------
        recycler = v.findViewById(R.id.recyclerTransactions);

        btnAll = v.findViewById(R.id.btnAll);
        btnIncome = v.findViewById(R.id.btnIncome);
        btnExpense = v.findViewById(R.id.btnExpense);

        // -------------------------
        // CONFIGURACIÓN DEL DAO Y LISTA
        // -------------------------
        dao = new TransactionDAO(getContext());
        list = new ArrayList<>(dao.getAll());

        adapter = new TransactionAdapter(list, t -> editTransaction(t), getContext());

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        enableSwipeToDelete();

        // -------------------------
        // LISTENERS DE LOS FILTROS
        // -------------------------
        btnAll.setOnClickListener(view -> loadAll());
        btnIncome.setOnClickListener(view -> loadIncome());
        btnExpense.setOnClickListener(view -> loadExpense());

        return v;
    }

    // -----------------------------
    // ACTUALIZAR LISTA AL VOLVER
    // -----------------------------
    @Override
    public void onResume() {
        super.onResume();
        loadAll(); // Actualiza por defecto
    }

    // -----------------------------
    // MÉTODOS DE FILTRO
    // -----------------------------

    private void loadAll() {
        list.clear();
        list.addAll(dao.getAll());
        adapter.notifyDataSetChanged();
    }

    private void loadIncome() {
        list.clear();
        list.addAll(dao.getByType("INCOME"));
        adapter.notifyDataSetChanged();
    }

    private void loadExpense() {
        list.clear();
        list.addAll(dao.getByType("EXPENSE"));
        adapter.notifyDataSetChanged();
    }

    // -----------------------------
    // EDITAR TRANSACCIÓN
    // -----------------------------
    private void editTransaction(Transaction t) {
        Intent i = new Intent(getContext(), TransactionFormActivity.class);
        i.putExtra("id", t.id);
        startActivity(i);
    }

    // -----------------------------
    // ELIMINAR CON SWIPE
    // -----------------------------
    private void enableSwipeToDelete() {
        ItemTouchHelper.SimpleCallback callback =
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView,
                                          RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
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
