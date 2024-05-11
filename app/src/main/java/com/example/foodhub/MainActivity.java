package com.example.foodhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText username;
    EditText password;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        Button btn = findViewById(R.id.btn1);
        Button btno = findViewById(R.id.btn2);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v){

                if(username.getText().toString().equals("user") && password.getText().toString().equals("1234")){
                    Toast.makeText(MainActivity.this,"Login Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, homepage.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(MainActivity.this,"Login Failed!",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent);

                }



            }

        });

        btno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateProfile.class);
                startActivity(intent);
            }
        });




    }

}