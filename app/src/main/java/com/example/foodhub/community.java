package com.example.foodhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.ProgressDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class community extends AppCompatActivity {

    private ImageView userImageView;
    private TextView userNameTextView;
    private OkHttpClient client;
    private Response response;
    private Request request;
    private String apiUrlGetUserInfo;
    private ProgressDialog progressDialog;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        // Initialize ImageView and TextView
        userImageView = findViewById(R.id.authorprofile);
        userNameTextView = findViewById(R.id.authorname);

        // Construct API URL
        apiUrlGetUserInfo = "https://lamp.ms.wits.ac.za/home/s2709514/ProfileAndName.php?email=" + email;

        // Execute AsyncTask to fetch user data
        new GetUserDataRequest().execute();

        bottomNavigationView = findViewById(R.id.bottom_navcomm);

        // Set click listeners for the buttons or icons
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    openHome();
                    return true;
                } else if (item.getItemId() == R.id.friends) {
                    openFriends();
                    return true;
                } else if (item.getItemId() == R.id.filter) {
                    openFilter();
                    return true;
                } else if (item.getItemId() == R.id.grocery_list) {
                    openGroceryList();
                    return true;
                } else if (item.getItemId() == R.id.meal_planner) {
                    openMealPlanner();
                    return true;
                }
                return false;
            }

            private void openMealPlanner() {
                Intent intent = new Intent(community.this, weekplan.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }

            private void openGroceryList() {
                Intent intent = new Intent(community.this, Grocery.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }

            private void openFilter() {
                Intent intent = new Intent(community.this, dietplan.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }

            private void openHome() {
                Intent intent = new Intent(community.this, homepage.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
            }

            private void openFriends() {
                Intent intent = new Intent(community.this, friends.class);
                intent.putExtra("email", email);
                startActivity(intent);
                finish();
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
            request = new Request.Builder().url(apiUrlGetUserInfo).build();
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful() && response.body() != null) {
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss(); // Dismiss the ProgressDialog

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String userName = jsonObject.getString("name");
                    String userImageURL = jsonObject.getString("profile_picture_url");

                    // Set user name to TextView
                    userNameTextView.setText(userName);

                    // Load user image using Picasso or Glide
                    Picasso.get().load(userImageURL).into(userImageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
