package com.example.foodhub;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class friends extends AppCompatActivity {
    private Intent intent;
    int userId;

    private BottomNavigationView bottomNavigationView;
    private LinearLayout friendsContainer;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends);
        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);
        friendsContainer = findViewById(R.id.friends_container);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.friends);
        bottomNavigationView.setSelectedItemId(selectedItemId);

        // Fetch friends details
        new FetchFriendsTask().execute(userId);
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
        } else if (item.getItemId() == R.id.meal_planner) {
            openMealPlannerPage();
            return true;
        }

        return true;
    }

    private void openHomePage() {
        Intent intent = new Intent(friends.this, homepage.class);
        intent.putExtra("selected_item_id", R.id.home);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(friends.this, community.class);
        intent.putExtra("selected_item_id", R.id.community);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(friends.this, weekplan.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.meal_planner);
        startActivity(intent);
        finish();
    }

    private void openFriendsPage() {
        // No implementation needed as we are already on the Friends page
    }

    @SuppressLint("StaticFieldLeak")
    private class FetchFriendsTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            int userId = params[0];
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/fetchfriends.php";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String postParams = "user_id=" + userId;
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(postParams);
                writer.flush();
                writer.close();

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                connection.disconnect();
                return response.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(friends.this, "Error fetching friends data", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray friendsArray = new JSONArray(result);
                for (int i = 0; i < friendsArray.length(); i++) {
                    JSONObject friendObject = friendsArray.getJSONObject(i);
                    String firstName = friendObject.getString("first_name");
                    String lastName = friendObject.getString("last_name");
                    String profileImage = friendObject.getString("profile_image");

                    addFriendItem(firstName, lastName, profileImage);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(friends.this, "Error parsing friends data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addFriendItem(String firstName, String lastName, String profileImage) {
        View friendItemView = LayoutInflater.from(this).inflate(R.layout.friend_item, friendsContainer, false);

        TextView friendNameTextView = friendItemView.findViewById(R.id.friend_name);
        ImageView profileImageBase64 = friendItemView.findViewById(R.id.friend_profile);
        Button likesButton = friendItemView.findViewById(R.id.friendlikes);

        friendNameTextView.setText(firstName + " " + lastName);

        if (profileImage != null && !profileImage.isEmpty()) {
            byte[] imageBytes = Base64.decode(profileImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            Glide.with(this)
                    .load(bitmap)
                    .circleCrop()
                    .into(profileImageBase64);
        } else {
            Glide.with(this)
                    .load(R.drawable.avator)
                    .circleCrop()
                    .into(profileImageBase64);
        }

        likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(friends.this,LikesActivity.class);
                intent.putExtra("selected_item_id", R.id.home);
                intent.putExtra("user_id", userId);
                overridePendingTransition(0, 0);
                startActivity(intent);
                finish();
            }
        });

        friendsContainer.addView(friendItemView);
    }
}
