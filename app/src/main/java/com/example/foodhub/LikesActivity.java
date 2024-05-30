package com.example.foodhub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class LikesActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private LinearLayout recipesContainer;
    private int userId;
    private Intent intent;
    private OkHttpClient client;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.likes);

        // Initialize views
        recipesContainer = findViewById(R.id.likes_container);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        // Get user ID from intent
        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        // Execute AsyncTask to fetch user data
        new FetchRecipesTask().execute();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener for bottom navigation
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });

        int selectedItemId = getIntent().getIntExtra("selected_item_id", R.id.home);
        bottomNavigationView.setSelectedItemId(selectedItemId);
    }

    private boolean handleNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == bottomNavigationView.getSelectedItemId()) {
            // Current item is already selected, do nothing
            return false;
        }

        if (item.getItemId() == R.id.home) {
            openHomePage();
        } else if (item.getItemId() == R.id.community) {
            openCommunityPage();
        } else if (item.getItemId() == R.id.friends) {
            openFriendPage();
        } else if (item.getItemId() == R.id.meal_planner) {
            openPlannerPage();
        }
        return true;
    }

    private void openHomePage() {
        Intent intent = new Intent(LikesActivity.this, homepage.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.home);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(LikesActivity.this, community.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.community);
        startActivity(intent);
        finish();
    }

    private void openFriendPage() {
        Intent intent = new Intent(LikesActivity.this, friends.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.friends);
        startActivity(intent);
        finish();
    }

    private void openPlannerPage() {
        Intent intent = new Intent(LikesActivity.this, weekplan.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("selected_item_id", R.id.meal_planner);
        startActivity(intent);
        finish();
    }

    private class FetchRecipesTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            // Include user_id in the API URL
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/getRCOMM2.php?user_id=" + userId;
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
                Toast.makeText(LikesActivity.this, "Failed to fetch recipes", Toast.LENGTH_SHORT).show();
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
                    byte[] profilePicBytes = Base64.decode(profilePicBase64, Base64.DEFAULT);
                    Glide.with(this)
                            .asBitmap()
                            .load(profilePicBytes)
                            .into(profileImageView);
                }

                // Set delete button click listener



                // Add the recipe view to the container
                recipesContainer.addView(recipeView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LikesActivity.this, "Failed to parse recipes", Toast.LENGTH_SHORT).show();
        }
    }

    private void showCommentDialog(String recipeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Comment");

        final EditText commentEditText = new EditText(this);
        builder.setView(commentEditText);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String comment = commentEditText.getText().toString();
                submitComment(recipeId, comment);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void submitComment(String recipeId, String comment) {
        String url = "https://lamp.ms.wits.ac.za/home/s2709514/addComment.php";
        RequestBody requestBody = new FormBody.Builder()
                .add("recipe_id", recipeId)
                .add("user_id", String.valueOf(userId))
                .add("comment", comment)
                .build();
        Request request = new Request.Builder().url(url).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LikesActivity.this, "Failed to submit comment", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LikesActivity.this, "Comment submitted", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LikesActivity.this, "Failed to submit comment", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void confirmDeleteRecipe(int recipeId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Recipe");
        builder.setMessage("Are you sure you want to delete this recipe?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteRecipe(recipeId);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void deleteRecipe(int recipeId) {
        String url = "https://lamp.ms.wits.ac.za/home/s2709514/delete_recipe.php";
        RequestBody requestBody = new FormBody.Builder()
                .add("Recipe_ID", String.valueOf(recipeId))
                .build();
        Request request = new Request.Builder().url(url).post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(LikesActivity.this, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LikesActivity.this, "Recipe deleted", Toast.LENGTH_SHORT).show();
                            // Refresh the list of recipes after deletion
                            new FetchRecipesTask().execute();
                        }
                    });
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(LikesActivity.this, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
