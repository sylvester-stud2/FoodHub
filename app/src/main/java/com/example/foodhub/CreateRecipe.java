package com.example.foodhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateRecipe extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    private static final String SERVER_URL = "https://lamp.ms.wits.ac.za/home/s2709514/CreateRecipe_yrr.php";
    private static final String[] PREDEFINED_INGREDIENTS = {
            "Apples", "Avocado", "Bacon", "Baking powder", "Banana", "Beans", "Beef", "Beetroot",
            "Berries", "Bread", "Broccoli", "Butter", "Butternut", "Carrot", "Cereal", "Cheese",
            "Chicken", "Cinnamon", "Cream", "Eggs", "Fish", "Flour", "Garlic", "Honey", "Lettuce",
            "Lemon", "Maize meal", "Mayonnaise", "Milk", "Mushrooms", "Oats", "Oil", "Onion",
            "Parsley", "Pasta", "Pepper", "Potatoes", "Rice", "Salt", "Soup powder", "Spinach",
            "Steak", "Sugar", "Tomato sauce", "Tomato", "Vanilla essence", "Vinegar", "Water",
            "Yeast", "Yogurt", "Stock (beef/chicken)"
    };

    private LinearLayout ingredientsLayout;
    private Button uploadImageButton;
    private ImageView imagePreview;
    private Button submitButton;
    private Uri imageUri;
    private int userId;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = getIntent();
        userId = intent.getIntExtra("user_id", -1);

        ingredientsLayout = findViewById(R.id.ingredientsLayout);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        imagePreview = findViewById(R.id.imagePreview);
        submitButton = findViewById(R.id.submitButton);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        populateIngredients();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRecipe();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                return handleNavigationItemSelected(item);
            }
        });
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

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imagePreview.setImageBitmap(bitmap);
                imagePreview.setVisibility(ImageView.VISIBLE);
                submitButton.setVisibility(Button.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateIngredients() {
        for (String ingredient : PREDEFINED_INGREDIENTS) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(ingredient);
            ingredientsLayout.addView(checkBox);
        }
    }

    private void submitRecipe() {
        EditText recipeNameField = findViewById(R.id.recipeName);
        EditText instructionsField = findViewById(R.id.instructions);

        String recipeName = recipeNameField.getText().toString();
        String instructions = instructionsField.getText().toString();
        List<String> selectedIngredients = new ArrayList<>();

        for (int i = 0; i < ingredientsLayout.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) ingredientsLayout.getChildAt(i);
            if (checkBox.isChecked()) {
                selectedIngredients.add(checkBox.getText().toString());
            }
        }

        if (recipeName.isEmpty() || selectedIngredients.isEmpty() || instructions.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and upload an image", Toast.LENGTH_LONG).show();
        } else {
            new UploadRecipeTask().execute(recipeName, instructions, selectedIngredients, imageUri.toString());
        }
    }

    private class UploadRecipeTask extends AsyncTask<Object, Void, String> {
        @Override
        protected String doInBackground(Object... params) {
            String recipeName = (String) params[0];
            String instructions = (String) params[1];
            List<String> selectedIngredients = (List<String>) params[2];
            String imagePath = (String) params[3];

            try {
                // Prepare the multipart request body
                MultipartBody.Builder builder = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("user_id", String.valueOf(userId))
                        .addFormDataPart("recipeName", recipeName)
                        .addFormDataPart("instructions", instructions);

                // Add ingredients
                for (String ingredient : selectedIngredients) {
                    builder.addFormDataPart("ingredients[]", ingredient);
                }

                // Add image
                InputStream inputStream = getContentResolver().openInputStream(Uri.parse(imagePath));
                byte[] imageBytes = new byte[inputStream.available()];
                inputStream.read(imageBytes);
                inputStream.close();

                builder.addFormDataPart("image", "image.jpg",
                        RequestBody.create(MediaType.parse("image/*"), imageBytes));

                RequestBody requestBody = builder.build();

                // Create request
                Request request = new Request.Builder()
                        .url(SERVER_URL)
                        .post(requestBody)
                        .build();

                // Execute request
                OkHttpClient client = new OkHttpClient();
                Response response = client.newCall(request).execute();
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(CreateRecipe.this, result, Toast.LENGTH_LONG).show();
        }
    }


    private void openHomePage() {
        Intent intent = new Intent(CreateRecipe.this, homepage.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openCommunityPage() {
        Intent intent = new Intent(CreateRecipe.this, community.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openFilterPage() {
        Intent intent = new Intent(CreateRecipe.this, dietplan.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openGroceryListPage() {
        Intent intent = new Intent(CreateRecipe.this, Grocery.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }

    private void openMealPlannerPage() {
        Intent intent = new Intent(CreateRecipe.this, weekplan.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
        finish();
    }
}

