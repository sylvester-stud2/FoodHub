package com.example.foodhub;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class homepage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView profileImageView;
    TextView firstNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        LinearLayout customButton = findViewById(R.id.customButton);
        profileImageView = customButton.findViewById(R.id.imageView2); // Get the ImageView inside the custom button
        firstNameTextView = customButton.findViewById(R.id.textView);


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
                } else if (item.getItemId() == R.id.meal_planner) {
                    openMealPlannerPage();
                    return true;
                }
                return false;
            }
        });

       SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "");
        Log.d("MainActivity", "Retrieved email: " + email);
        new FetchUserProfileTask(email).execute("https://lamp.ms.wits.ac.za/home/s2709514/ProfileAndName.php");

    }

    // AsyncTask to fetch user profile data
    private class FetchUserProfileTask extends AsyncTask<String, Void, JSONObject> {

        private String email;

        public FetchUserProfileTask(String email) {
            this.email = email;

            // Log the value
            Log.d("FetchUserProfileTask", "Email: " + email);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            StringBuilder result = new StringBuilder();

            try {
                // Modify your URL to include the email as a parameter
                URL url = new URL(strings[0] + "?email=" + URLEncoder.encode(email, "UTF-8"));
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                // Read the response
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                // Parse JSON response
                return new JSONObject(result.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);

            if (jsonObject != null) {
                // Log the entire JSON response
                Log.d("FetchUserProfileTask", "JSON Response: " + jsonObject.toString());

                // Assuming the JSON structure is like {"profile_picture": "URL", "first_name": "Name"}
                String profilePictureUrl = jsonObject.optString("profile_picture");
                String firstName = jsonObject.optString("first_name");

                // Log the first name
                Log.d("FetchUserProfileTask", "First Name: " + firstName);

                // Load profile picture into ImageView using Picasso
                if (!TextUtils.isEmpty(profilePictureUrl)) {
                    // Load profile picture into ImageView using Picasso
                    Picasso.get().load(profilePictureUrl).into(profileImageView);
                } else {
                    Log.d("FetchUserProfileTask", "Profile picture URL is empty or null");
                }

                // Check if firstNameTextView is initialized
                if (firstNameTextView != null) {
                    // Set first name into TextView
                    firstNameTextView.setText(firstName);
                } else {
                    Log.d("FetchUserProfileTask", "firstNameTextView is not initialized");
                }
            }
        }


    }

    // Methods to open respective pages
    private void openHomePage() {
        // Already implemented to open CreateProfile page
        Intent intent = new Intent(homepage.this, CreateProfile.class);
        startActivity(intent);
    }

    private void openCommunityPage() {
        // Implement logic to open Community page
    }

    private void openFilterPage() {
        // Implement logic to open Filter page
    }

    private void openGroceryListPage() {
        // Implement logic to open Grocery List page
    }

    private void openMealPlannerPage() {
        // Implement logic to open Meal Planner page
    }
}
