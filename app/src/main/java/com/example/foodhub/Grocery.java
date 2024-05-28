package com.example.foodhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

    private ListView groceryListView;
    private ArrayAdapter<String> groceryAdapter;
    private BottomNavigationView bottomNavigationView;
    private List<String> groceryDisplayList = new ArrayList<>();
    private Map<String, JSONObject> weekPlan = new HashMap<>();
    private Intent intent;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grocery);
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

        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);


        new FetchGroceryListTask().execute();
    }


    private class FetchGroceryListTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {

                URL url = new URL("https://lamp.ms.wits.ac.za/home/s2709514/get_ingredients.php?user_id=" + userId);
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
                    return response.toString();
                } else {
                    Log.e("FetchGroceryListTask", "Failed to fetch grocery list: " + responseCode);
                    return null;
                }
            } catch (Exception e) {
                Log.e("FetchGroceryListTask", "Error fetching grocery list", e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    groceryDisplayList.clear();  // Clear the existing list

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String ingredientName = jsonObject.getString("Name");
                        groceryDisplayList.add(ingredientName);
                    }

                    // Notify adapter to refresh the list view
                    groceryAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("FetchGroceryListTask", "Error parsing grocery list", e);
                }
            } else {
                Log.e("FetchGroceryListTask", "Result is null, cannot update grocery list");
            }
        }
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
        Intent intent = new Intent(Grocery.this, homepage.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.home);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(Grocery.this, community.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.community);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(Grocery.this, dietplan.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.filter);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {

    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(Grocery.this, weekplan.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.meal_planner);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
    public void onSelectMealClick(View view) {
        TextView textView = (TextView) view;
        String selectedMeal = textView.getText().toString();
        Log.d("weekplan", "Selected meal: " + selectedMeal);

        if (!selectedMeal.equals("Select") && !selectedMeal.equals("No Recipe")) {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("user_id", userId);

            boolean mealFound = false;
            for (Map.Entry<String, JSONObject> entry : weekPlan.entrySet()) {
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
}
