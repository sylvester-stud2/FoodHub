package com.example.foodhub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class dietplan extends AppCompatActivity {

    String getURL = "https://lamp.ms.wits.ac.za/mc/Dietplan.php";
    String postURL ="https://lamp.ms.wits.ac.za/mc/Dietplan.php";

    Intent intent;
    int userId;
    String emailToChange;

    private List<String> selectedIngredients;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_plan);

        intent = getIntent();

        userId = intent.getIntExtra("user_id", -1);

        selectedIngredients = new ArrayList<>();

        // Set up checkboxes
        setupCheckBox(R.id.allergic_checkbox, "Apples");
        setupCheckBox(R.id.vegan_checkbox1, "Avocadoe");
        setupCheckBox(R.id.vegan_checkbox2, "Bacon");
        setupCheckBox(R.id.vegan_checkbox3, "Baking powder");
        setupCheckBox(R.id.vegan_checkbox4, "Banana");
        setupCheckBox(R.id.vegan_checkbox5, "Beans");
        setupCheckBox(R.id.vegan_checkbox6, "Beef");
        setupCheckBox(R.id.vegan_checkbox8, "Beetroot");
        setupCheckBox(R.id.vegan_checkbox9, "Berries");
        setupCheckBox(R.id.vegan_checkbox10, "Bread");
        setupCheckBox(R.id.vegan_checkbox11, "Brocolli");
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
        setupCheckBox(R.id.vegan_checkbox28, "Mayonaise");
        setupCheckBox(R.id.vegan_checkbox29, "Milk");
        setupCheckBox(R.id.vegan_checkbox30, "Mushrooms");
        setupCheckBox(R.id.vegan_checkbox31,"Oats");

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

        return true;
    }

    private void setupCheckBox(int checkBoxId, String ingredient) {
        CheckBox checkBox = findViewById(checkBoxId);
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
    }

    private void checkIngredientsForRecipe(List<String> recipeIngredients) {
        // List to hold missing ingredients
        List<String> missingIngredients = new ArrayList<>();

        // Check for missing ingredients
        for (String ingredient : recipeIngredients) {
            if (!selectedIngredients.contains(ingredient)) {
                missingIngredients.add(ingredient);
            }
        }

        // Prepare the message to be displayed
        String message;
        if (missingIngredients.isEmpty()) {
            message = "You have all the ingredients needed for the recipe!";
        } else {
            message = "You are missing the following ingredients:\n" + String.join(", ", missingIngredients);
        }

        // Create and show the AlertDialog
        new AlertDialog.Builder(this)
                .setTitle("Recipe Check Result")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    // Methods to open respective pages
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

    private void openFilterPage() {
        Intent intent = new Intent(dietplan.this, dietplan.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {

        Intent intent = new Intent(dietplan.this,Grocery.class);
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
}