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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        }
        else if (item.getItemId() == R.id.meal_planner) {
            openMealPlannerPage();
            return true;
        }

        return true;
    }



    private void openHomePage() {
        // Already implemented to open CreateProfile page
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
//        Intent intent = new Intent(friends.this, friends.class);
//        intent.putExtra("selected_item_id", R.id.friends);
//        intent.putExtra("user_id", userId);
//        overridePendingTransition(0, 0);
//        startActivity(intent);
//        finish();
    }

    // AsyncTask to fetch friends' details
    @SuppressLint("StaticFieldLeak")
    private class FetchFriendsTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            int userId = params[0];
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/fetchfriends.php"; // Replace with your server URL

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String postParams = "user_id=" + userId;
                connection.getOutputStream().write(postParams.getBytes());

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

        if (profileImageBase64 != null) {
            byte[] decodedString = Base64.decode(String.valueOf(profileImageBase64), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImageBase64.setImageBitmap(decodedByte);
        } else {
            profileImageBase64.setImageResource(R.drawable.person);
        }

        likesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(friends.this, com.example.foodhub.LikesActivity.class);
                intent.putExtra("friend_id", userId); // Pass the friend ID here
                startActivity(intent);
            }
        });

        friendsContainer.addView(friendItemView);
    }
}

