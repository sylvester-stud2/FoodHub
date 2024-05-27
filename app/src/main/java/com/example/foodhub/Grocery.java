package com.example.foodhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grocery extends AppCompatActivity {

    Intent intent;
    int userId;
    private BottomNavigationView bottomNavigationView;
    private ListView groceryListView;
    private ArrayAdapter<String> groceryAdapter;
    private List<String> groceryDisplayList = new ArrayList<>();

    private static final Map<Integer, String> ingredientNames = new HashMap<>();

    static {
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
        Log.d("Grocery", "User ID: " + userId);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this::handleNavigationItemSelected);

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.grocery_list);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        groceryListView = findViewById(R.id.list);
        groceryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, groceryDisplayList);
        groceryListView.setAdapter(groceryAdapter);

        // Fetch and display the stored grocery list
        new FetchStoredGroceryListTask().execute();
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

    // AsyncTask to fetch and display the stored grocery list
    private class FetchStoredGroceryListTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                URL url = new URL("https://lamp.ms.wits.ac.za/home/s2709514/get_grocery_list.php?user_id=" + userId);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    Log.d("FetchStoredGroceryListTask", "Response: " + response.toString());  // Log the full response
                    return response.toString();
                } else {
                    Log.e("FetchStoredGroceryListTask", "Failed to fetch stored grocery list: " + responseCode);
                    return null;
                }
            } catch (Exception e) {
                Log.e("FetchStoredGroceryListTask", "Error fetching stored grocery list", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    groceryDisplayList.clear();  // Clear the existing list to avoid duplicates

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int ingredientId = jsonObject.getInt("Ingredient_ID");
                        String ingredientName = ingredientNames.get(ingredientId);
                        if (ingredientName != null) {
                            groceryDisplayList.add(ingredientName);
                            Log.d("FetchStoredGroceryListTask", "Added ingredient: " + ingredientName);
                        } else {
                            Log.e("FetchStoredGroceryListTask", "Ingredient ID not found: " + ingredientId);
                        }
                    }

                    // Update the UI on the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            groceryAdapter.notifyDataSetChanged();  // Notify adapter to refresh the list view
                            Log.d("FetchStoredGroceryListTask", "Grocery list updated");
                        }
                    });
                } catch (Exception e) {
                    Log.e("FetchStoredGroceryListTask", "Error parsing stored grocery list", e);
                }
            } else {
                Log.e("FetchStoredGroceryListTask", "Result is null, cannot update grocery list");
            }
        }
    }


}
