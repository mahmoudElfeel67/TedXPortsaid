package com.bloomers.tedxportsaidadmin.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase.getInstance().getReference().child("speaker_questions").keepSynced(true);
        FirebaseDatabase.getInstance().getReference().child("Speakers").keepSynced(true);

        startActivity(new Intent(SplashActivity.this,  MainActivity.class));
        finish();



    }
}
