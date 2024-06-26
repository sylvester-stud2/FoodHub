package com.example.foodhub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class community extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private LinearLayout recipesContainer;
    private ImageView profile_image;
    private TextView name_txt;
    private Intent intent;
    private int userId;
    private OkHttpClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community);

        // Initialize views
        profile_image = findViewById(R.id.profile_image_id);
        name_txt = findViewById(R.id.NameTest);
        recipesContainer = findViewById(R.id.recipes_container);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        // Get email and user ID from intent
        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        // Execute AsyncTask to fetch user data
        new GetUserDataRequest().execute();
        new FetchRecipesTask().execute();

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
                return handleNavigationItemSelected(item);
            }
        });

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.community);
        bottomNavigationView.setSelectedItemId(selectedItemId);
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
            // Current item is already selected, do nothing
            return false;
        }

        if (item.getItemId() == R.id.home) {
            openHomePage();
            return true;
        } else if (item.getItemId() == R.id.community) {
            openCommunityPage();
            return true;
        } else if (item.getItemId() == R.id.friends) {
            openFriendPage();
            return true;
        }
        else if (item.getItemId() == R.id.meal_planner) {
            openPlannerPage();
            return true;
        }

        return true;
    }

    private class GetUserDataRequest extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/ProfileAndName.php?user_id=" + userId;
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
                updateUserData(result);
            } else {
                Toast.makeText(community.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class AddToMealPlannerTask extends AsyncTask<Void, Void, String> {
        private int recipeId;
        private String selectedDay;
        private String mealType;
        private int userId;

        public AddToMealPlannerTask(int recipeId, String selectedDay, String mealType, int userId) {
            this.recipeId = recipeId;
            this.selectedDay = selectedDay;
            this.mealType = mealType;
            this.userId = userId;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/addToMealPlanner.php";
            RequestBody requestBody = new FormBody.Builder()
                    .add("recipe_id", String.valueOf(recipeId))
                    .add("day", selectedDay)
                    .add("meal_type", mealType)
                    .add("user_id", String.valueOf(userId))
                    .build();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .build();
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
            if (result != null) {
                Toast.makeText(community.this, "Recipe added to meal planner successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(community.this, "Failed to add recipe to meal planner", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void updateUserData(String strJson) {
        try {
            JSONArray parent = new JSONArray(strJson);
            JSONObject child = parent.getJSONObject(0);
            String imgBase64 = child.optString("profile_picture");
            String firstName = child.getString("first_name");

            if (!imgBase64.isEmpty()) {
                byte[] imageBytes = Base64.decode(imgBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                Glide.with(this)
                        .load(bitmap)
                        .circleCrop()
                        .into(profile_image);
            }

            name_txt.setText(firstName);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse user data", Toast.LENGTH_SHORT).show();
        }
    }

    private class FetchRecipesTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/getRCOMM2.php"; // Updated to remove user_id parameter
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
                displayRecipes(result);
            } else {
                Toast.makeText(community.this, "Failed to fetch recipes", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayRecipes(String jsonData) {
        try {
            JSONArray recipesArray = new JSONArray(jsonData);
            for (int i = 0; i < recipesArray.length(); i++) {
                JSONObject recipeObject = recipesArray.getJSONObject(i);
                String recipeId = recipeObject.getString("Recipe_ID");
                String title = recipeObject.getString("Title");
                String instructions = recipeObject.getString("Instructions");
                String imageBase64 = recipeObject.getString("image");
                String authorName = recipeObject.getString("author_name");
                String profilePicBase64 = recipeObject.getString("profile_picture");
                int RecipeId = recipeObject.getInt("Recipe_ID");

                // Create a new view for each recipe
                View recipeView = getLayoutInflater().inflate(R.layout.post_item_moloko, null);

                // Set recipe title and instructions
                TextView titleTextView = recipeView.findViewById(R.id.recipe_title);
                TextView instructionsTextView = recipeView.findViewById(R.id.recipe_instructions);
                titleTextView.setText(title);
                instructionsTextView.setText(instructions);

                // Set recipe image using Glide
                ImageView recipeImageView = recipeView.findViewById(R.id.recipe_image);
                if (!imageBase64.isEmpty()) {
                    byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                    Glide.with(this)
                            .asBitmap()
                            .load(imageBytes)
                            .into(recipeImageView);
                } else {
                    Log.d("RecipeImage", "Image base64 string is empty");
                }

                // Set author name and profile picture
                TextView authorNameTextView = recipeView.findViewById(R.id.author_name);
                ImageView profileImageView = recipeView.findViewById(R.id.profile_image);
                authorNameTextView.setText(authorName);
                if (!profilePicBase64.isEmpty()) {
                    byte[] profileImageBytes = Base64.decode(profilePicBase64, Base64.DEFAULT);
                    Glide.with(this)
                            .asBitmap()
                            .load(profileImageBytes)
                            .circleCrop()
                            .into(profileImageView);
                } else {
                    Log.d("ProfileImage", "Profile image base64 string is empty");
                }

                // Add an "Add to Meal Planner" button
                Button addToMealPlannerButton = new Button(this);
                addToMealPlannerButton.setText("Add to Meal Planner");
                addToMealPlannerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showAddToMealPlannerDialog(recipeObject);
                    }
                });

                // Set OnClickListener for the comment ImageView
                ImageView commentImageView = recipeView.findViewById(R.id.comment);
                commentImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCommentDialog(RecipeId);
                    }
                });

                // Add OnClickListener to thumbs up button
                ImageView thumbsUpImageView = recipeView.findViewById(R.id.thumbsup);
                thumbsUpImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UpdateRatingTask(RecipeId, "TU").execute();
                    }
                });

                // Add OnClickListener to thumbs down button
                ImageView thumbsDownImageView = recipeView.findViewById(R.id.thumbsdown);
                thumbsDownImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UpdateRatingTask(RecipeId, "TD").execute();
                    }
                });

                // add click listener to the "+ friend" button
                Button addFriendButton = recipeView.findViewById(R.id.makefriend);
                addFriendButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int currentUserID = userId; // Assuming userId represents the current user's ID
                        int friendID = 0; // Assuming author_id represents the ID of the person to be added as a friend
                        try {
                            friendID = recipeObject.getInt("Author_ID");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        addFriend(currentUserID, friendID);
                    }
                });

                // Add the button to the recipe view
                ((LinearLayout) recipeView).addView(addToMealPlannerButton);

                // Add the recipe view to the top of the container
                recipesContainer.addView(recipeView, 0);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse recipes data", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddToMealPlannerDialog(JSONObject recipeObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_to_meal_planner, null);
        builder.setView(dialogView);

        Spinner daySpinner = dialogView.findViewById(R.id.spinner_days);
        Spinner mealTypeSpinner = dialogView.findViewById(R.id.spinner_meal_types);

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(dayAdapter);

        ArrayAdapter<CharSequence> mealTypeAdapter = ArrayAdapter.createFromResource(this, R.array.meal_types, android.R.layout.simple_spinner_item);
        mealTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mealTypeSpinner.setAdapter(mealTypeAdapter);

        builder.setTitle("Add to Meal Planner");
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedDay = daySpinner.getSelectedItem().toString();
                String selectedMealType = mealTypeSpinner.getSelectedItem().toString();
                try {
                    int recipeId = recipeObject.getInt("Recipe_ID");
                    new community.AddToMealPlannerTask(recipeId, selectedDay, selectedMealType, userId).execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(community.this, "Failed to get recipe ID", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showCommentDialog(int recipeId) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_comments);

        EditText commentInput = dialog.findViewById(R.id.comment_input);
        Button postCommentButton = dialog.findViewById(R.id.post_comment_button);

        postCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentText = commentInput.getText().toString().trim();
                if (!commentText.isEmpty()) {
                    new AddCommentTask(recipeId, userId, commentText).execute();
                    dialog.dismiss();
                } else {
                    Toast.makeText(community.this, "Comment cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private class AddCommentTask extends AsyncTask<Void, Void, String> {
        private int recipeId;
        private int userId;
        private String commentText;

        public AddCommentTask(int recipeId, int userId, String commentText) {
            this.recipeId = recipeId;
            this.userId = userId;
            this.commentText = commentText;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/comment.php";
            RequestBody requestBody = new FormBody.Builder()
                    .add("recipe_id", String.valueOf(recipeId))
                    .add("user_id", String.valueOf(userId))
                    .add("comment", commentText)
                    .build();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .build();
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
            if (result != null) {
                Toast.makeText(community.this, "Comment posted successfully", Toast.LENGTH_SHORT).show();
                // Optionally, refresh comments section here
            } else {
                Toast.makeText(community.this, "Failed to post comment", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private class UpdateRatingTask extends AsyncTask<Void, Void, String> {
        private int recipeId;
        private String rating;

        public UpdateRatingTask(int recipeId, String rating) {
            this.recipeId = recipeId;
            this.rating = rating;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/rating.php";
            RequestBody requestBody = new FormBody.Builder()
                    .add("recipe_id", String.valueOf(recipeId))
                    .add("rating", rating)
                    .build();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .build();
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
            if (result != null) {
                Toast.makeText(community.this, "Rating updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(community.this, "Failed to update rating", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // Function to add friend to the database
    private void addFriend(int currentUserID, int friendID) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", String.valueOf(currentUserID))
                .add("friend_id", String.valueOf(friendID))
                .build();
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2709514/makefriend.php")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(community.this, "Failed to add friend. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("Friend added successfully")) {
                            Toast.makeText(community.this, "Friend added successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(community.this, "Failed to add friend", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    private void openProfilePage() {
        Intent intent = new Intent(community.this, Profile.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    private void openHomePage() {
        Intent intent = new Intent(community.this, homepage.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.home);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
//        Intent intent = new Intent(community.this, homepage.class);
//        intent.putExtra("user_id", userId);
//        intent.putExtra("selected_item_id", R.id.home);
//        startActivity(intent);
//        finish();
    }

    private void openFriendPage() {
        Intent intent = new Intent(community.this, friends.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.friends);
        startActivity(intent);
        finish();
    }private void openPlannerPage() {
        Intent intent = new Intent(community.this, weekplan.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.meal_planner);
        startActivity(intent);
        finish();
    }


}
