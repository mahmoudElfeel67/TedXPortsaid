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
import android.widget.ProgressBar;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.Adapter.VideosAdapter;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTask;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTaskInterface;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeChannel;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.bloomers.tedxportsaid.Utitltes.EndlessOnScrollListener;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class VideosFragment extends Fragment {

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }
    public static List<YouTubeVideo> videos;
    public static int when = -2;
    VideosAdapter adapter;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_videos, container, false);
        final ProgressBar progressBar = root.findViewById(R.id.spin_kit);
        final RecyclerView article_segment_recycler = root.findViewById(R.id.videosRecycler);
        LinearLayoutManagerEXT linearLayoutManagerEXT = new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.VERTICAL, false);
        article_segment_recycler.setLayoutManager(linearLayoutManagerEXT);



        when= -2;
        new GetChannelVideosTask(new YouTubeChannel("UCAuUUnT6oDeKwE6v1NGQxug","asdas"),-2)
             .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                 @Override
                 public void onGetVideos(List<YouTubeVideo> videos) {
                     VideosFragment.videos = new ArrayList<>();
                     VideosFragment.videos.addAll(videos);
                     Timber.e("Sadas whee"+ when + "  videos  "+videos.size());
                     new GetChannelVideosTask(new YouTubeChannel("UCAuUUnT6oDeKwE6v1NGQxug","asdas"),-3)
                          .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                              @Override
                              public void onGetVideos(List<YouTubeVideo> videos) {
                                  when= -3;
                                  Timber.e("Sadas whee"+ when + "  videos  "+videos.size());
                                  VideosFragment.videos.addAll(videos);
                                  progressBar.setVisibility(View.GONE);

                                  if (VideosFragment.videos!=null){
                                      adapter = new VideosAdapter((AppCompatActivity) getActivity(), VideosFragment.videos);
                                      article_segment_recycler.setAdapter(adapter);

                                  }else {
                                      MainActivity.showCusomtToast(getActivity(),getString(R.string.videos_not_loaded),null,false);
                                  }


                              }
                          }).executeInParallel();

                 }
             }).executeInParallel();

        article_segment_recycler.addOnScrollListener(new EndlessOnScrollListener(linearLayoutManagerEXT) {
            @Override
            public void onScrolledToEnd() {
                new GetChannelVideosTask(new YouTubeChannel("UCAuUUnT6oDeKwE6v1NGQxug","asdas"),when = when-1)
                     .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                         @Override
                         public void onGetVideos(List<YouTubeVideo> videos) {
                             Timber.e("Sadas whee"+ when + "  videos  "+videos.size());
                             VideosFragment.videos.addAll(videos);
                             adapter.setVideos((ArrayList<YouTubeVideo>) VideosFragment.videos);
                             adapter.notifyDataSetChanged();



                         }
                     }).executeInParallel();
            }
        });


        return root;
    }

}