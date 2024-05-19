package com.example.foodhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class weekplan extends AppCompatActivity {
    Intent intent;
    String email;


    BottomNavigationView bottomNavigationView;
    private static List<String> weeklyMeals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekplan);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        intent = getIntent();
        email = intent.getStringExtra("email");

        // Initialize the weekly meals
        initializeWeeklyMeals();
        // Set listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    openHomePage();
                    return true;
                } else if (item.getItemId() == R.id.community) {
                    openCommunityPage();
                    return true;
                } else if (item.getItemId() == R.id.filter) {
                    openFilterPage();
                    return true;
                } else if (item.getItemId() == R.id.grocery_list) {
                    openGroceryListPage();
                    return true;
                } else if (item.getItemId() == R.id.meal_planner) {
                    openMealPlannerPage();
                    return true;
                }
                return false;
            }

        });

    }

    // Initialize weekly meals
    private void initializeWeeklyMeals() {
        weeklyMeals = new ArrayList<>();
        weeklyMeals.add("Spaghetti");
        weeklyMeals.add("Tacos");
        weeklyMeals.add("Chicken Salad");
        // Add more meals as needed
    }

    public static List<String> getWeeklyMeals() {
        return weeklyMeals;
    }

    // Methods to open respective pages
    private void openHomePage() {

        Intent intent = new Intent(weekplan.this, homepage.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        // Implement logic to open Community page
//        Intent intent = new Intent(weekplan.this, community.class);
//        intent.putExtra("email", email);
//        overridePendingTransition(0, 0);
//        startActivity(intent);
//        finish();
    }

    private void openFilterPage() {
        // Implement logic to open Filter page
        Intent intent = new Intent(weekplan.this, dietplan.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {

        // Implement logic to open Grocery List page
//        Intent intent = new Intent(weekplan.this, homepage.class);
//        intent.putExtra("email", email);
//        overridePendingTransition(0, 0);
//        startActivity(intent);
//        finish();
    }

    private void openMealPlannerPage() {
        // Implement logic to open Meal Planner page
        Intent intent = new Intent(weekplan.this, weekplan.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
}

