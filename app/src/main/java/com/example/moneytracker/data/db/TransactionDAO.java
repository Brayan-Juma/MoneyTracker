package com.example.moneytracker.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.github.mikephil.charting.data.PieEntry;


import com.example.moneytracker.data.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {

    private SQLiteDatabase db;

    public TransactionDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    // INSERT
    public long insert(Transaction t) {
        ContentValues values = new ContentValues();
        values.put("type", t.type);
        values.put("amount", t.amount);
        values.put("category", t.category);
        values.put("description", t.description);
        values.put("date", t.date);
        values.put("payment_method", t.paymentMethod);
        values.put("created_at", t.createdAt);
        return db.insert("transactions", null, values);
    }

    // UPDATE
    public int update(Transaction t) {
        ContentValues values = new ContentValues();
        values.put("type", t.type);
        values.put("amount", t.amount);
        values.put("category", t.category);
        values.put("description", t.description);
        values.put("date", t.date);
        values.put("payment_method", t.paymentMethod);

        return db.update("transactions", values, "id=?", new String[]{String.valueOf(t.id)});
    }

    // DELETE
    public int delete(int id) {
        return db.delete("transactions", "id=?", new String[]{String.valueOf(id)});
    }

    // GET ALL (ORDER BY DATE DESC)
    public List<Transaction> getAll() {
        List<Transaction> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM transactions ORDER BY date DESC", null);

        while (c.moveToNext()) {
            Transaction t = new Transaction();
            t.id = c.getInt(0);
            t.type = c.getString(1);
            t.amount = c.getDouble(2);
            t.category = c.getString(3);
            t.description = c.getString(4);
            t.date = c.getString(5);
            t.paymentMethod = c.getString(6);
            t.createdAt = c.getString(7);
            list.add(t);
        }
        c.close();
        return list;
    }

    // FILTRO POR RANGO DE FECHAS
    public List<Transaction> getByDateRange(String start, String end) {
        List<Transaction> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM transactions WHERE date BETWEEN ? AND ?",
                new String[]{start, end});

        while (c.moveToNext()) {
            Transaction t = new Transaction();
            t.id = c.getInt(0);
            t.type = c.getString(1);
            t.amount = c.getDouble(2);
            list.add(t);
        }
        c.close();
        return list;
    }

    // TOTAL POR TIPO
    public double getTotalByType(String type) {
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM transactions WHERE type=?", new String[]{type});
        if (c.moveToFirst()) return c.getDouble(0);
        return 0;
    }
    public Transaction getById(int id) {
        Cursor c = db.rawQuery("SELECT * FROM transactions WHERE id=?",
                new String[]{String.valueOf(id)});

        if (c.moveToFirst()) {
            Transaction t = new Transaction();
            t.id = c.getInt(0);
            t.type = c.getString(1);
            t.amount = c.getDouble(2);
            t.category = c.getString(3);
            t.description = c.getString(4);
            t.date = c.getString(5);
            t.paymentMethod = c.getString(6);
            t.createdAt = c.getString(7);
            c.close();
            return t;
        }

        c.close();
        return null;
    }
    public List<Transaction> getByType(String type) {
        List<Transaction> list = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM transactions WHERE type=? ORDER BY date DESC",
                new String[]{type});

        while (c.moveToNext()) {
            Transaction t = new Transaction();
            t.id = c.getInt(0);
            t.type = c.getString(1);
            t.amount = c.getDouble(2);
            t.category = c.getString(3);
            t.description = c.getString(4);
            t.date = c.getString(5);
            t.paymentMethod = c.getString(6);
            t.createdAt = c.getString(7);
            list.add(t);
        }
        c.close();
        return list;
    }
    public List<PieEntry> getExpenseByCategory() {

        List<PieEntry> entries = new ArrayList<>();

        Cursor c = db.rawQuery(
                "SELECT category, SUM(amount) FROM transactions WHERE type='EXPENSE' GROUP BY category",
                null
        );

        while (c.moveToNext()) {
            String category = c.getString(0);
            float amount = c.getFloat(1);
            entries.add(new PieEntry(amount, category));
        }

        c.close();
        return entries;
    }
    public double getAverageDailyExpense() {
        Cursor c = db.rawQuery(
                "SELECT AVG(amount) FROM transactions WHERE type='EXPENSE'",
                null
        );

        if (c.moveToFirst()) {
            double avg = c.getDouble(0);
            c.close();
            return avg;
        }

        c.close();
        return 0;
    }


}
