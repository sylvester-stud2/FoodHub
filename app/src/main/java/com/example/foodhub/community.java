package com.example.foodhub;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class community extends AppCompatActivity {

    private LinearLayout postsContainer;
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    private int userId;

    private OkHttpClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        postsContainer = findViewById(R.id.posts_container);
        bottomNavigationView = findViewById(R.id.bottom_navcomm);

        client = new OkHttpClient();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");


        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.home);
        bottomNavigationView.setSelectedItemId(selectedItemId);
    }

    private void fetchPosts() {
        String url = "https://lamp.ms.wits.ac.za/home/s2709514/postdetails.php";
        new FetchPostsTask().execute(url);
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
        } else if (item.getItemId() == R.id.friends) {
            openFriendsPage();
            return true;
        }

        return true;
    }

    private void openHomePage() {
        Intent intent = new Intent(community.this, homepage.class);
        intent.putExtra("selected_item_id", R.id.home);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        // Do nothing, already on community page
    }

    private void openFriendsPage() {
        Intent intent = new Intent(community.this, friends.class);
        intent.putExtra("selected_item_id", R.id.friends);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }


    private class FetchPostsTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        protected String doInBackground(String... urls) {
            String apiUrl = urls[0];
            Request request = new Request.Builder().url(apiUrl).build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            super.onPostExecute(jsonResponse);
            progressDialog.dismiss();
            if (jsonResponse != null) {
                displayPosts(jsonResponse);
            } else {
                Toast.makeText(community.this, "Failed to fetch posts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayPosts(String jsonData) {

        // Inside displayPosts method
        Log.d("JSON_RESPONSE", "Response: " + jsonData); // Check if jsonData is not null and contains expected data
        try {
            JSONArray posts = new JSONArray(jsonData);
            for (int i = 0; i < posts.length(); i++) {
                JSONObject postObject = posts.getJSONObject(i);
                Log.d("POST_DATA", "Post Object: " + postObject.toString()); // Check if each post object is parsed correctly
                // Remaining code...
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("JSON_ERROR", "Error parsing JSON: " + e.getMessage()); // Log JSON parsing errors
        }


        try {
            JSONArray Posts = new JSONArray(jsonData);
            for (int i = 0; i < Posts.length(); i++) {
                JSONObject postObject = Posts.getJSONObject(i);
                String ingredients = postObject.getString("Ingredients");
                String title = postObject.getString("Title");
                String instructions = postObject.getString("Instructions");
                String imageBase64 = postObject.getString("Image");

                View postView = LayoutInflater.from(this).inflate(R.layout.post_item, postsContainer, false);
                TextView ingredientsview = postView.findViewById(R.id.ingredients);
                TextView titleview = postView.findViewById(R.id.recipe_title);
                TextView instructionsview = postView.findViewById(R.id.instructions);
                ImageView postPic = postView.findViewById(R.id.postpic);

                ingredientsview.setText(ingredients);
                titleview.setText(title);
                instructionsview.setText(instructions);

                // Decode base64 image and set to ImageView
                byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                postPic.setImageBitmap(bitmap);

                postsContainer.addView(postView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse recipes data", Toast.LENGTH_SHORT).show();
        }
    }

}
