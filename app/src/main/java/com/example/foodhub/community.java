package com.example.foodhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class community extends AppCompatActivity {

    int userId;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        bottomNavigationView = findViewById(R.id.bottom_navcomm);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });
        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.community);
        bottomNavigationView.setSelectedItemId(selectedItemId);
    }
    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
            // Current item is already selected, do nothing
            return false;
        }

        Intent intent;
        if (item.getItemId() == R.id.home) {
            openHomePage();
            return true;
        } else if (item.getItemId() == R.id.friends) {
            openFriendPage();
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

        return true;
    }
    private void openProfilePage() {
        Intent intent = new Intent(community.this, Profile.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openHomePage() {
        // Already implemented to open CreateProfile page
        Intent intent = new Intent(community.this, community.class);
        intent.putExtra("selected_item_id", R.id.home);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFriendPage() {
        Intent intent = new Intent(community.this, friends.class);
        intent.putExtra("selected_item_id", R.id.community);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(community.this, dietplan.class);
        intent.putExtra("selected_item_id", R.id.filter);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {
        // You need to implement this method
        Intent intent = new Intent(community.this, Grocery.class);
        intent.putExtra("selected_item_id", R.id.grocery_list);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        // You need to implement this method
        Intent intent = new Intent(community.this, weekplan.class);
        intent.putExtra("selected_item_id", R.id.meal_planner);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
}
