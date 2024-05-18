package com.example.foodhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class weekplan extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekplan);

        sharedPreferences = getSharedPreferences("WeekPlanPrefs", MODE_PRIVATE);

        setupDay("monday");
        setupDay("tuesday");
        setupDay("wednesday");
        setupDay("thursday");
        setupDay("friday");
        setupDay("saturday");
        setupDay("sunday");

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
                } else if (item.getItemId() == R.id.friends) {
                    openFriendsPage();
                    return true;
                }
                return false;
            }

        });
    }

    private void setupDay(final String day) {
        final EditText breakfast = findViewById(getResources().getIdentifier("breakfast_" + day, "id", getPackageName()));
        final EditText lunch = findViewById(getResources().getIdentifier("lunch_" + day, "id", getPackageName()));
        final EditText dinner = findViewById(getResources().getIdentifier("dinner_" + day, "id", getPackageName()));

        Button addBreakfastButton = findViewById(getResources().getIdentifier("add_meal_breakfast_" + day, "id", getPackageName()));
        Button addLunchButton = findViewById(getResources().getIdentifier("add_meal_lunch_" + day, "id", getPackageName()));
        Button addDinnerButton = findViewById(getResources().getIdentifier("add_meal_dinner_" + day, "id", getPackageName()));

        loadMeals(day, breakfast, lunch, dinner);

        addBreakfastButton.setOnClickListener(v -> showAddMealDialog(day, "breakfast", breakfast));
        addLunchButton.setOnClickListener(v -> showAddMealDialog(day, "lunch", lunch));
        addDinnerButton.setOnClickListener(v -> showAddMealDialog(day, "dinner", dinner));
    }

    private void showAddMealDialog(final String day, final String mealType, final EditText mealEditText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.addsmeals, null);
        builder.setView(dialogView);

        final EditText mealInput = dialogView.findViewById(R.id.meal_input);
        Button saveMealButton = dialogView.findViewById(R.id.save_meal_button);

        AlertDialog dialog = builder.create();

        saveMealButton.setOnClickListener(v -> {
            String meal = mealInput.getText().toString();
            if (!meal.isEmpty()) {
                mealEditText.setText(meal);
                saveMeals(day, mealType, meal);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void loadMeals(String day, EditText breakfast, EditText lunch, EditText dinner) {
        String breakfastText = sharedPreferences.getString(day + "_breakfast", "");
        String lunchText = sharedPreferences.getString(day + "_lunch", "");
        String dinnerText = sharedPreferences.getString(day + "_dinner", "");

        breakfast.setText(breakfastText);
        lunch.setText(lunchText);
        dinner.setText(dinnerText);
    }

    private void saveMeals(String day, String mealType, String meal) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(day + "_" + mealType, meal);
        editor.apply();
    }

    // Methods to open respective pages
    private void openHomePage() {
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openCommunityPage() {
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openFilterPage() {
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openGroceryListPage() {
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openFriendsPage() {
        Intent intent = new Intent(weekplan.this, CreateProfile.class);
        startActivity(intent);
    }
}
