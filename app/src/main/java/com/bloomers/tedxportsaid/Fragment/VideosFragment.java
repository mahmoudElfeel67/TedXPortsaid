package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Adapter.VideosAdapter;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTask;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTaskInterface;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeChannel;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;

import java.util.List;

public class VideosFragment extends Fragment {

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_videos, container, false);

        final RecyclerView article_segment_recycler = root.findViewById(R.id.videosRecycler);
        article_segment_recycler.setLayoutManager(new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.VERTICAL, false));


        new GetChannelVideosTask(new YouTubeChannel("UCAuUUnT6oDeKwE6v1NGQxug","asdas"))
                .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                    @Override
                    public void onGetVideos(List<YouTubeVideo> videos) {

                        article_segment_recycler.setAdapter(new VideosAdapter((AppCompatActivity) getActivity(), videos));

                    }
                }).executeInParallel();

        return root;
    }

}