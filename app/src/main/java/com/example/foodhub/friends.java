package com.example.foodhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class friends extends AppCompatActivity {
    private Intent intent;

    int userId;
    BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        bottomNavigationView = findViewById(R.id.bottom_navcomm);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });
        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.friends);
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
        } else if (item.getItemId() == R.id.friends) {
            openFriendsPage();
            return true;
        }

        return true;
    }


    private void openHomePage() {
        // Already implemented to open CreateProfile page
        Intent intent = new Intent(friends.this, homepage.class);
        intent.putExtra("selected_item_id", R.id.home);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(friends.this, community.class);
        intent.putExtra("selected_item_id", R.id.community);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFriendsPage() {
//        Intent intent = new Intent(friends.this, friends.class);
//        intent.putExtra("selected_item_id", R.id.friends);
//        intent.putExtra("user_id", userId);
//        overridePendingTransition(0, 0);
//        startActivity(intent);
//        finish();
    }


}
