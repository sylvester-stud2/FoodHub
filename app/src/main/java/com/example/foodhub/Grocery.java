package com.example.foodhub;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grocery extends AppCompatActivity {
    
  
    Intent intent;
    String email;
    BottomNavigationView bottomNavigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grocery);
        intent = getIntent();
        email = intent.getStringExtra("email");

       
        bottomNavigationView = findViewById(R.id.bottom_navigation);
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

    private void openProfilePage() {
        Intent intent = new Intent(Grocery.this, Profile.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();

    }

    private void openHomePage() {
        // Already implemented to open CreateProfile page

    }

    private void openCommunityPage() {
        Intent intent = new Intent(Grocery.this,community.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(Grocery.this,dietplan.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();

    }

    private void openGroceryListPage() {
        Intent intent = new Intent(Grocery.this, Grocery.class);
        intent.putExtra("email",email);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(Grocery.this, weekplan.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

 


    }
}
