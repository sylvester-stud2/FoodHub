package com.example.foodhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class weekplan extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_plan);

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
                } else if (item.getItemId() == R.id.friends) {
                    openFriendsPage();
                    return true;
                }
                return false;
            }
        });

    }

    // Methods to open respective pages
    private void openHomePage() {

        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openCommunityPage() {
        // Implement logic to open Community page
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openFilterPage() {
        // Implement logic to open Filter page
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openGroceryListPage() {
        // Implement logic to open Grocery List page
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openFriendsPage() {
        // Implement logic to open Meal Planner page
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }
}

