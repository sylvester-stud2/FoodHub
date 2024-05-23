package com.example.foodhub;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;

    private static final String SERVER_URL = "https://lamp.ms.wits.ac.za/home/s2709514/USERppUp.php";

    private Button btnSelect, btnUpload;
    private ImageView imagePreview;
    private Uri imageUri;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changeprofile);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        btnSelect = findViewById(R.id.btnSelect);
        btnUpload = findViewById(R.id.btnUpload);
        imagePreview = findViewById(R.id.imagePreview);

        btnSelect.setOnClickListener(v -> openFileChooser());
        btnUpload.setOnClickListener(v -> uploadImage());

        Button backToProfile = findViewById(R.id.back);


        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChangeProfile.this, Profile.class);
                intent.putExtra("email",email);
                startActivity(intent);
                finish();
            }
        });

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
                btnUpload.setVisibility(Button.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage() {
        if (imageUri == null) {
            Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            return;
        }

        OkHttpClient client = new OkHttpClient();

        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] imageBytes = bos.toByteArray();

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("image", "image.jpg", RequestBody.create(MediaType.parse("image/*"), imageBytes))
                    .addFormDataPart("email", email)
                    .build();

            Request request = new Request.Builder()
                    .url(SERVER_URL)
                    .post(requestBody)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(ChangeProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        runOnUiThread(() -> Toast.makeText(ChangeProfile.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show());
                    } else {
                        runOnUiThread(() -> Toast.makeText(ChangeProfile.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
                    }
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
