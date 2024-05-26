package com.example.foodhub;

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

        fetchPosts();

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
        String url = "https://lamp.ms.wits.ac.za/home/s2709514/getpostdetails.php";
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

        @Override
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
                try {
                    Log.d("community", "JSON Response: " + jsonResponse);
                    JSONArray postsArray = new JSONArray(jsonResponse);
                    List<Post> posts = new ArrayList<>();

                    for (int i = 0; i < postsArray.length(); i++) {
                        JSONObject postObject = postsArray.getJSONObject(i);
                        Post post = new Post();
                        post.setTitle(postObject.getString("Title"));
                        post.setInstructions(postObject.getString("Instructions"));
                        post.setImageBase64(postObject.getString("image"));
                        post.setIngredients(postObject.getString("Ingredients"));
                        posts.add(post);
                    }

                    // Call method to display posts
                    displayPosts(posts);
                } catch (JSONException e) {
                    Log.e("community", "Error parsing JSON data", e);
                }
            } else {
                Toast.makeText(community.this, "Failed to fetch posts", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayPosts(List<Post> posts) {
        // Clear existing posts before adding new ones
        postsContainer.removeAllViews();

        for (Post post : posts) {
            View postView = LayoutInflater.from(this).inflate(R.layout.post_item, postsContainer, false);

            TextView profileName = postView.findViewById(R.id.profilename);
            ImageView postPic = postView.findViewById(R.id.postpic);
            TextView recipeTitle = postView.findViewById(R.id.recipe_title);
            TextView instructions = postView.findViewById(R.id.instructions);
            TextView ingredients = postView.findViewById(R.id.ingredients);

            profileName.setText(post.getTitle());
            recipeTitle.setText(post.getTitle());
            instructions.setText(post.getInstructions());
            ingredients.setText(post.getIngredients());

            // Decode base64 image and set to ImageView
            byte[] imageBytes = Base64.decode(post.getImageBase64(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            postPic.setImageBitmap(bitmap);

            postsContainer.addView(postView);
        }
    }
}
