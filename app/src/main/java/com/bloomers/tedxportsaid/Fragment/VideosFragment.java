package com.bloomers.tedxportsaid.Fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.Adapter.EmptyAdapter;
import com.bloomers.tedxportsaid.Adapter.VideosAdapter;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideos;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTask;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTaskInterface;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeChannel;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.bloomers.tedxportsaid.Utitltes.EndlessOnScrollListener;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosFragment extends Fragment {

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    public static List<YouTubeVideo> videos;
    VideosAdapter adapter;
    public static final GetChannelVideos getChannelVideos = new GetChannelVideos();
    @BindView(R.id.refreshLayout) SwipeRefreshLayout refreshLayout;
    private boolean searchRunning = false;
    private boolean isRefreshing = false;

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, root);

        final SpinKitView progressBar = root.findViewById(R.id.spin_kit);
        final RecyclerView article_segment_recycler = root.findViewById(R.id.videosRecycler);
        LinearLayoutManagerEXT linearLayoutManagerEXT = new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.VERTICAL, false);
        article_segment_recycler.setLayoutManager(linearLayoutManagerEXT);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefreshing&&videos!=null){
                    refreshLayout.setRefreshing(false);
                }else {
                    load(progressBar,article_segment_recycler);
                }

            }
        });

        article_segment_recycler.setAdapter(new EmptyAdapter());

        if (videos == null && !searchRunning) {
            load(progressBar,article_segment_recycler);
        } else {
            progressBar.setVisibility(View.GONE);
            adapter = new VideosAdapter((AppCompatActivity) getActivity(), VideosFragment.videos,false);
            article_segment_recycler.setAdapter(adapter);
        }

        article_segment_recycler.addOnScrollListener(new EndlessOnScrollListener(linearLayoutManagerEXT) {
            @Override
            public void onScrolledToEnd() {
                final int last = videos.size() - 1;
                isRefreshing = true;
                refreshLayout.setRefreshing(true);
                new GetChannelVideosTask(getChannelVideos, new YouTubeChannel("UCAuUUnT6oDeKwE6v1NGQxug", "asdas"))
                     .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                         @Override
                         public void onGetVideos(List<YouTubeVideo> videos) {
                             isRefreshing= false;
                             refreshLayout.setRefreshing(false);
                             article_segment_recycler.smoothScrollToPosition(last + 1);
                             if (videos != null) {
                                 VideosFragment.videos.addAll(videos);
                                 adapter.setVideos((ArrayList<YouTubeVideo>) VideosFragment.videos);
                                 adapter.notifyDataSetChanged();
                             }
                         }
                     }).executeInParallel();
            }
        });

        return root;
    }

    private void load(final SpinKitView progressBar, final RecyclerView article_segment_recycler) {
        searchRunning = true;
        new GetChannelVideosTask(getChannelVideos, new YouTubeChannel("UCAuUUnT6oDeKwE6v1NGQxug", "asdas"))
             .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                 @Override
                 public void onGetVideos(List<YouTubeVideo> videos) {
                     refreshLayout.setRefreshing(false);
                     searchRunning = false;
                     if (videos != null) {
                         VideosFragment.videos = new ArrayList<>();
                         VideosFragment.videos.addAll(videos);
                         progressBar.setVisibility(View.GONE);

                         if (VideosFragment.videos != null) {
                             adapter = new VideosAdapter((AppCompatActivity) getActivity(), VideosFragment.videos,false);
                             article_segment_recycler.setAdapter(adapter);

                         } else {
                             MainActivity.showCusomtToast(getActivity(), getString(R.string.videos_not_loaded), null, false);
                         }
                     }

                 }
             }).executeInParallel();
    }

}