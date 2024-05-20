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
    Intent intent;
    String email;
    private List<String> selectedIngredients;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diet_plan);

        intent = getIntent();
        email = intent.getStringExtra("email");

        selectedIngredients = new ArrayList<>();

        // Set up checkboxes
        setupCheckBox(R.id.allergic_checkbox, "Water");
        setupCheckBox(R.id.vegan_checkbox1, "Baby tomatoes");
        setupCheckBox(R.id.vegan_checkbox2, "Vinegar");
        setupCheckBox(R.id.vegan_checkbox3, "Salt");
        setupCheckBox(R.id.vegan_checkbox4, "Soy milk");
        setupCheckBox(R.id.vegan_checkbox5, "Flour");
        setupCheckBox(R.id.vegan_checkbox6, "Olive oil");
        setupCheckBox(R.id.vegan_checkbox8, "Brown sugar");
        setupCheckBox(R.id.vegan_checkbox9, "Rice");
        setupCheckBox(R.id.vegan_checkbox10, "Nuts");
        setupCheckBox(R.id.vegan_checkbox11, "Grains");
        setupCheckBox(R.id.vegan_checkbox12, "Honey");
        setupCheckBox(R.id.vegan_checkbox13, "Seeds");
        setupCheckBox(R.id.vegan_checkbox14, "Broccoli");
        setupCheckBox(R.id.vegan_checkbox15, "Carrots");
        setupCheckBox(R.id.vegan_checkbox16, "Garlic");
        setupCheckBox(R.id.vegan_checkbox17, "Onions");
        setupCheckBox(R.id.vegan_checkbox18, "Herbs");
        setupCheckBox(R.id.vegan_checkbox19, "Lemon");
        setupCheckBox(R.id.vegan_checkbox20, "Spices (turmeric, paprika)");
        setupCheckBox(R.id.vegan_checkbox21, "Soy sauce");
        setupCheckBox(R.id.vegan_checkbox22, "Cauliflower");
        setupCheckBox(R.id.vegan_checkbox23, "Avocado");
        setupCheckBox(R.id.vegan_checkbox24, "Mushrooms");
        setupCheckBox(R.id.vegan_checkbox25, "Spinach");
        setupCheckBox(R.id.vegan_checkbox26, "Cocoa powder");
        setupCheckBox(R.id.vegan_checkbox27, "Cinnamon");
        setupCheckBox(R.id.vegan_checkbox28, "Banana");
        setupCheckBox(R.id.vegan_checkbox29, "Oats");
        setupCheckBox(R.id.vegan_checkbox30, "Dozen eggs");

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

        // Set up check recipe button
//        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button checkRecipeButton = findViewById(R.id.check_recipe_button);
//        checkRecipeButton.setOnClickListener(v -> {
//            List<String> recipeIngredients = Arrays.asList("Water", "Baby tomatoes", "Vinegar", "Salt");
//            checkIngredientsForRecipe(recipeIngredients);
//        });
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
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(dietplan.this, community.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
//        Intent intent = new Intent(dietplan.this, filter.class);
//        intent.putExtra("email", email);
//        startActivity(intent);
//        finish();
    }

    private void openGroceryListPage() {

        Intent intent = new Intent(dietplan.this,Grocery.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();

    }

    private void openFriendsPage() {
        Intent intent = new Intent(dietplan.this, friends.class);
        intent.putExtra("email", email);
        startActivity(intent);
        finish();
    }
}