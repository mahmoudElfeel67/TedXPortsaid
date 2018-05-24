package com.bloomers.tedxportsaid.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.bloomers.tedxportsaid.AppController;
public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppController.getInstance().implementOthers(this);
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }
}
