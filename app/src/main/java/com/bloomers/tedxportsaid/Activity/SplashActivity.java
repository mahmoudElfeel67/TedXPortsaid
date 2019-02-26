package com.bloomers.tedxportsaid.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bloomers.tedxportsaid.AppController;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getInstance().implementOthers(this);

        SharedPreferences sharedPrefereces = getSharedPreferences("TEDX", MODE_PRIVATE);
        Class classToGO;
        if (sharedPrefereces.getBoolean("isSlideShown", false)) {
            classToGO = MainActivity.class;

        } else {
            classToGO = WalkThroughActivity.class;


        }
        startActivity(new Intent(SplashActivity.this, classToGO));
        finish();



    }
}
