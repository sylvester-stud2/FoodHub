package com.example.foodhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class community extends AppCompatActivity {

    private LinearLayout postsContainer;
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);
        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        postsContainer = findViewById(R.id.posts_container);
        bottomNavigationView = findViewById(R.id.bottom_navcomm);

        fetchPostDetails(); // Call the function to fetch post details
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.home);
        bottomNavigationView.setSelectedItemId(selectedItemId);
    }

    private void fetchPostDetails() {
        String url = "https://lamp.ms.wits.ac.za/home/s2709514/getpostdetails.php"; // Use the getpostdetails.php URL
        new FetchPostDetailsTask().execute(url);
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
            // Current item is already selected, do nothing
            return false;
        }

        Intent intent;
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
        // Already implemented to open CreateProfile page
        Intent intent = new Intent(community.this, homepage.class);
        intent.putExtra("selected_item_id", R.id.home);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        // Implement if needed
    }

    private void openFriendsPage() {
        Intent intent = new Intent(community.this, friends.class);
        intent.putExtra("selected_item_id", R.id.friends);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private class FetchPostDetailsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return getJsonResponseFromUrl(urls[0]);
        }

        @Override
        protected void onPostExecute(String jsonResponse) {
            try {
                Log.d("community", "JSON Response: " + jsonResponse);
                JSONArray postsArray = new JSONArray(jsonResponse);
                List<Post> posts = new ArrayList<>();

                for (int i = 0; i < postsArray.length(); i++) {
                    JSONObject postObject = postsArray.getJSONObject(i);
                    Post post = new Post();
                    post.setTitle(postObject.getString("Title"));
                    post.setInstructions(postObject.getString("Instructions"));
                    post.setImageUrl(postObject.getString("image"));
                    post.setIngredients(postObject.getString("Ingredients"));
                    posts.add(post);
                }

                displayPosts(posts);
            } catch (JSONException e) {
                Log.e("community", "Error parsing JSON data", e);
            }
        }
    }

    private String getJsonResponseFromUrl(String urlString) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (Exception e) {
            Log.e("community", "Error fetching JSON data", e);
        }
        return result.toString();
    }

    private void displayPosts(List<Post> posts) {
        for (Post post : posts) {
            View postView = LayoutInflater.from(this).inflate(R.layout.post_item, postsContainer, false);

            TextView profileName = postView.findViewById(R.id.profilename);
            ImageView profilePic = postView.findViewById(R.id.profilepic);
            ImageView postPic = postView.findViewById(R.id.postpic);
            TextView instructions = postView.findViewById(R.id.instructions);
            TextView ingredients = postView.findViewById(R.id.ingredients);

            profileName.setText(post.getTitle());
            instructions.setText(post.getInstructions());
            ingredients.setText(post.getIngredients());

            Glide.with(this).load(post.getImageUrl()).into(postPic);

            // Add OnClickListener to postPic ImageView to display details when clicked
            postPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showPostDetails(post);
                }
            });

            postsContainer.addView(postView);
        }
    }

    // Function to show post details when postPic is clicked
    private void showPostDetails(Post post) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(post.getTitle());
        builder.setMessage("Ingredients: " + post.getIngredients() + "\n\nInstructions: " + post.getInstructions());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
