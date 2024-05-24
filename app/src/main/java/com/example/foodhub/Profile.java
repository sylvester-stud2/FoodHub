package com.example.foodhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Profile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageView profile_image;
    EditText name_txt;
    EditText last_name;
    EditText email_txt;
    Button saveChangesButton;

    Intent intent;
    String email;
    String apiUrlGetUserInfo;
    String apiUrlUpdateUserInfo;
    String newEmail; // Define newEmail variable to store updated email

    private OkHttpClient client;
    private Response response;
    private Request request;
    String strJson;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Initialize views
        profile_image = findViewById(R.id.profilePicture);
        name_txt = findViewById(R.id.firstName);
        last_name = findViewById(R.id.lastNameTextView);
        email_txt = findViewById(R.id.emailTextView);
        saveChangesButton = findViewById(R.id.SaveChangesButton);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");

        // Get email from intent
        intent = getIntent();
        email = intent.getStringExtra("email");

        // Construct API URLs
        apiUrlGetUserInfo = "https://lamp.ms.wits.ac.za/home/s2709514/ProfileAndName.php?email=" + email;
        apiUrlUpdateUserInfo = "https://lamp.ms.wits.ac.za/home/s2709514/update_user.php";

        // Execute AsyncTask to fetch user data
        new GetUserDataRequest().execute();

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Handle home navigation
                    Intent intent = new Intent(Profile.this, homepage.class);
                    intent.putExtra("email", newEmail); // Pass the updated email
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.community) {
                    // Handle community navigation
                    Intent intent = new Intent(Profile.this, community.class);
                    intent.putExtra("email", newEmail); // Pass the updated email
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.filter) {
                    // Handle filter navigation
                    Intent intent = new Intent(Profile.this, dietplan.class);
                    intent.putExtra("email", newEmail); // Pass the updated email
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.grocery_list) {
                    // Handle grocery list navigation
                    Intent intent = new Intent(Profile.this, Grocery.class);
                    intent.putExtra("email", newEmail); // Pass the updated email
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.meal_planner) {
                    // Handle meal planner navigation
                    Intent intent = new Intent(Profile.this, weekplan.class);
                    intent.putExtra("email", newEmail); // Pass the updated email
                    startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });


        // Set listener for Save Changes button
        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
            }
        });
    }

    public class GetUserDataRequest extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show(); // Show the ProgressDialog here
        }

        @Override
        protected String doInBackground(Void... voids) {
            // Create a form-encoded request body
            request = new Request.Builder().url(apiUrlGetUserInfo).build();
            try {
                response = client.newCall(request).execute();
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
                updateUserData(result);
            } else {
                // Handle the error
                Toast.makeText(Profile.this, "Failed to fetch user data", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss(); // Dismiss the ProgressDialog here
        }
    }
    private void updateUserData(String strJson) {
        try {
            JSONArray parent = new JSONArray(strJson);
            JSONObject child = parent.getJSONObject(0);
            String imgBase64 = child.optString("profile_picture", "");

            String firstName = child.getString("first_name");
            String lastName = child.getString("last_name");

            if (!imgBase64.isEmpty()) {
                // Decode base64 to byte array
                byte[] imageBytes = Base64.decode(imgBase64, Base64.DEFAULT);
                // Convert byte array to Bitmap
                InputStream inputStream = new ByteArrayInputStream(imageBytes);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Use Glide to load the Bitmap into the ImageView
                Glide.with(this)
                        .load(bitmap)
                        .into(profile_image);
            }

            name_txt.setText(firstName);
            last_name.setText(lastName);
            email_txt.setText(email);
            newEmail = email; // Set newEmail to the initial email value
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse user data", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserInfo() {
        // Get the updated information
        String newFirstName = name_txt.getText().toString().trim();
        String newLastName = last_name.getText().toString().trim();
        newEmail = email_txt.getText().toString().trim(); // Update newEmail with the edited email

        // Execute AsyncTask to update user information
        new UpdateUserInfoRequest().execute(newFirstName, newLastName, newEmail);
    }

    public class UpdateUserInfoRequest extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String newFirstName = params[0];
            String newLastName = params[1];
            String newEmail = params[2];

            // Create form body with updated information
            RequestBody formBody = new FormBody.Builder()
                    .add("old_email", email)
                    .add("new_email", newEmail)
                    .add("first_name", newFirstName)
                    .add("last_name", newLastName)
                    .build();

            // Create POST request to update user information
            Request request = new Request.Builder()
                    .url(apiUrlUpdateUserInfo)
                    .post(formBody)
                    .build();

            try {
                // Execute request
                response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Handle response after updating user information
            if (result != null && result.equals("{\"status\":\"success\"}")) {
                Toast.makeText(Profile.this, "User information updated successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Profile.this, "Failed to update user information", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



