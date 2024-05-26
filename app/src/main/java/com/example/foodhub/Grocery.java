package com.example.foodhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;


public class Grocery extends AppCompatActivity {

    Intent intent;
    int userId;
    private BottomNavigationView bottomNavigationView;
    private ListView groceryListView;
    private ArrayAdapter<String> groceryAdapter;
    private List<GroceryItem> groceryList = new ArrayList<>();
    private List<String> groceryDisplayList = new ArrayList<>();

    // Mapping of meal plan IDs to names
    private static final Map<Integer, String> mealPlanNames = new HashMap<>();
    private static final Map<Integer, String> ingredientNames = new HashMap<>();

    static {
        mealPlanNames.put(1, "Breakfast Plan");
        mealPlanNames.put(2, "Lunch Plan");
        mealPlanNames.put(3, "Dinner Plan");
        // Add more meal plans as needed

        ingredientNames.put(1, "Apples");
        ingredientNames.put(2, "Avocado");
        ingredientNames.put(3, "Bacon");
        ingredientNames.put(4, "Baking powder");
        ingredientNames.put(5, "Banana");
        ingredientNames.put(6, "Beans");
        ingredientNames.put(7, "Beef");
        ingredientNames.put(8, "Beetroot");
        ingredientNames.put(9, "Berries");
        ingredientNames.put(10, "Bread");
        ingredientNames.put(11, "Broccoli");
        ingredientNames.put(12, "Butter");
        ingredientNames.put(13, "Butternut");
        ingredientNames.put(14, "Carrot");
        ingredientNames.put(15, "Cereal");
        ingredientNames.put(16, "Cheese");
        ingredientNames.put(17, "Chicken");
        ingredientNames.put(18, "Cinnamon");
        ingredientNames.put(19, "Cream");
        ingredientNames.put(20, "Eggs");
        ingredientNames.put(21, "Fish");
        ingredientNames.put(22, "Flour");
        ingredientNames.put(23, "Garlic");
        ingredientNames.put(24, "Honey");
        ingredientNames.put(25, "Lettuce");
        ingredientNames.put(26, "Lemon");
        ingredientNames.put(27, "Maize meal");
        ingredientNames.put(28, "Mayonnaise");
        ingredientNames.put(29, "Milk");
        ingredientNames.put(30, "Mushrooms");
        ingredientNames.put(31, "Oats");
        ingredientNames.put(32, "Oil");
        ingredientNames.put(33, "Onion");
        ingredientNames.put(34, "Parsley");
        ingredientNames.put(35, "Pasta");
        ingredientNames.put(36, "Pepper");
        ingredientNames.put(37, "Potatoes");
        ingredientNames.put(38, "Rice");
        ingredientNames.put(39, "Salt");
        ingredientNames.put(40, "Soup powder");
        ingredientNames.put(41, "Spinach");
        ingredientNames.put(42, "Steak");
        ingredientNames.put(43, "Sugar");
        ingredientNames.put(44, "Tomato sauce");
        ingredientNames.put(45, "Tomato");
        ingredientNames.put(46, "Vanilla essence");
        ingredientNames.put(47, "Vinegar");
        ingredientNames.put(48, "Water");
        ingredientNames.put(49, "Yeast");
        ingredientNames.put(50, "Yogurt");
        ingredientNames.put(51, "Stock (beef/chicken)");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);

        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.grocery_list);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        groceryListView = findViewById(R.id.list);
        groceryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groceryDisplayList);
        groceryListView.setAdapter(groceryAdapter);

        // Example of adding items to the grocery list
        groceryList.add(new GroceryItem(1, 20, 2));
        groceryList.add(new GroceryItem(1, 29, 3));

        // Update the display list and notify the adapter
        updateGroceryDisplayList();

        // Store the grocery list in the database
        new StoreGroceryListTask().execute();
    }

    private void updateGroceryDisplayList() {
        groceryDisplayList.clear();
        for (GroceryItem item : groceryList) {
            String mealPlanName = mealPlanNames.getOrDefault(item.getMealPlanId(), "Unknown Meal Plan");
            String ingredientName = ingredientNames.getOrDefault(item.getIngredientId(), "Unknown Ingredient");
            String displayText = mealPlanName + ": " + ingredientName + " - Quantity: " + item.getQuantity();
            groceryDisplayList.add(displayText);
        }
        groceryAdapter.notifyDataSetChanged();
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
            return false;
        }

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

    private void openProfilePage() {
        Intent intent = new Intent(Grocery.this, Profile.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openHomePage() {
        Intent intent = new Intent(Grocery.this, homepage.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(Grocery.this, community.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(Grocery.this, dietplan.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {
        // Implement if needed
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(Grocery.this, weekplan.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    // AsyncTask to store the grocery list
    private class StoreGroceryListTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://lamp.ms.wits.ac.za/home/s2709514/store_grocery_list.php");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                JSONObject data = new JSONObject();
                data.put("user_id", userId);

                JSONArray groceryArray = new JSONArray();
                for (GroceryItem item : groceryList) {
                    JSONObject groceryItem = new JSONObject();
                    groceryItem.put("meal_plan_id", item.getMealPlanId());
                    groceryItem.put("ingredient_id", item.getIngredientId());
                    groceryItem.put("quantity", item.getQuantity());
                    groceryArray.put(groceryItem);
                }
                data.put("grocery_list", groceryArray);

                OutputStream os = connection.getOutputStream();
                os.write(data.toString().getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    return "Grocery list stored successfully";
                } else {
                    return "Failed to store grocery list";
                }
            } catch (Exception e) {
                Log.e("StoreGroceryListTask", "Error storing grocery list", e);
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("StoreGroceryListTask", result);
        }
    }

    // GroceryItem class to represent each grocery item
    private static class GroceryItem {
        private int mealPlanId;
        private int ingredientId;
        private int quantity;

        public GroceryItem(int mealPlanId, int ingredientId, int quantity) {
            this.mealPlanId = mealPlanId;
            this.ingredientId = ingredientId;
            this.quantity = quantity;
        }

        public int getMealPlanId() {
            return mealPlanId;
        }

        public int getIngredientId() {
            return ingredientId;
        }

        public int getQuantity() {
            return quantity;
        }
    }
}
