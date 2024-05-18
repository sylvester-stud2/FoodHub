package com.example.foodhub;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Grocery extends AppCompatActivity {

    private Spinner daySpinner;
    private EditText mealEditText;
    private Button addMealButton;
    private Button generateGroceryButton;
    private ListView mealsListView;
    private TextView groceryListTextView;
    private BottomNavigationView bottomNavigationView;

    private HashMap<String, List<String>> mealPlan;
    private ArrayAdapter<String> mealsAdapter;
    private List<String> meals;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        daySpinner = findViewById(R.id.day_spinner);
        mealEditText = findViewById(R.id.meal_edit_text);
        addMealButton = findViewById(R.id.add_meal_button);
        generateGroceryButton = findViewById(R.id.generate_button);
        mealsListView = findViewById(R.id.meals_list_view);
        groceryListTextView = findViewById(R.id.grocery_list_text_view);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        mealPlan = new HashMap<>();
        meals = new ArrayList<>();
        mealsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, meals);
        mealsListView.setAdapter(mealsAdapter);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        // Receive meal plans from intent
        Intent intent = getIntent();
        HashMap<String, String> mealPlans = (HashMap<String, String>) intent.getSerializableExtra("mealPlans");
        if (mealPlans != null) {
            for (String day : mealPlans.keySet()) {
                List<String> mealsForDay = new ArrayList<>();
                mealsForDay.add(mealPlans.get(day));
                mealPlan.put(day, mealsForDay);
            }
        }

        daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String day = parent.getItemAtPosition(position).toString();
                updateMealsList(day);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        addMealButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = daySpinner.getSelectedItem().toString();
                String meal = mealEditText.getText().toString();

                if (!meal.isEmpty()) {
                    if (!mealPlan.containsKey(day)) {
                        mealPlan.put(day, new ArrayList<String>());
                    }
                    mealPlan.get(day).add(meal);
                    mealEditText.setText("");
                    updateMealsList(day);
                } else {
                    Toast.makeText(Grocery.this, "Please enter a meal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        generateGroceryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateGroceryList();
            }
        });

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
                        // We are already in Grocery activity
                        return true;
                    case R.id.friends:
                        openFriendsPage();
                        return true;
                }
                return false;
            }
        });
    }

    private void updateMealsList(String day) {
        meals.clear();
        if (mealPlan.containsKey(day)) {
            meals.addAll(mealPlan.get(day));
        }
        mealsAdapter.notifyDataSetChanged();
    }

    private void generateGroceryList() {
        HashMap<String, Integer> groceryList = new HashMap<>();
        for (List<String> dailyMeals : mealPlan.values()) {
            for (String meal : dailyMeals) {
                String[] ingredients = meal.split(", ");
                for (String ingredient : ingredients) {
                    if (groceryList.containsKey(ingredient)) {
                        groceryList.put(ingredient, groceryList.get(ingredient) + 1);
                    } else {
                        groceryList.put(ingredient, 1);
                    }
                }
            }
        }
        StringBuilder groceryListText = new StringBuilder();
        for (String item : groceryList.keySet()) {
            groceryListText.append(item).append(": ").append(groceryList.get(item)).append("\n");
        }
        groceryListTextView.setText(groceryListText.toString());
    }

    // Methods to open respective pages
    private void openHomePage() {
        Intent intent = new Intent(Grocery.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openCommunityPage() {
        Intent intent = new Intent(Grocery.this, Community.class);
        startActivity(intent);
    }

    private void openFilterPage() {
        Intent intent = new Intent(Grocery.this, Filter.class);
        startActivity(intent);
    }

    private void openFriendsPage() {
        Intent intent = new Intent(Grocery.this, Friends.class);
        startActivity(intent);
    }
}
