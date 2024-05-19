package com.example.foodhub;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grocery extends AppCompatActivity {
    private List<String> weeklyMeals;
    private ListView listView;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        title = findViewById(R.id.title);
        listView = findViewById(R.id.list);

        // Initialize weekly meals
        initializeWeeklyMeals();
        generateGroceryList();
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
