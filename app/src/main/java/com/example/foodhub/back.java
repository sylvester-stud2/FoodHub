package com.example.foodhub;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

public class back extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        new_page is just new_page.xml
        setContentView(R.layout.welcome);
    }
}
