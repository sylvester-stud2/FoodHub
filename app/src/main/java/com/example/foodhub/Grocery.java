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
    private List<String> weeklyMeals;
    private ListView listView;
    private TextView title;
    Intent intent;
    String email;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);
        intent = getIntent();
        email = intent.getStringExtra("email");

        title = findViewById(R.id.title);
        listView = findViewById(R.id.list);
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

        // Initialize weekly meals
        initializeWeeklyMeals();
        generateGroceryList();
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

    // Initialize weekly meals
    private void initializeWeeklyMeals() {
        weekplan weekPlan = new weekplan();
        weeklyMeals = weekplan.getWeeklyMeals();
    }

    private void generateGroceryList() {
        List<String> groceryItems = new ArrayList<>();

        Map<String, List<String>> mealToIngredientsMap = new HashMap<>();
        mealToIngredientsMap.put("Spaghetti", List.of("Spaghetti", "Tomato Sauce", "Ground Beef", "Garlic"));
        mealToIngredientsMap.put("Tacos", List.of("Tortillas", "Ground Beef", "Lettuce", "Cheese", "Salsa"));
        mealToIngredientsMap.put("Chicken Salad", List.of("Chicken Breast", "Lettuce", "Cucumber", "Tomatoes", "Salad Dressing"));
        // Add mappings for more meals as needed

        for (String meal : weeklyMeals) {
            if (mealToIngredientsMap.containsKey(meal)) {
                groceryItems.addAll(mealToIngredientsMap.get(meal));
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groceryItems);
        listView.setAdapter(adapter);
    }
}
