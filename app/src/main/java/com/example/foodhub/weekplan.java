package com.example.foodhub;

import android.app.ProgressDialog;
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
    private Map<String, JSONObject> mealPlan = new HashMap<>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weekplan);

        setMealSelectionListeners();

        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.meal_planner);
        bottomNavigationView.setSelectedItemId(selectedItemId);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading meal plan...");
        progressDialog.setCancelable(false);

        loadMealPlan();
    }

    private void setMealSelectionListeners() {
        int[] mealTextViewIds = {
                R.id.monday_breakfast_select, R.id.monday_lunch_select, R.id.monday_supper_select,

        };

        for (int id : mealTextViewIds) {
            TextView textView = findViewById(id);
            if (textView != null) {
                textView.setOnClickListener(this::onSelectMealClick);
                textView.setText("Select");
            }
        }
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

    }

    private void loadMealPlan() {

        progressDialog.show();
        new LoadMealPlanTask().execute();
    }

    private class LoadMealPlanTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://lamp.ms.wits.ac.za/home/s2709514/meal_plan_test.php?user_id=" + userId);
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
            // Dismiss ProgressDialog after executing AsyncTask
            progressDialog.dismiss();

            if (result != null) {
                try {
                    Log.d("LoadMealPlanTask", "Parsing JSON response");
                    JSONObject jsonResponse = new JSONObject(result);
                    JSONArray mealArray = jsonResponse.getJSONArray("meals");

                    for (int i = 0; i < mealArray.length(); i++) {
                        JSONObject meal = mealArray.getJSONObject(i);
                        String day = meal.getString("Plan_Day");
                        String mealType = meal.getString("Meal_Type");
                        String recipeName = meal.optString("Recipe_Title", "Select");
                        String recipeInstructions = meal.optString("Recipe_Instructions", "No Instructions Available");
                        String recipeImage = meal.optString("Recipe_Image", "");

                        String key = day + "_" + mealType;
                        mealPlan.put(key, meal);

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

    public void onSelectMealClick(View view) {
        TextView textView = (TextView) view;
        String selectedMeal = textView.getText().toString();
        Log.d("weekplan", "Selected meal: " + selectedMeal);

        if (!selectedMeal.equals("Select") && !selectedMeal.equals("No Recipe")) {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("user_id", userId);

            boolean mealFound = false;
            for (Map.Entry<String, JSONObject> entry : mealPlan.entrySet()) {
                JSONObject mealDetails = entry.getValue();
                try {
                    if (mealDetails.getString("Recipe_Title").equals(selectedMeal)) {
                        intent.putExtra("recipe_details", mealDetails.toString());
                        mealFound = true;
                        break;
                    }
                } catch (Exception e) {
                    Log.e("weekplan", "Error getting recipe details from mealPlan map", e);
                }
            }

            if (mealFound) {
                Log.d("weekplan", "Starting RecipeDetailActivity with recipe details.");
                startActivity(intent);
            } else {
                Log.d("weekplan", "No recipe details found for the selected meal.");
                Toast.makeText(this, "No recipe details found for the selected meal", Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.d("weekplan", "No recipe selected or no recipe available.");
            Toast.makeText(this, "No recipe selected or no recipe available", Toast.LENGTH_SHORT).show();
        }
    }

    public class GroceryItem {
        private int mealPlanId;
        private int ingredientId;
        //private int quantity;

        public GroceryItem(int mealPlanId, int ingredientId ) {
            this.mealPlanId = mealPlanId;
            this.ingredientId = ingredientId;
            //this.quantity = quantity;
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

       // public int getQuantity() {
      //      return quantity;
      //  }

    //    public void setQuantity(int quantity) {
     //       this.quantity = quantity;
     //   }
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
                    //int quantity = ingredient.getInt("Quantity");

                    groceryList.add(new GroceryItem(meal.getInt("Meal_Plan_ID"), ingredientId ));
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
                    //groceryItem.put("quantity", item.getQuantity());
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
                Log.e("", "", e);
                Toast.makeText(weekplan.this, "", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

