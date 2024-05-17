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

public class community extends AppCompatActivity {

        private Object addfriendImageView;
        BottomNavigationView bottomNavigationView;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.community);

            // Find views by their IDs
            //ImageView thumbsUpImageView = findViewById(R.id.thumbsup);
            //ImageView thumbsDownImageView = findViewById(R.id.thumbsdown);
            //ImageView commentImageView = findViewById(R.id.comment);
            //@SuppressLint("WrongViewCast") ImageView addfriendImageView = findViewById(R.id.addfriend);
            bottomNavigationView = findViewById(R.id.bottom_navcomm);

            // Set click listeners for the buttons or icons

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    if (item.getItemId() == R.id.home) {
                        openHome();
                        return true;
                    } else if (item.getItemId() == R.id.friends) {
                        openFriends();
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
                    // Intent intent = new Intent(community.this, friends.class);
                    // startActivity(intent);
                }

                private void openGroceryList() {
                    // Intent intent = new Intent(community.this, friends.class);
                    //startActivity(intent);
                }

                private void openFilter() {
                    //Intent intent = new Intent(community.this, friends.class);
                    // startActivity(intent);
                }

                private void openHome() {
                    Intent intent = new Intent(community.this, homepage.class);
                    startActivity(intent);

                }

                private void openFriends() {
                    Intent intent = new Intent(community.this, friends.class);
                    startActivity(intent);
                }
            });

        }
}