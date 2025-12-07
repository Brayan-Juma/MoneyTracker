package com.example.moneytracker.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "moneytracker.db";
    public static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Tabla transacciones
        db.execSQL("CREATE TABLE transactions (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "type TEXT NOT NULL," +
                "amount REAL NOT NULL," +
                "category TEXT NOT NULL," +
                "description TEXT," +
                "date TEXT NOT NULL," +
                "payment_method TEXT," +
                "created_at TEXT NOT NULL" +
                ");");

        // Tabla categorías
        db.execSQL("CREATE TABLE categories (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "type TEXT NOT NULL," +
                "icon TEXT," +
                "color TEXT" +
                ");");

        // Insertar categorías por defecto
        insertDefaultCategories(db);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {

        // Gastos
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Alimentación', 'EXPENSE', 'ic_food', '#F7C8E0');");
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Transporte', 'EXPENSE', 'ic_transport', '#A3C9F9');");
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Educación', 'EXPENSE', 'ic_school', '#B8E0D2');");
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Entretenimiento', 'EXPENSE', 'ic_entertainment', '#F7C8E0');");
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Salud', 'EXPENSE', 'ic_health', '#A3C9F9');");
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Otros', 'EXPENSE', 'ic_other', '#CCCCCC');");

        // Ingresos
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Salario', 'INCOME', 'ic_salary', '#B8E0D2');");
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Freelance', 'INCOME', 'ic_freelance', '#A3C9F9');");
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Beca', 'INCOME', 'ic_scholarship', '#F7C8E0');");
        db.execSQL("INSERT INTO categories (name, type, icon, color) VALUES ('Otros', 'INCOME', 'ic_other', '#CCCCCC');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Para futuras migraciones
        db.execSQL("DROP TABLE IF EXISTS transactions");
        db.execSQL("DROP TABLE IF EXISTS categories");
        onCreate(db);
    }
}
