package com.bloomers.tedxportsaid.Dialog;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Adapter.VideosAdapter;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

public class YoutubeFragment extends DialogFragment implements YouTubePlayer.OnInitializedListener {

    private static String VIDEO_ID = "dARAN1z2KqY";
    private YouTubePlayer youTubePlayer;
    private Boolean isIntilize = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_video, container);
        if (getDialog() != null && getDialog().getWindow() != null) {

            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }

        if (getArguments() != null) {
            VIDEO_ID = getArguments().getString("url");
        }


        RecyclerView article_segment_recycler = view.findViewById(R.id.videosRecycler);
        article_segment_recycler.setLayoutManager(new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.VERTICAL, false));
        article_segment_recycler.setAdapter(new VideosAdapter((AppCompatActivity) getActivity()));


        if (HeavilyUsed.isContextSafe(getActivity())) {
            YouTubePlayerSupportFragment youtubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
            getChildFragmentManager().beginTransaction().disallowAddToBackStack().add(R.id.youtube_layout, youtubePlayerFragment).commitAllowingStateLoss();
            youtubePlayerFragment.initialize(getString(R.string.google_maps_key), this);
        }

        return view;
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            youTubePlayer.loadVideo(VIDEO_ID);
            youTubePlayer.setShowFullscreenButton(false);
            this.youTubePlayer = youTubePlayer;
            isIntilize = true;
        }
    }



    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        AppController.getInstance().showErrorToast(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
        if (youTubePlayer != null && isIntilize) {
            youTubePlayer.release();
        }
        youTubePlayer = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (youTubePlayer != null) {
            youTubePlayer.release();
        }
        youTubePlayer = null;
    }

}