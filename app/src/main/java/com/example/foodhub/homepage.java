package com.example.foodhub;

import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
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

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.RequestBody;
import okhttp3.FormBody;

public class homepage extends AppCompatActivity {
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
        setContentView(R.layout.homepage);

        Button CreateRecipe = findViewById(R.id.add_recipe);
        CreateRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(homepage.this, CreateRecipe.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                finish();
            }
        });

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

        // Construct API URL
        String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/getRecipes.php?user_id=" + userId;

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
                Toast.makeText(homepage.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
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
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/getRecipes.php?user_id=" + userId;
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
                Toast.makeText(homepage.this, "Failed to fetch recipes", Toast.LENGTH_SHORT).show();
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

                View recipeView = getLayoutInflater().inflate(R.layout.recipe_item, null);
                TextView recipeIdTextView = recipeView.findViewById(R.id.recipe_id);
                TextView titleTextView = recipeView.findViewById(R.id.recipe_title);
                TextView instructionsTextView = recipeView.findViewById(R.id.recipe_instructions);
                ImageView recipeImageView = recipeView.findViewById(R.id.recipe_image);
                Button optionsButton = recipeView.findViewById(R.id.options_button);

                recipeIdTextView.setText("Recipe ID: " + recipeId);
                titleTextView.setText(title);
                instructionsTextView.setText(instructions);

                // Decode base64 image and set to ImageView
                byte[] imageBytes = Base64.decode(imageBase64, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                recipeImageView.setImageBitmap(bitmap);

                optionsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showOptionsDialog(recipeObject);
                    }
                });

                recipesContainer.addView(recipeView);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse recipes data", Toast.LENGTH_SHORT).show();
        }
    }


    private void showOptionsDialog(JSONObject recipeObject) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setItems(new CharSequence[]{ "Post","Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Handle delete action
                                PostRecipe(recipeObject);
                                break;

                            case 1:
                                // Handle delete action
                                deleteRecipe(recipeObject);
                                break;
                        }
                    }
                });
        builder.show();
    }


    private void deleteRecipe(JSONObject recipeObject) {
        // Implement the logic for deleting the recipe
        // For example, you can make an API call to delete the recipe from the server
        try {
            int recipeId = recipeObject.getInt("Recipe_ID");
            new DeleteRecipeTask().execute(recipeId);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to delete recipe", Toast.LENGTH_SHORT).show();
        }
    }

    private class DeleteRecipeTask extends AsyncTask<Integer, Void, Boolean> {
        private String errorMessage = "Failed to delete recipe";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int recipeId = params[0];
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/deleteRecipe.php";
            RequestBody formBody = new FormBody.Builder()
                    .add("recipe_id", String.valueOf(recipeId))
                    .build();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(formBody)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    if (jsonResponse.getBoolean("success")) {
                        return true;
                    } else {
                        errorMessage = jsonResponse.getString("error");
                        return false;
                    }
                } else {
                    errorMessage = "Server returned error: " + response.code();
                    return false;
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                errorMessage = "Exception: " + e.getMessage();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            progressDialog.dismiss();
            if (success) {
                Toast.makeText(homepage.this, "Recipe deleted successfully", Toast.LENGTH_SHORT).show();
                // Refresh recipes list
                recipesContainer.removeAllViews();
                new FetchRecipesTask().execute();
            } else {
                Toast.makeText(homepage.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void PostRecipe(JSONObject recipeObject) {
        try {
            int recipeId = recipeObject.getInt("Recipe_ID");

            new PostRecipeTask().execute(recipeId);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to get recipe ID", Toast.LENGTH_SHORT).show();
        }
    }

    private class PostRecipeTask extends AsyncTask<Integer, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            int recipeId = params[0];
            String apiUrl = "https://lamp.ms.wits.ac.za/home/s2709514/addPost.php";
            RequestBody requestBody = new FormBody.Builder()
                    .add("Author_ID", String.valueOf(userId))
                    .add("Recipe_ID", String.valueOf(recipeId))
                    .build();
            Request request = new Request.Builder()
                    .url(apiUrl)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                return response.isSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            if (result) {
                Toast.makeText(homepage.this, "Recipe posted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(homepage.this, "Failed to post recipe", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openProfilePage() {
        Intent intent = new Intent(homepage.this, Profile.class);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openHomePage() {
        Intent intent = new Intent(homepage.this, community.class);
        intent.putExtra("selected_item_id", R.id.home);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(homepage.this, community.class);
        intent.putExtra("selected_item_id", R.id.community);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(homepage.this, dietplan.class);
        intent.putExtra("selected_item_id", R.id.filter);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {
        Intent intent = new Intent(homepage.this, Grocery.class);
        intent.putExtra("selected_item_id", R.id.grocery_list);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(homepage.this, weekplan.class);
        intent.putExtra("selected_item_id", R.id.meal_planner);
        intent.putExtra("user_id", userId);
        overridePendingTransition(0, 0);
        startActivity(intent);
        finish();
    }
}
