package com.example.foodhub;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreateRecipe extends AppCompatActivity {
    Intent intent;
    String email;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String[] PREDEFINED_INGREDIENTS = {
            "Flour", "Sugar", "Eggs", "Butter", "Salt", "Milk", "Baking Powder", "Vanilla Extract"
    };

    private LinearLayout ingredientsLayout;
    private Button uploadImageButton;
    private ImageView imagePreview;
    private Button submitButton;
    private Uri imageUri;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        intent = getIntent();
        email = intent.getStringExtra("email");

        ingredientsLayout = findViewById(R.id.ingredientsLayout);
        uploadImageButton = findViewById(R.id.uploadImageButton);
        imagePreview = findViewById(R.id.imagePreview);
        submitButton = findViewById(R.id.submitButton);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        populateIngredients();

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
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
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Handle home navigation
                    Intent intent = new Intent(CreateRecipe.this, homepage.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.community) {
                    // Handle community navigation
                    Intent intent = new Intent(CreateRecipe.this, community.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();return true;
                } else if (itemId == R.id.filter) {
                    // Handle filter navigation
                    Intent intent = new Intent(CreateRecipe.this, dietplan.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();return true;
                } else if (itemId == R.id.grocery_list) {
                    // Handle grocery list navigation
                    Intent intent = new Intent(CreateRecipe.this, homepage.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();return true;
                } else if (itemId == R.id.meal_planner) {
                    // Handle meal planner navigation
                    Intent intent = new Intent(CreateRecipe.this, weekplan.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    private void populateIngredients() {
        for (String ingredient : PREDEFINED_INGREDIENTS) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(ingredient);
            ingredientsLayout.addView(checkBox);
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imagePreview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
            // Handle the submission logic here, such as saving the data to a database or sending it to a server
            Toast.makeText(this, "Recipe submitted!", Toast.LENGTH_LONG).show();
        }
    }
}
