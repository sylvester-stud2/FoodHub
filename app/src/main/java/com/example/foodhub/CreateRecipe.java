package com.example.foodhub;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class CreateRecipe extends AppCompatActivity {
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        RecipeDbHelper dbHelper = new RecipeDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
    }
}
