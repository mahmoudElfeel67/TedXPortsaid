package com.bloomers.tedxportsaid.Activity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.CustomView.onboarder.AhoyOnboarderActivity;
import com.bloomers.tedxportsaid.CustomView.onboarder.AhoyOnboarderCard;
import com.bloomers.tedxportsaid.Fragment.VideosFragment;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideos;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTask;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTaskInterface;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeChannel;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.bloomers.tedxportsaid.Fragment.VideosFragment.tedxPortsaidChnnel;

public class WalkThroughActivity extends AhoyOnboarderActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<AhoyOnboarderCard> pages = new ArrayList<>();
        pages.add(makePage());
        pages.add(makePage());
        pages.add(makePage());

        setFont(Typeface.createFromAsset(getAssets(),AppController.mediumFont));
        setOnboardPages(pages);
        showNavigationControls(true);
        setFinishButtonTitle("تمام");

        if (AppController.getInstance().isThereInternet(this)){
            new GetChannelVideosTask(new GetChannelVideos(), new YouTubeChannel(tedxPortsaidChnnel, "asdas"))
                    .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                        @Override
                        public void onGetVideos(List<YouTubeVideo> videos) {
                            if (videos != null) {
                                VideosFragment.videos = new ArrayList<>();
                                VideosFragment.videos.addAll(videos);
                                if (!getSharedPreferences("TEDX", Context.MODE_PRIVATE).getBoolean("isSaved",false)){
                                    try {
                                        if (tedxPortsaidChnnel.equals(tedxPortsaidChnnel)){
                                            for (YouTubeVideo tubeVideo :VideosFragment.videos){
                                                try {
                                                    YouTubeVideo.storeFilter(tubeVideo,WalkThroughActivity.this);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            getSharedPreferences("TEDX", Context.MODE_PRIVATE).edit().putBoolean("isSaved",true).apply();
                                        }
                                    }catch (Exception e){

                                    }
                                }
                            }

                        }
                    }).executeInParallel();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        AppController.getInstance().hideNavBar(this);
    }

    private AhoyOnboarderCard makePage() {
        AhoyOnboarderCard ahoyOnboarderCard = new AhoyOnboarderCard("بلا بلا بلا !", "بلا بلا بلااااا", R.drawable.speaker);
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
        SharedPreferences sharedPrefereces = getSharedPreferences("TEDX", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefereces.edit();
        editor.putBoolean("isSlideShown", true);
        editor.apply();
        startActivity(new Intent(this,  MainActivity.class));
        finish();

    }
}
