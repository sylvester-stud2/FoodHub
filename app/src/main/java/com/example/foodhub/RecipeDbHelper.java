package com.example.foodhub;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "recipes.db";
    private static final int DATABASE_VERSION = 1;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_RECIPE_TABLE = "CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "ingredients TEXT NOT NULL, " +
                "instructions TEXT NOT NULL" +
                ");";
        db.execSQL(SQL_CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipes");
        onCreate(db);
    }
}
