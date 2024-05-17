package com.example.foodhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
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

        // Initialize views
        profile_image = findViewById(R.id.profile_image_id);
        name_txt = findViewById(R.id.NameTest);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize progressDialog
        progressDialog = new ProgressDialog(this);

        // Get email from intent
        intent = getIntent();
        email = intent.getStringExtra("email");

        // Construct API URL
        apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/ProfileAndName.php?email=" + email;

        // Execute AsyncTask to fetch user data
        new GetUserDataRequest().execute();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener

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

    public class GetUserDataRequest extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show(); // Show the ProgressDialog here
        }

        protected Void doInBackground(Void... voids) {
            // Create a form-encoded request body
            request = new Request.Builder().url(apiUrl).build();
            try {
                response = client.newCall(request).execute();
            } catch (IOException e){
                e.printStackTrace();
            }
            // Your background task logic here
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            try {
                strJson = response.body().string();
                updateUserData(strJson);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void updateUserData(String strJson) {
        try {
            JSONArray parent = new JSONArray(strJson);
            JSONObject child = parent.getJSONObject(0);
            String imgUrl = child.optString("profile_picture", "");

            String name = child.getString("first_name");
            if (imgUrl != null && !imgUrl.isEmpty()) {
                Glide.with(this).load(imgUrl).into(profile_image);
            } else {
                Glide.with(this).load(R.drawable.userhome).into(profile_image);
            }

            name_txt.setText(name);
            progressDialog.hide();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    // Methods to open respective pages
    private void openProfilePage() {
        // Implement logic to open Filter page
        Intent intent = new Intent(homepage.this, Profile.class);
        startActivity(intent);
    }
    private void openHomePage() {
        // Already implemented to open CreateProfile page
        Intent intent = new Intent(homepage.this, homepage.class);
        startActivity(intent);
    }

    private void openCommunityPage() {
        // Implement logic to open Community page
        Intent intent = new Intent(homepage.this, community.class);
        startActivity(intent);
    }

    private void openFilterPage() {
        // Implement logic to open Filter page
        Intent intent = new Intent(homepage.this, dietplan.class);
        startActivity(intent);
    }

    private void openGroceryListPage() {
        // Implement logic to open Grocery List page

    }

    private void openMealPlannerPage() {
        // Implement logic to open Meal Planner page
        Intent intent = new Intent(homepage.this, weekplan.class);
        startActivity(intent);
    }

}
