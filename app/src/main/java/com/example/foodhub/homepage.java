package com.example.foodhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class homepage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView profile_image;
    TextView name_txt;
    Intent intent;
    String email;

    private OkHttpClient client;
    private Response response;
    private Request request;
    String strJson, apiUrl;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        Button CreateRecipe = findViewById(R.id.add_recipe);
        CreateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homepage.this, CreateRecipe.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }
        });

        // Initialize views
        profile_image = findViewById(R.id.profile_image_id);
        name_txt = findViewById(R.id.NameTest);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        // Get email from intent
        intent = getIntent();
        email = intent.getStringExtra("email");

        // Construct API URL
        apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/ProfileAndName.php?email=" + email;

        // Execute AsyncTask to fetch user data
        new GetUserDataRequest().execute();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener for profile image
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilePage();
            }
        });

        // Set OnClickListener on name text
        name_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfilePage();
            }
        });

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
    }

    public class GetUserDataRequest extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show(); // Show the ProgressDialog here
        }

        @Override
        protected String doInBackground(Void... voids) {
            // Create a form-encoded request body
            request = new Request.Builder().url(apiUrl).build();
            try {
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                updateUserData(result);
            } else {
                // Handle the error
                // You might want to show a message to the user here
            }
            progressDialog.dismiss(); // Dismiss the ProgressDialog here
        }
    }

    private void updateUserData(String strJson) {
        try {
            JSONArray parent = new JSONArray(strJson);
            JSONObject child = parent.getJSONObject(0);
            String imgUrl = child.optString("profile_picture");
            String name = child.getString("first_name");

            Glide.with(this).load(imgUrl).into(profile_image);

            name_txt.setText(name);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Methods to open respective pages
    private void openProfilePage() {
        Intent intent = new Intent(homepage.this, Profile.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openHomePage() {
        // Already implemented to open CreateProfile page
    }

    private void openCommunityPage() {
        Intent intent = new Intent(homepage.this, community.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(homepage.this, dietplan.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {
        // Implement logic to open Grocery List page
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(homepage.this, weekplan.class);
        intent.putExtra("email", email);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
}
