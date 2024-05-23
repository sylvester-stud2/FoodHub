package com.example.foodhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeProfile extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Uri imageUri;
    private OkHttpClient client;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeprofile);

        // Get email from Intent
        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        Button backToLogin = findViewById(R.id.back);

        backToLogin.setOnClickListener(v -> {
            Intent backIntent = new Intent(ChangeProfile.this, Profile.class);
            backIntent.putExtra("email", email);
            startActivity(backIntent);
            finish();
        });

        imageView = findViewById(R.id.imagePreview);
        Button selectImageButton = findViewById(R.id.uploadImageButton);
        Button uploadImageButton = findViewById(R.id.submitButton);
        client = new OkHttpClient();

        selectImageButton.setOnClickListener(v -> openImagePicker());
        uploadImageButton.setOnClickListener(v -> uploadProfilePicture());
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadProfilePicture() {
        if (imageUri == null) {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            byte[] imageBytes = readBytes(inputStream);

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("email", email)
                    .addFormDataPart("profile_picture", "profile_picture.jpg",
                            RequestBody.create(MediaType.parse("image/jpeg"), imageBytes))
                    .build();

            Request request = new Request.Builder()
                    .url("https://lamp.ms.wits.ac.za/home/s2709514/ChangeProfile.php")
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(ChangeProfile.this, "Network error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    final String responseBody = response.body().string();
                    runOnUiThread(() -> {
                        Toast.makeText(ChangeProfile.this, responseBody, Toast.LENGTH_SHORT).show();
                        if (response.isSuccessful()) {
                            Toast.makeText(ChangeProfile.this, "Profile picture updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ChangeProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to read image", Toast.LENGTH_SHORT).show();
        }
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        return byteArrayOutputStream.toByteArray();
    }
}
