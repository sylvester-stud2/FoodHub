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
            ImageView thumbsUpImageView = findViewById(R.id.thumbsup);
            ImageView thumbsDownImageView = findViewById(R.id.thumbsdown);
            ImageView commentImageView = findViewById(R.id.comment);
            @SuppressLint("WrongViewCast") ImageView addfriendImageView = findViewById(R.id.addfriend);
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
                    Intent intent = new Intent(community.this, friends.class);
                    startActivity(intent);
                }

                private void openGroceryList() {
                    Intent intent = new Intent(community.this, friends.class);
                    startActivity(intent);
                }

                private void openFilter() {
                    Intent intent = new Intent(community.this, friends.class);
                    startActivity(intent);
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



            thumbsUpImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle thumbs up icon click
                    onThumbsUpClicked();
                }
            });

            thumbsDownImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle thumbs down icon click
                    onThumbsDownClicked();
                }
            });

            commentImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Handle comment icon click
                    onCommentClicked();
                }
            });
            
            addfriendImageView.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v){
                    onAddFriendClicked();
                }
            });

            // Add more click listeners for other buttons or icons as needed
        }
        

        public void onAddFriendClicked() {
        }

        // Method to handle thumbs up icon click
        public void onThumbsUpClicked() {
            // Implement your logic here
            // For example, update the thumbs up count or perform some other action
        }

        // Method to handle thumbs down icon click
        public void onThumbsDownClicked() {
            // Implement your logic here
            // For example, update the thumbs down count or perform some other action
        }

        // Method to handle comment icon click
        public void onCommentClicked() {
            // Implement your logic here
            // For example, open a comment activity or perform some other action
        }
}