package com.example.foodhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class RecipeDetailActivity extends AppCompatActivity {

    private static final String TAG = "RecipeDetailActivity";
    Intent intent;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Button backToLogin = findViewById(R.id.back);
        intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecipeDetailActivity.this, weekplan.class);
                intent.putExtra("user_id", userId);
                overridePendingTransition(0, 0);
                startActivity(intent);
                finish();
            }
        });

        // Get the JSON string containing recipe details from the intent
        String jsonString = getIntent().getStringExtra("recipe_details");
        Log.d(TAG, "Received JSON: " + jsonString);

        try {
            // Parse the JSON string to extract recipe details
            JSONObject jsonObject = new JSONObject(jsonString);
            String recipeTitle = jsonObject.optString("Recipe_Title");
            String recipeInstructions = jsonObject.optString("Recipe_Instructions");
            String recipeImage = jsonObject.optString("Recipe_Image");

            // Display the recipe details in the UI
            TextView recipeTitleTextView = findViewById(R.id.recipe_name);
            TextView recipeInstructionsTextView = findViewById(R.id.recipe_instructions);
            ImageView recipeImageView = findViewById(R.id.recipe_image);

            if (recipeTitleTextView != null) {
                recipeTitleTextView.setText(recipeTitle);
            }

            if (recipeInstructionsTextView != null) {
                recipeInstructionsTextView.setText(recipeInstructions);
            }

            if (recipeImageView != null && recipeImage != null && !recipeImage.isEmpty()) {
                // Decode base64 string to bitmap and set to ImageView
                Bitmap bitmap = decodeBase64(recipeImage);
                recipeImageView.setImageBitmap(bitmap);
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON parsing error", e);
            Toast.makeText(this, "Failed to load recipe details: JSON parsing error", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to decode base64 string to bitmap
    private Bitmap decodeBase64(String base64Str) throws IllegalArgumentException {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
