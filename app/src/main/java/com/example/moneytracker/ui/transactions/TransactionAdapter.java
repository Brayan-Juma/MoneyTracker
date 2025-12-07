package com.example.moneytracker.ui.transactions;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moneytracker.R;
import com.example.moneytracker.data.models.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> list;
    private OnTransactionClick listener;
    private Context context;

    public interface OnTransactionClick {
        void onClick(Transaction t);
    }

    public TransactionAdapter(List<Transaction> list, OnTransactionClick listener, Context ctx) {
        this.list = list;
        this.listener = listener;
        this.context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transaction_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {

        Transaction t = list.get(pos);

        h.txtCategory.setText(t.category);
        h.txtDescription.setText(t.description);
        h.txtAmount.setText(String.format("$%.2f", t.amount));

        // Color por tipo
        if (t.type.equals("EXPENSE"))
            h.txtAmount.setTextColor(context.getResources().getColor(R.color.pastel_pink));
        else
            h.txtAmount.setTextColor(context.getResources().getColor(R.color.pastel_green));

        // Click editar
        h.itemView.setOnClickListener(v -> listener.onClick(t));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtDescription, txtAmount;
        ImageView imgCategory;

        public ViewHolder(@NonNull View v) {
            super(v);

            txtCategory = v.findViewById(R.id.txtCategory);
            txtDescription = v.findViewById(R.id.txtDescription);
            txtAmount = v.findViewById(R.id.txtAmount);
            imgCategory = v.findViewById(R.id.imgCategory);
        }
    }

}
