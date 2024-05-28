package com.example.foodhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class weekplan extends AppCompatActivity {
    Intent intent;
    int userId;

    private BottomNavigationView bottomNavigationView;
    private Map<String, JSONObject> mealPlan = new HashMap<>(); // Map to store meal details

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekplan);

        // Set onClickListeners for meal selection
        setMealSelectionListeners();

        // Get user ID from intent
        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set bottom navigation item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.meal_planner);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        // Load meal plan from server
        loadMealPlan();
    }

    // Method to set onClickListeners for meal selection
    private void setMealSelectionListeners() {
        // Add the onClickListener for each meal selection TextView
        int[] mealTextViewIds = {
                R.id.monday_breakfast_select, R.id.monday_lunch_select, R.id.monday_supper_select,
                // Add other meal TextView IDs here
        };

        for (int id : mealTextViewIds) {
            TextView textView = findViewById(id);
            if (textView != null) {
                textView.setOnClickListener(this::onSelectMealClick);
            }
        }
    }

    // Method to handle bottom navigation item selection
    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
            // Current item is already selected, do nothing
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

    // Methods to open different pages
    private void openHomePage() {
        Intent intent = new Intent(weekplan.this, homepage.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.home);

        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(weekplan.this, community.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.community);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(weekplan.this, dietplan.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.filter);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {
        Intent intent = new Intent(weekplan.this, Grocery.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.grocery_list);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        // Implement logic to open Meal Planner page if needed
    }

    // Method to load meal plan from server
    private void loadMealPlan() {
        // Start AsyncTask to load meal plan from server
        new LoadMealPlanTask().execute();
    }

    // AsyncTask to load meal plan from server
    private class LoadMealPlanTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://lamp.ms.wits.ac.za/home/s2709514/meal_plan.php?user_id=" + userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                int responseCode = connection.getResponseCode();
                Log.d("LoadMealPlanTask", "Response Code: " + responseCode);

                InputStream inputStream = connection.getInputStream();
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("\\A");

                boolean hasInput = scanner.hasNext();
                if (hasInput) {
                    String response = scanner.next();
                    Log.d("LoadMealPlanTask", "Response: " + response);
                    return response;
                } else {
                    Log.e("LoadMealPlanTask", "No response from server");
                    return null;
                }
            } catch (Exception e) {
                Log.e("LoadMealPlanTask", "Error in doInBackground", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    Log.d("LoadMealPlanTask", "Parsing JSON response");
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray mealArray = jsonResponse.getJSONArray("meals");

                    // Loop through each meal in the meal plan
                    for (int i = 0; i < mealArray.length(); i++) {
                        JSONObject meal = mealArray.getJSONObject(i);
                        String day = meal.getString("Plan_Day");
                        String mealType = meal.getString("Meal_Type");
                        String recipeName = meal.getString("Recipe_Title");

                        // Store the recipe details in the mealPlan map
                        String key = day + "_" + mealType;
                        mealPlan.put(key, meal);

                        // Update the TextView for the meal with the recipe name
                        int resID = getResources().getIdentifier(day.toLowerCase() + "_" + mealType.toLowerCase() + "_select", "id", getPackageName());
                        TextView textView = findViewById(resID);
                        if (textView != null) {
                            textView.setText(recipeName);
                        }
                    }

                    // Generate the grocery list based on the meal plan
                    generateGroceryList();

                } catch (Exception e) {
                    Log.e("LoadMealPlanTask", "Error parsing JSON response", e);
                    Toast.makeText(weekplan.this, "Failed to load meal plan: JSON parsing error", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("LoadMealPlanTask", "Result is null");
                Toast.makeText(weekplan.this, "Failed to load meal plan: No response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to handle meal selection click
    public void onSelectMealClick(View view) {
        TextView textView = (TextView) view;
        String selectedMeal = textView.getText().toString();
        if (!selectedMeal.equals("Select")) {
            // Log the selected meal
            Log.d("weekplan", "Selected meal: " + selectedMeal);

            // Create an intent and add the JSON data to it
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("user_id", userId);

            // Find the meal details from the map
            for (Map.Entry<String, JSONObject> entry : mealPlan.entrySet()) {
                JSONObject mealDetails = entry.getValue();
                try {
                    if (mealDetails.getString("Recipe_Title").equals(selectedMeal)) {
                        intent.putExtra("recipe_details", mealDetails.toString());
                        break;
                    }
                } catch (Exception e) {
                    Log.e("weekplan", "Error getting recipe details from mealPlan map", e);
                }
            }

            startActivity(intent);
        } else {
            Toast.makeText(this, "No recipe selected", Toast.LENGTH_SHORT).show();
        }
    }

    public class GroceryItem {
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

        public void setMealPlanId(int mealPlanId) {
            this.mealPlanId = mealPlanId;
        }

        public int getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(int ingredientId) {
            this.ingredientId = ingredientId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    // Method to generate the grocery list based on the meal plan
    private void generateGroceryList() {
        // Compile the list of ingredients needed for the week
        List<GroceryItem> groceryList = new ArrayList<>();

        for (JSONObject meal : mealPlan.values()) {
            try {
                JSONArray ingredients = meal.getJSONArray("Ingredients");
                for (int i = 0; i < ingredients.length(); i++) {
                    JSONObject ingredient = ingredients.getJSONObject(i);
                    int ingredientId = ingredient.getInt("Ingredient_ID");
                    int quantity = ingredient.getInt("Quantity");

                    groceryList.add(new GroceryItem(meal.getInt("Meal_Plan_ID"), ingredientId, quantity));
                }
            } catch (Exception e) {
                Log.e("generateGroceryList", "Error generating grocery list", e);
            }
        }

        // Store the grocery list in the database
        new StoreGroceryListTask(groceryList).execute();
    }

    // AsyncTask to store grocery list in the database
    private class StoreGroceryListTask extends AsyncTask<Void, Void, String> {
        private List<GroceryItem> groceryList;

        public StoreGroceryListTask(List<GroceryItem> groceryList) {
            this.groceryList = groceryList;
        }

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
                InputStream inputStream;
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                } else {
                    inputStream = connection.getErrorStream();
                }
                Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";

                return response;
            } catch (Exception e) {
                Log.e("StoreGroceryListTask", "Error storing grocery list", e);
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("StoreGroceryListTask", result);
            try {
                JSONObject response = new JSONObject(result);
                String status = response.getString("status");
                String message = response.getString("message");

                if ("success".equals(status)) {
                    Toast.makeText(weekplan.this, message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(weekplan.this, "Failed to store grocery list: " + message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("StoreGroceryListTask", "Error parsing server response", e);
                Toast.makeText(weekplan.this, "Error parsing server response", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
