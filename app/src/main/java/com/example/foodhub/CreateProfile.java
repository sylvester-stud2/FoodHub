package com.example.foodhub;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class CreateProfile extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, passwordEditText, reEnterPasswordEditText;

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

                if (!password.equals(reEnterPassword)) {
                    Toast.makeText(CreateProfile.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    new SignUpTask().execute(firstName, lastName, email, password);
                }
            }
        });
    }

    private class SignUpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            String firstName = params[0];
            String lastName = params[1];
            String email = params[2];
            String password = params[3];

            try {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));
                StringBuilder sb = new StringBuilder();
                for (byte b : hashedPassword) {
                    sb.append(String.format("%02x", b));
                }
                password = sb.toString();

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

                return conn.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Integer responseCode) {
            if (responseCode != null && responseCode == 200) {
                Toast.makeText(CreateProfile.this, "Sign up successful", Toast.LENGTH_SHORT).show();

                // Create an Intent to start the homepage activity
                Intent intent = new Intent(CreateProfile.this, homepage.class);
                startActivity(intent);

                // Close the CreateProfile activity
                finish();
            } else {
                Toast.makeText(CreateProfile.this, "Sign up failed", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
