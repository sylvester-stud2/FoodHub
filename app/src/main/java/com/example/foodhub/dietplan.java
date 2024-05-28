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

        // Add LinearLayouts that contain checkboxes to the list
        linearLayouts.add(findViewById(R.id.linearLayout1)); // Make sure these IDs exist in your layout file
        linearLayouts.add(findViewById(R.id.linearLayout2));
        linearLayouts.add(findViewById(R.id.linearLayout3));
        linearLayouts.add(findViewById(R.id.linearLayout4));
        linearLayouts.add(findViewById(R.id.linearLayout5));
        linearLayouts.add(findViewById(R.id.linearLayout6));
        linearLayouts.add(findViewById(R.id.linearLayout7));
        linearLayouts.add(findViewById(R.id.linearLayout8));
        linearLayouts.add(findViewById(R.id.linearLayout9));
        linearLayouts.add(findViewById(R.id.linearLayout10));
        linearLayouts.add(findViewById(R.id.linearLayout11));
        linearLayouts.add(findViewById(R.id.linearLayout12));
        linearLayouts.add(findViewById(R.id.linearLayout13));
        linearLayouts.add(findViewById(R.id.linearLayout14));
        linearLayouts.add(findViewById(R.id.linearLayout15));
        linearLayouts.add(findViewById(R.id.linearLayout16));
        linearLayouts.add(findViewById(R.id.linearLayout17));
        linearLayouts.add(findViewById(R.id.linearLayout18));
        linearLayouts.add(findViewById(R.id.linearLayout19));
        linearLayouts.add(findViewById(R.id.linearLayout20));
        linearLayouts.add(findViewById(R.id.linearLayout21));
        linearLayouts.add(findViewById(R.id.linearLayout22));
        linearLayouts.add(findViewById(R.id.linearLayout23));
        linearLayouts.add(findViewById(R.id.linearLayout24));
        linearLayouts.add(findViewById(R.id.linearLayout25));
        linearLayouts.add(findViewById(R.id.linearLayout26));
        linearLayouts.add(findViewById(R.id.linearLayout27));
        linearLayouts.add(findViewById(R.id.linearLayout28));
        linearLayouts.add(findViewById(R.id.linearLayout29));
        linearLayouts.add(findViewById(R.id.linearLayout30));
        linearLayouts.add(findViewById(R.id.linearLayout31));
        linearLayouts.add(findViewById(R.id.linearLayout32));
        linearLayouts.add(findViewById(R.id.linearLayout33));
        linearLayouts.add(findViewById(R.id.linearLayout34));
        linearLayouts.add(findViewById(R.id.linearLayout35));
        linearLayouts.add(findViewById(R.id.linearLayout36));
        linearLayouts.add(findViewById(R.id.linearLayout37));
        linearLayouts.add(findViewById(R.id.linearLayout38));
        linearLayouts.add(findViewById(R.id.linearLayout39));
        linearLayouts.add(findViewById(R.id.linearLayout40));
        linearLayouts.add(findViewById(R.id.linearLayout41));
        linearLayouts.add(findViewById(R.id.linearLayout42));
        linearLayouts.add(findViewById(R.id.linearLayout43));
        linearLayouts.add(findViewById(R.id.linearLayout44));
        linearLayouts.add(findViewById(R.id.linearLayout45));
        linearLayouts.add(findViewById(R.id.linearLayout46));
        linearLayouts.add(findViewById(R.id.linearLayout47));
        linearLayouts.add(findViewById(R.id.linearLayout48));
        linearLayouts.add(findViewById(R.id.linearLayout49));
        linearLayouts.add(findViewById(R.id.linearLayout50));
        linearLayouts.add(findViewById(R.id.linearLayout51));



        // Add all your linear layouts here...

        // Set up checkboxes
        setupCheckBox(R.id.allergic_checkbox, "Apples");
        setupCheckBox(R.id.vegan_checkbox1, "Avocado");
        setupCheckBox(R.id.vegan_checkbox2, "Bacon");
        setupCheckBox(R.id.vegan_checkbox3, "Baking powder");
        setupCheckBox(R.id.vegan_checkbox4, "Banana");
        setupCheckBox(R.id.vegan_checkbox5, "Beans");
        setupCheckBox(R.id.vegan_checkbox6, "Beef");
        setupCheckBox(R.id.vegan_checkbox8, "Beetroot");
        setupCheckBox(R.id.vegan_checkbox9, "Berries");
        setupCheckBox(R.id.vegan_checkbox10, "Bread");
        setupCheckBox(R.id.vegan_checkbox11, "Broccoli");
        setupCheckBox(R.id.vegan_checkbox12, "Butter");
        setupCheckBox(R.id.vegan_checkbox13, "Butternut");
        setupCheckBox(R.id.vegan_checkbox14, "Carrot");
        setupCheckBox(R.id.vegan_checkbox15, "Cereal");
        setupCheckBox(R.id.vegan_checkbox16, "Cheese");
        setupCheckBox(R.id.vegan_checkbox17, "Chicken");
        setupCheckBox(R.id.vegan_checkbox18, "Cinnamon");
        setupCheckBox(R.id.vegan_checkbox19, "Cream");
        setupCheckBox(R.id.vegan_checkbox20, "Eggs");
        setupCheckBox(R.id.vegan_checkbox21, "Fish");
        setupCheckBox(R.id.vegan_checkbox22, "Flour");
        setupCheckBox(R.id.vegan_checkbox23, "Garlic");
        setupCheckBox(R.id.vegan_checkbox24, "Honey");
        setupCheckBox(R.id.vegan_checkbox25, "Lettuce");
        setupCheckBox(R.id.vegan_checkbox26, "Lemon");
        setupCheckBox(R.id.vegan_checkbox27, "Maize meal");
        setupCheckBox(R.id.vegan_checkbox28, "Mayonnaise");
        setupCheckBox(R.id.vegan_checkbox29, "Milk");
        setupCheckBox(R.id.vegan_checkbox30, "Mushrooms");
        setupCheckBox(R.id.vegan_checkbox31, "Oats");
        setupCheckBox(R.id.vegan_checkbox32, "Oil");
        setupCheckBox(R.id.vegan_checkbox33, "Onion");
        setupCheckBox(R.id.vegan_checkbox34, "Parsley");
        setupCheckBox(R.id.vegan_checkbox35, "Pasta");
        setupCheckBox(R.id.vegan_checkbox36, "Pepper");
        setupCheckBox(R.id.vegan_checkbox37, "Potatoes");
        setupCheckBox(R.id.vegan_checkbox38, "Rice");
        setupCheckBox(R.id.vegan_checkbox39, "Salt");
        setupCheckBox(R.id.vegan_checkbox40, "Soup powder");
        setupCheckBox(R.id.vegan_checkbox41, "Spinach");
        setupCheckBox(R.id.vegan_checkbox42, "Steak");
        setupCheckBox(R.id.vegan_checkbox43, "Sugar");
        setupCheckBox(R.id.vegan_checkbox44, "Tomato sauce");
        setupCheckBox(R.id.vegan_checkbox45, "Tomato");
        setupCheckBox(R.id.vegan_checkbox46, "Vanilla");
        setupCheckBox(R.id.vegan_checkbox47, "Vinegar");
        setupCheckBox(R.id.vegan_checkbox48, "Water");
        setupCheckBox(R.id.vegan_checkbox49, "Yeast");
        setupCheckBox(R.id.vegan_checkbox50, "Yogurt");
        setupCheckBox(R.id.vegan_checkbox51, "Stock (beef/chicken)");

        // Add all your checkboxes here...

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });
        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.filter);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        // Load the previously selected ingredients
        loadSelectedIngredients();
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

        // Iterate through all LinearLayouts containing checkboxes
        for (LinearLayout linearLayout : linearLayouts) {
            if (linearLayout != null) {
                // Iterate through all child views in the LinearLayout
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View child = linearLayout.getChildAt(i);
                    if (child instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) child;
                        TextView textView = (TextView) linearLayout.getChildAt(i + 1); // Assuming TextView is next to CheckBox

                        if (checkBox.isChecked()) {
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

        // Iterate through all LinearLayouts containing checkboxes
        for (LinearLayout linearLayout : linearLayouts) {
            if (linearLayout != null) {
                // Iterate through all child views in the LinearLayout
                for (int i = 0; i < linearLayout.getChildCount(); i++) {
                    View child = linearLayout.getChildAt(i);
                    if (child instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) child;
                        TextView textView = (TextView) linearLayout.getChildAt(i + 1); // Assuming TextView is next to CheckBox

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
        // Define the full list of possible ingredients
        List<String> allIngredients = new ArrayList<>();
        allIngredients.add("Apples");
        allIngredients.add("Avocado");
        allIngredients.add("Bacon");
        allIngredients.add("Baking powder");
        allIngredients.add("Banana");
        allIngredients.add("Beans");
        allIngredients.add("Beef");
        allIngredients.add("Beetroot");
        allIngredients.add("Berries");
        allIngredients.add("Bread");
        allIngredients.add("Broccoli");
        allIngredients.add("Butter");
        allIngredients.add("Butternut");
        allIngredients.add("Carrot");
        allIngredients.add("Cereal");
        allIngredients.add("Cheese");
        allIngredients.add("Chicken");
        allIngredients.add("Cinnamon");
        allIngredients.add("Cream");
        allIngredients.add("Eggs");
        allIngredients.add("Fish");
        allIngredients.add("Flour");
        allIngredients.add("Garlic");
        allIngredients.add("Honey");
        allIngredients.add("Lettuce");
        allIngredients.add("Lemon");
        allIngredients.add("Maize meal");
        allIngredients.add("Mayonnaise");
        allIngredients.add("Milk");
        allIngredients.add("Mushrooms");
        allIngredients.add("Oats");
        allIngredients.add("Oil");
        allIngredients.add("Onion");
        allIngredients.add("Parsley");
        allIngredients.add("Pasta");
        allIngredients.add("Pepper");
        allIngredients.add("Potatoes");
        allIngredients.add("Rice");
        allIngredients.add("Salt");
        allIngredients.add("Soup powder");
        allIngredients.add("Spinach");
        allIngredients.add("Steak");
        allIngredients.add("Sugar");
        allIngredients.add("Tomato sauce");
        allIngredients.add("Tomato");
        allIngredients.add("Vanilla");
        allIngredients.add("Vinegar");
        allIngredients.add("Water");
        allIngredients.add("Yeast");
        allIngredients.add("Yogurt");
        allIngredients.add("Stock (beef/chicken)");

        // Identify unselected ingredients
        List<String> unselectedIngredients = new ArrayList<>(allIngredients);
        unselectedIngredients.removeAll(selectedIngredients);

        // Display unselected ingredients in an AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Unselected Ingredients");
        builder.setMessage(unselectedIngredients.toString());
        builder.setPositiveButton("OK", null);
        builder.show();
    }
}
