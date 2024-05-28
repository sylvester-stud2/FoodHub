package com.example.foodhub;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class dietplan extends AppCompatActivity {

    private static final String PREFS_NAME = "DietPlanPrefs";
    private static final String SELECTED_INGREDIENTS_KEY = "SelectedIngredients";

    String postURL = "https://lamp.ms.wits.ac.za/mc/Dietplan.php";

    Intent intent;
    int userId;

    private List<String> selectedIngredients;
    private BottomNavigationView bottomNavigationView;
    private List<LinearLayout> linearLayouts;

    private static final String[] INGREDIENTS = {
            "Apples", "Avocado", "Bacon", "Baking powder", "Banana", "Beans", "Beef", "Beetroot",
            "Berries", "Bread", "Broccoli", "Butter", "Butternut", "Carrot", "Cereal", "Cheese",
            "Chicken", "Cinnamon", "Cream", "Eggs", "Fish", "Flour", "Garlic", "Honey", "Lettuce",
            "Lemon", "Maize meal", "Mayonnaise", "Milk", "Mushrooms", "Oats", "Oil", "Onion",
            "Parsley", "Pasta", "Pepper", "Potatoes", "Rice", "Salt", "Soup powder", "Spinach",
            "Steak", "Sugar", "Tomato sauce", "Tomato", "Vanilla", "Vinegar", "Water", "Yeast",
            "Yogurt", "Stock (beef/chicken)"
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_plan);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSelections();
                saveSelectedIngredients();
                handleUnselectedIngredients();
            }
        });

        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        selectedIngredients = new ArrayList<>();
        linearLayouts = new ArrayList<>();

        for (int i = 1; i <= 51; i++) {
            String linearLayoutId = "linearLayout" + i;
            int resID = getResources().getIdentifier(linearLayoutId, "id", getPackageName());
            linearLayouts.add(findViewById(resID));
        }

        for (int i = 0; i < INGREDIENTS.length; i++) {
            String checkBoxId = "vegan_checkbox" + (i + 1);
            int resID = getResources().getIdentifier(checkBoxId, "id", getPackageName());
            setupCheckBox(resID, INGREDIENTS[i]);
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });
        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.filter);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        loadSelectedIngredients();
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {

            return false;
        }

        Intent intent;
        if (item.getItemId() == R.id.home) {
            openHomePage();
            return true;
        } else if (item.getItemId() == R.id.community) {
            openCommunityPage();
            return true;
        } else if (item.getItemId() == R.id.grocery_list) {
            openGroceryListPage();
            return true;
        } else if (item.getItemId() == R.id.meal_planner) {
            openMealPlannerPage();
            return true;
        }

        return true;
    }

    private void setupCheckBox(int checkBoxId, String ingredient) {
        CheckBox checkBox = findViewById(checkBoxId);
        if (checkBox != null) {
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        selectedIngredients.add(ingredient);
                    } else {
                        selectedIngredients.remove(ingredient);
                    }
                }
            });
        } else {
            Log.e("SetupCheckBox", "CheckBox not found for ID: " + checkBoxId);
        }
    }

    public void saveSelections() {
        selectedIngredients.clear();

        for (LinearLayout linearLayout : linearLayouts) {
            if (linearLayout != null) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View child = linearLayout.getChildAt(i);
                    if (child instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) child;
                        if (checkBox.isChecked()) {
                            TextView textView = (TextView) linearLayout.getChildAt(i + 1);
                            selectedIngredients.add(textView.getText().toString());
                        }
                    }
                }
            } else {
                Log.e("SaveSelections", "LinearLayout is null");
            }
        }

        // Log the selected ingredients
        Log.d("Selected Ingredients", selectedIngredients.toString());
    }

    private void openHomePage() {
        Intent intent = new Intent(dietplan.this, homepage.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(dietplan.this, community.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {
        Intent intent = new Intent(dietplan.this, Grocery.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(dietplan.this, weekplan.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void saveSelectedIngredients() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> set = new HashSet<>(selectedIngredients);
        editor.putStringSet(SELECTED_INGREDIENTS_KEY, set);
        editor.apply();
    }

    private void loadSelectedIngredients() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> set = sharedPreferences.getStringSet(SELECTED_INGREDIENTS_KEY, new HashSet<>());

        for (LinearLayout linearLayout : linearLayouts) {
            if (linearLayout != null) {
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View child = linearLayout.getChildAt(i);
                    if (child instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) child;
                        TextView textView = (TextView) linearLayout.getChildAt(i + 1);
                        if (textView != null && set.contains(textView.getText().toString())) {
                            checkBox.setChecked(true);
                            selectedIngredients.add(textView.getText().toString());
                        }
                    }
                }
            }
        }
    }

    private void handleUnselectedIngredients() {
        List<String> allIngredients = new ArrayList<>();
        for (String ingredient : INGREDIENTS) {
            allIngredients.add(ingredient);
        }

        List<String> unselectedIngredients = new ArrayList<>(allIngredients);
        unselectedIngredients.removeAll(selectedIngredients);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unselected Ingredients");
        builder.setMessage(unselectedIngredients.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
