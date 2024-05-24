package com.example.foodhub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class CreateProfile extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, reEnterPasswordEditText;
    ImageView profile_image;
    TextView name_txt;
    Intent intent;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createprofile);

        firstNameEditText = findViewById(R.id.editTextText);
        lastNameEditText = findViewById(R.id.editTextText4);
        emailEditText = findViewById(R.id.editTextText6);
        passwordEditText = findViewById(R.id.editTextText3);
        reEnterPasswordEditText = findViewById(R.id.editTextText5);

        Button signUpButton = findViewById(R.id.tohomepage);
        Button backToLogin = findViewById(R.id.back);

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String reEnterPassword = reEnterPasswordEditText.getText().toString();

                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || reEnterPassword.isEmpty()) {
                    Toast.makeText(CreateProfile.this, "All fields must be filled", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(reEnterPassword)) {
                    Toast.makeText(CreateProfile.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    new SignUpTask().execute(firstName, lastName, email, password);
                }
            }
        });
    }

    private class SignUpTask extends AsyncTask<String, Void, Integer> {
        private String email;
        private int userId;

        @Override
        protected Integer doInBackground(String... params) {
            String firstName = params[0];
            String lastName = params[1];
            email = params[2];
            String password = params[3];

            try {
                URL url = new URL("https://lamp.ms.wits.ac.za/home/s2709514/ADD.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);

                String data = "FIRST_NAME=" + URLEncoder.encode(firstName, "UTF-8") +
                        "&LAST_NAME=" + URLEncoder.encode(lastName, "UTF-8") +
                        "&EMAIL=" + URLEncoder.encode(email, "UTF-8") +
                        "&PASSWORD=" + URLEncoder.encode(password, "UTF-8");

                OutputStream os = conn.getOutputStream();
                os.write(data.getBytes());
                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == 200) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String inputLine;

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());
                    userId = jsonResponse.getInt("user_id");
                    Log.d("SignUpTask", "Received user_id: " + userId);
                }
                return responseCode;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode != null && responseCode == 200) {
                Toast.makeText(CreateProfile.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CreateProfile.this, homepage.class);
                intent.putExtra("user_id", userId); // Use user_id instead of email
                startActivity(intent);
                finish();
            } else if (responseCode != null && responseCode == 409) {
                Toast.makeText(CreateProfile.this, "Email already exists", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CreateProfile.this, "Sign up failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
