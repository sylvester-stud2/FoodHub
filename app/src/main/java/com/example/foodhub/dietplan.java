package com.example.foodhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class dietplan extends AppCompatActivity {
    Intent intent;
    String email;
    private SharedPreferences sharedPreferences;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dietItems;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_plan);
        intent = getIntent();
        email = intent.getStringExtra("email");

        sharedPreferences = getSharedPreferences("DietPlanPrefs", MODE_PRIVATE);
        dietItems = new ArrayList<>(sharedPreferences.getStringSet("dietItems", new HashSet<>()));

        ListView dietItemsList = findViewById(R.id.diet_items_list);
        EditText dietItemInput = findViewById(R.id.diet_item_input);
        Button addButton = findViewById(R.id.add_button);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dietItems);
        dietItemsList.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            String item = dietItemInput.getText().toString().trim();
            if (!item.isEmpty()) {
                dietItems.add(item);
                adapter.notifyDataSetChanged();
                saveDietItems();
                dietItemInput.setText("");
            }
        });
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
    private void saveDietItems() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>(dietItems);
        editor.putStringSet("dietItems", set);
        editor.apply();
    }
    // Methods to open respective pages
    private void openHomePage() {

        Intent intent = new Intent(dietplan.this, homepage.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        // Implement logic to open Community page
        Intent intent = new Intent(dietplan.this, homepage.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        // Implement logic to open Filter page
//        Intent intent = new Intent(dietplan.this, home.class);
//        intent.putExtra("email", email);
//        startActivity(intent);
//        finish();
    }

    private void openGroceryListPage() {
        // Implement logic to open Grocery List page
//        Intent intent = new Intent(dietplan.this, CreateProfile.class);
//        startActivity(intent);
    }

    private void openFriendsPage() {
        // Implement logic to open Meal Planner page
        Intent intent = new Intent(dietplan.this, friends.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
}