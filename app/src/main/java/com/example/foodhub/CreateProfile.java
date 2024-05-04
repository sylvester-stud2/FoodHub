package com.example.foodhub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

public class CreateProfile extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new_page is just new_page.xml
        setContentView(R.layout.createprofile);


        Button btn = findViewById(R.id.back);
        Button btno = findViewById(R.id.tologin);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v){
                Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                startActivity(intent);
            }

        });

        btno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }


}
