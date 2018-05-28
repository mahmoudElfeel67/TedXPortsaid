package com.bloomers.tedxportsaid.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.CustomView.onboarder.AhoyOnboarderActivity;
import com.bloomers.tedxportsaid.CustomView.onboarder.AhoyOnboarderCard;
import com.bloomers.tedxportsaid.R;

import java.util.ArrayList;
import java.util.List;

public class WalkThroughActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(makePage("بلا بلا بلا !", "بلا بلا بلااااا", R.drawable.speaker));
        pages.add(makePage("بلا بلا بلا !", "بلا بلا بلااااا", R.drawable.speaker));
        pages.add(makePage("بلا بلا بلا !", "بلا بلا بلااااا", R.drawable.speaker));

        setFont(Typeface.createFromAsset(getAssets(),AppController.mediumFont));
        setOnboardPages(pages);
        showNavigationControls(true);
        setFinishButtonTitle("تمام");


    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().hideNavBar(this);
    }

    private AhoyOnboarderCard makePage(String title, String Description, int drawable) {
        AhoyOnboarderCard ahoyOnboarderCard = new AhoyOnboarderCard(title, Description, drawable);
        ahoyOnboarderCard.setTitleTextSize(22);
        ahoyOnboarderCard.setDescriptionTextSize(13);
        if (getResources().getBoolean(R.bool.isTablet)) {
            ahoyOnboarderCard.setTitleTextSize(28);
            ahoyOnboarderCard.setDescriptionTextSize(16);
            ahoyOnboarderCard.setIconLayoutParams((int) dpToPixels(200, this), (int) dpToPixels(200, this), (int) dpToPixels(150, this), (int) dpToPixels(60, this), (int) dpToPixels(60, this), (int) dpToPixels(60, this));
        }
        return ahoyOnboarderCard;
    }

    @Override
    public void onFinishButtonPressed() {
        SharedPreferences sharedPrefereces = getSharedPreferences("My App", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefereces.edit();
        editor.putBoolean("isSlideShown", true);
        editor.apply();
        startActivity(new Intent(this,  MainActivity.class));
        finish();

    }
}
