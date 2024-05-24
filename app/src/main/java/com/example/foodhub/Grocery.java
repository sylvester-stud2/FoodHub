package com.example.foodhub;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Grocery extends AppCompatActivity {
    
  
    Intent intent;

    int userId;
    private BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grocery);
        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);


       
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });
        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.grocery_list);
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

        return true;
    }

    private void openProfilePage() {
        Intent intent = new Intent(Grocery.this, Profile.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();

    }

    private void openHomePage() {
        // Already implemented to open CreateProfile page
        Intent intent = new Intent(Grocery.this,homepage.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();

    }

    private void openCommunityPage() {
        Intent intent = new Intent(Grocery.this,community.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(Grocery.this,dietplan.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();

    }

    private void openGroceryListPage() {
        Intent intent = new Intent(Grocery.this, Grocery.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(Grocery.this, weekplan.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

 


}

