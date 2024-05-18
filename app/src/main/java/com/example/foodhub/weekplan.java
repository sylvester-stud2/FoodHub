package com.example.foodhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;

public class weekplan extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private HashMap<String, String> mealPlans; // Assume mealPlans contains meal data for the week

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekplan);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        mealPlans = new HashMap<>(); // Initialize your mealPlans here

        // Set listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        openHomePage();
                        return true;
                    case R.id.community:
                        openCommunityPage();
                        return true;
                    case R.id.filter:
                        openFilterPage();
                        return true;
                    case R.id.grocery_list:
                        openGroceryListPage();
                        return true;
                    case R.id.friends:
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
        Intent intent = new Intent(weekplan.this, community.class);
        startActivity(intent);
    }

    private void openFilterPage() {
        Intent intent = new Intent(weekplan.this, dietplan.class);
        startActivity(intent);
    }

    private void openGroceryListPage() {
        Intent intent = new Intent(weekplan.this, Grocery.class);
        intent.putExtra("mealPlans", mealPlans); // Pass meal plans to Grocery activity
        startActivity(intent);
    }

    private void openFriendsPage() {
        Intent intent = new Intent(weekplan.this, Friends.class);
        startActivity(intent);
    }
}
