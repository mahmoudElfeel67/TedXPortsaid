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

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.Adapter.VideosAdapter;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.Fragment.VideosFragment;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTask;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTaskInterface;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeChannel;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.bloomers.tedxportsaid.Utitltes.other.HeavilyUsed;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import java.util.ArrayList;
import java.util.List;

public class YoutubeFragment extends DialogFragment implements YouTubePlayer.OnInitializedListener {

    private static String VIDEO_ID = "dARAN1z2KqY";
    private YouTubePlayer youTubePlayer;
    private Boolean isIntilize = false;
    private int postion = 0;


    public static YoutubeFragment newInstance(String videoid) {
        YoutubeFragment fragment = new YoutubeFragment();
        VIDEO_ID = videoid;
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_youtube_video, container);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setWindowAnimations(R.style.MyAnimation_Window);
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }

        final SpinKitView spinKitView = view.findViewById(R.id.spin_kit);

        ArrayList<com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo> cutArrayList = new ArrayList<>();


        for (int i=0;i<VideosFragment.videos.size();i++){
            if (VideosFragment.videos.get(i).getId().equals(VIDEO_ID)){
                postion=i+1;
                break;
            }
        }

        for (int i=postion;i<VideosFragment.videos.size();i++){
            cutArrayList.add(VideosFragment.videos.get(i));
        }


        final RecyclerView article_segment_recycler = view.findViewById(R.id.videosRecycler);
        article_segment_recycler.setLayoutManager(new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.VERTICAL, false));


        if (cutArrayList.size()==0){
            new GetChannelVideosTask(VideosFragment.getChannelVideos, new YouTubeChannel("UCAuUUnT6oDeKwE6v1NGQxug", "asdas"))
                 .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                     @Override
                     public void onGetVideos(List<YouTubeVideo> videos) {
                         spinKitView.setVisibility(View.GONE);
                         if (videos != null) {
                             article_segment_recycler.setAdapter(new VideosAdapter((AppCompatActivity) getActivity(), videos,true));

                         }else {
                             MainActivity.showCustomToast(getActivity(), getString(R.string.videos_not_loaded), null, false);
                         }

                     }
                 }).executeInParallel();
        }else {
            spinKitView.setVisibility(View.GONE);
            article_segment_recycler.setAdapter(new VideosAdapter((AppCompatActivity) getActivity(), cutArrayList,true));
        }




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