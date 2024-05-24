package com.example.foodhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;


public class Profile extends AppCompatActivity {
    private AlertDialog dialog;

    BottomNavigationView bottomNavigationView;
    ImageView profile_image;
    EditText name_txt;
    Button changePasswordButton;
    Button deleteAccountButton;
    EditText last_name;
    EditText email_txt;
    Button saveChangesButton;

    Intent intent;

    int userId;
    String apiUrlGetUserInfo;
    String apiUrlUpdateUserInfo;


    private OkHttpClient client;
    private Response response;
    private Request request;
    Button ChangeProfilePicture;
    String strJson;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        deleteAccountButton = findViewById(R.id.deleteProfileButton);

        // Initialize views
        profile_image = findViewById(R.id.profilePicture);
        name_txt = findViewById(R.id.firstName);
        last_name = findViewById(R.id.lastNameTextView);
        email_txt = findViewById(R.id.emailTextView);
        saveChangesButton = findViewById(R.id.SaveChangesButton);
        ChangeProfilePicture = findViewById(R.id.uploadImageButton);

        // Initialize OkHttpClient
        client = new OkHttpClient();

        // Initialize progressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");



        intent = getIntent();

        userId = intent.getIntExtra("user_id", -1);

        // Construct API URLs
        apiUrlGetUserInfo = "https://lamp.ms.wits.ac.za/home/s2709514/infoProfile.php?user_id=" + userId;

        apiUrlUpdateUserInfo = "https://lamp.ms.wits.ac.za/home/s2709514/update_user.php";

        // Execute AsyncTask to fetch user data
        new GetUserDataRequest().execute();



        ChangeProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, ChangeProfile.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
                finish();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.home) {
                    // Handle home navigation
                    Intent intent = new Intent(Profile.this, homepage.class);
                    intent.putExtra("user_id", userId);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.community) {
                    // Handle community navigation
                    Intent intent = new Intent(Profile.this, community.class);

                    intent.putExtra("user_id", userId);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.filter) {
                    // Handle filter navigation
                    Intent intent = new Intent(Profile.this, dietplan.class);
                    intent.putExtra("user_id", userId);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.grocery_list) {
                    // Handle grocery list navigation
                    Intent intent = new Intent(Profile.this, Grocery.class);
                    intent.putExtra("user_id", userId);
                    overridePendingTransition(0, 0);
                    startActivity(intent);
                    finish();
                    return true;
                } else if (itemId == R.id.meal_planner) {
                    // Handle meal planner navigation
                    Intent intent = new Intent(Profile.this, weekplan.class);
                    intent.putExtra("user_id", userId);
                    overridePendingTransition(0, 0);
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
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to change password
                changePassword();
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Call method to delete account
                deleteAccount();
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
            String email = child.getString("email");

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

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to parse user data", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUserInfo() {
        // Get the updated information
        String newFirstName = name_txt.getText().toString().trim();
        String newLastName = last_name.getText().toString().trim();
        String newEmail = email_txt.getText().toString().trim();


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
                    .add("user_id", String.valueOf(userId))
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

    private class ChangePasswordTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String userId = params[0];
            String oldPassword = params[1];
            String newPassword = params[2];

            OkHttpClient client = new OkHttpClient();

            // Define the request body with user ID, old password, and new password
            RequestBody requestBody = new FormBody.Builder()
                    .add("user_id", userId)
                    .add("old_password", oldPassword)
                    .add("new_password", newPassword)
                    .build();

            // Define the request
            Request request = new Request.Builder()
                    .url("https://lamp.ms.wits.ac.za/home/s2709514/change_password.php") // Replace with your actual URL
                    .post(requestBody)
                    .build();

            try {
                // Execute the request
                Response response = client.newCall(request).execute();
                String responseBody = response.body().string();
                Log.d("ChangePasswordTask", "Response: " + responseBody);
                return responseBody;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Handle response after changing password
            if (result != null) {
                Log.d("ChangePasswordTask", "Result: " + result);
                try {
                    JSONObject jsonResponse = new JSONObject(result);
                    String status = jsonResponse.getString("status");
                    if (status.equals("success")) {
                        Toast.makeText(Profile.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        String message = jsonResponse.getString("message");
                        Toast.makeText(Profile.this, "Failed to change password: " + message, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Profile.this, "Failed to change password: Invalid response", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Profile.this, "Failed to change password: No response from server", Toast.LENGTH_SHORT).show();
            }
            // Dismiss the dialog here to ensure it's dismissed after the task completes
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private void changePassword() {
        // Inflate the change_password.xml layout
        View changePasswordView = getLayoutInflater().inflate(R.layout.change_password, null);

        // Find views within the layout
        EditText oldPasswordEditText = changePasswordView.findViewById(R.id.oldPasswordEditText);
        EditText newPasswordEditText = changePasswordView.findViewById(R.id.newPasswordEditText);
        EditText confirmPasswordEditText = changePasswordView.findViewById(R.id.confirmPasswordEditText);
        Button changePasswordButton = changePasswordView.findViewById(R.id.changePasswordButton);

        // Create AlertDialog to show the change password form
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(changePasswordView);
        dialog = builder.create();
        dialog.show();

        // Set click listener for the change password button
        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered passwords
                String oldPassword = oldPasswordEditText.getText().toString().trim();
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmPassword = confirmPasswordEditText.getText().toString().trim();

                // Perform validation
                if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(Profile.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(Profile.this, "New password and confirm password do not match", Toast.LENGTH_SHORT).show();
                } else {
                    // Call AsyncTask to change the password
                    new ChangePasswordTask().execute(String.valueOf(userId), oldPassword, newPassword);
                }
            }
        });
    }


    // Method to delete account
    private void deleteAccount() {
        // Check if userId is valid
        if (userId == -1) {
            Toast.makeText(this, "Invalid user ID. Cannot delete account.", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        // Define the request body with the user's ID
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", String.valueOf(userId))
                .build();

        // Define the request
        Request request = new Request.Builder()
                .url("https://lamp.ms.wits.ac.za/home/s2709514/delete_user.php") // Replace with the actual URL of your PHP script
                .post(requestBody)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure (e.g., display an error message)
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Profile.this, "Failed to delete user account. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                // Handle response
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonResponse = new JSONObject(responseData);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");
                            if (status.equals("success")) {
                                // User account deleted successfully
                                Toast.makeText(Profile.this, message, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Profile.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // Failed to delete user account
                                Toast.makeText(Profile.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing error
                            Toast.makeText(Profile.this, "An error occurred. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


}



