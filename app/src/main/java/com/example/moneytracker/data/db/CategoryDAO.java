package com.example.moneytracker.data.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.moneytracker.data.models.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    private SQLiteDatabase db;

    public CategoryDAO(Context context) {
        DatabaseHelper helper = new DatabaseHelper(context);
        db = helper.getWritableDatabase();
    }

    public List<Category> getByType(String type) {
        List<Category> categories = new ArrayList<>();

        Cursor c = db.rawQuery(
                "SELECT * FROM categories WHERE type=?",
                new String[]{type});

        while (c.moveToNext()) {
            Category cat = new Category();
            cat.id = c.getInt(0);
            cat.name = c.getString(1);
            cat.type = c.getString(2);
            cat.icon = c.getString(3);
            cat.color = c.getString(4);
            categories.add(cat);
        }
        c.close();
        return categories;
    }
}
