package com.example.foodhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class friends extends AppCompatActivity {

    private Object addfriendImageView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);

        // Find views by their IDs
        bottomNavigationView = findViewById(R.id.bottom_navfriend);

        // Set click listeners for the buttons or icons

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    openHome();
                    return true;
                } else if (item.getItemId() == R.id.community) {
                    openCommunity();
                    return true;
                } else if (item.getItemId() == R.id.filter) {
                    openFilter();
                    return true;
                } else if (item.getItemId() == R.id.grocery_list) {
                    openGroceryList();
                    return true;
                } else if (item.getItemId() == R.id.meal_planner) {
                    openMealPlanner();
                    return true;
                }
                return false;
            }

            private void openMealPlanner() {
                Intent intent = new Intent(friends.this, friends.class);
                startActivity(intent);
            }

            private void openGroceryList() {
                Intent intent = new Intent(friends.this, friends.class);
                startActivity(intent);
            }

            private void openFilter() {
                Intent intent = new Intent(friends.this, friends.class);
                startActivity(intent);
            }

            private void openHome() {
                Intent intent = new Intent(friends.this, homepage.class);
                startActivity(intent);

            }

            private void openCommunity() {
                Intent intent = new Intent(friends.this, community.class);
                startActivity(intent);
            }
        });
    }
}
