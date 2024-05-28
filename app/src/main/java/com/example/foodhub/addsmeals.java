package com.example.foodhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class addsmeals extends AppCompatActivity {
    Intent intent;
    String email;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addsmeals);
        intent=getIntent();
        email=intent.getStringExtra("email");

        bottomNavigationView = findViewById(R.id.bottom_navigation);

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

    // Methods to open respective pages
    private void openHomePage() {

        Intent intent = new Intent(addsmeals.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openCommunityPage() {
        // Implement logic to open Community page
        Intent intent = new Intent(addsmeals.this, community.class);
        startActivity(intent);

    }

    private void openFilterPage() {
        // Implement logic to open Filter page
        Intent intent = new Intent(addsmeals.this, dietplan.class);
        startActivity(intent);
    }

    private void openGroceryListPage() {
        // Implement logic to open Grocery List page
        Intent intent = new Intent(addsmeals.this, Grocery.class);
        startActivity(intent);

    }

    private void openMealPlannerPage() {
        // Implement logic to open Meal Planner page
//        Intent intent = new Intent(addsmeals.this, weekplan.class);
//        intent.putExtra("email",email);
//        startActivity(intent);
//        finish();
    }
}

