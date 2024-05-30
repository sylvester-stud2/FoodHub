package com.example.foodhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LikesActivity extends AppCompatActivity {

    private LinearLayout likesContainer;
    private int friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likes);

        likesContainer = findViewById(R.id.likes_container);

        // Get the friend_id from the intent
        Intent intent = getIntent();
        friendId = intent.getIntExtra("friend_id", -1);

        // Fetch liked recipes
        new FetchLikesTask().execute(friendId);
    }

    private class FetchLikesTask extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            int friendId = params[0];
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/fetchlikes.php"; // Replace with your server URL

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String postParams = "friend_id=" + friendId;
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
                Toast.makeText(LikesActivity.this, "Error fetching likes data", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                JSONArray likesArray = new JSONArray(result);
                for (int i = 0; i < likesArray.length(); i++) {
                    JSONObject likeObject = likesArray.getJSONObject(i);
                    String recipeTitle = likeObject.getString("recipe_title");
                    String recipeImage = likeObject.getString("recipe_image");
                    String recipeIngredients = likeObject.getString("recipe_ingredients");
                    String recipeInstructions = likeObject.getString("recipe_instructions");

                    addLikeItem(recipeTitle, recipeImage, recipeIngredients, recipeInstructions);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(LikesActivity.this, "Error parsing likes data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addLikeItem(String title, String image, String ingredients, String instructions) {
        View likeItemView = getLayoutInflater().inflate(R.layout.like_item, likesContainer, false);

        TextView titleTextView = likeItemView.findViewById(R.id.recipe_title);
        ImageView imageView = likeItemView.findViewById(R.id.recipe_image);
        TextView ingredientsTextView = likeItemView.findViewById(R.id.recipe_ingredients);
        TextView instructionsTextView = likeItemView.findViewById(R.id.recipe_instructions);

        titleTextView.setText(title);
        // Assuming you have a method to load images from a URL
        Picasso.get().load(image).into(imageView);
        ingredientsTextView.setText(ingredients);
        instructionsTextView.setText(instructions);

        likesContainer.addView(likeItemView);
    }
}
