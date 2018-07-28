package com.bloomers.tedxportsaid.Activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

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
        pages.add(makePage(R.string.schedule,R.string.schedule_desc,R.drawable.schedule));
        pages.add(makePage(R.string.speakers,R.string.schedule_speakers,R.drawable.speaker));
        pages.add(makePage(R.string.videos,R.string.schedule_videos,R.drawable.videos));

        setFont(Typeface.createFromAsset(getAssets(),AppController.mediumFont));
        setOnboardPages(pages);
        showNavigationControls(true);
        setFinishButtonTitle(R.string.finish);

    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().hideNavBar(this);
    }

    private AhoyOnboarderCard makePage(@StringRes  int title,@StringRes int desc, @DrawableRes int drawable) {
        AhoyOnboarderCard ahoyOnboarderCard = new AhoyOnboarderCard(title, desc,drawable);
        ahoyOnboarderCard.setTitleTextSize(21);
        ahoyOnboarderCard.setDescriptionTextSize(14);
        if (getResources().getBoolean(R.bool.isTablet)) {
            ahoyOnboarderCard.setTitleTextSize(28);
            ahoyOnboarderCard.setDescriptionTextSize(16);
            ahoyOnboarderCard.setIconLayoutParams((int) dpToPixels(200, this), (int) dpToPixels(200, this), (int) dpToPixels(150, this), (int) dpToPixels(60, this), (int) dpToPixels(60, this), (int) dpToPixels(60, this));
        }
        return ahoyOnboarderCard;
    }

    @Override
    public void onFinishButtonPressed() {
        SharedPreferences sharedPrefereces = getSharedPreferences("TEDX", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefereces.edit();
        editor.putBoolean("isSlideShown", true);
        editor.apply();
        startActivity(new Intent(this,  MainActivity.class));
        finish();

    }
}
