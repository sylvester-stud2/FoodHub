package com.example.foodhub;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class community extends AppCompatActivity {

    private LinearLayout postsContainer;
    private OkHttpClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        // Initialize views
        postsContainer = findViewById(R.id.posts_container);
        client = new OkHttpClient();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        // Fetch and display posts
        new FetchPostsTask().execute();

        // Initialize bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navcomm);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
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

    private void openHomePage() {
        Intent intent = new Intent(community.this, homepage.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(community.this, community.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(community.this, dietplan.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {
        Intent intent = new Intent(community.this, Grocery.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(community.this, weekplan.class);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private class FetchPostsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/getPosts.php";
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
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result != null) {
                displayPosts(result);
            } else {
                Toast.makeText(community.this, "Failed to fetch posts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayPosts(String jsonData) {
        try {
            JSONArray postsArray = new JSONArray(jsonData);
            for (int i = 0; i < postsArray.length(); i++) {
                JSONObject postObject = postsArray.getJSONObject(i);
                String postId = postObject.getString("Post_ID");
                String authorName = postObject.getString("Author_Name");
                String imageBase64 = postObject.getString("Post_Image");
                String instructions = postObject.getString("Instructions");

                View postView = getLayoutInflater().inflate(R.layout.post_item, null);
                TextView authorNameTextView = postView.findViewById(R.id.profilename);
                ImageView postImageView = postView.findViewById(R.id.postpic);

                authorNameTextView.setText(authorName);

                // Decode base64 image and set to ImageView
                byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                postImageView.setImageBitmap(bitmap);

                // Set onClick listener for post image to show post details
                postImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showPostDetails(instructions);
                    }
                });

                postsContainer.addView(postView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse posts data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPostDetails(String instructions) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post Details")
                .setMessage(instructions)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
