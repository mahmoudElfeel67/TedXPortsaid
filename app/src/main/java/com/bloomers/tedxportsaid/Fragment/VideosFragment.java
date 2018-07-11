package com.bloomers.tedxportsaid.Fragment;


import android.content.Context;
import android.graphics.Color;
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
import android.widget.TextView;

import com.bloomers.tedxportsaid.Activity.MainActivity;
import com.bloomers.tedxportsaid.Adapter.EmptyAdapter;
import com.bloomers.tedxportsaid.Adapter.VideosAdapter;
import com.bloomers.tedxportsaid.AppController;
import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideos;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTask;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTaskInterface;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeChannel;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.bloomers.tedxportsaid.Utitltes.EndlessOnScrollListener;
import com.bloomers.tedxportsaid.Utitltes.other.LinearLayoutManagerEXT;
import com.github.ybq.android.spinkit.SpinKitView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideosFragment extends Fragment implements View.OnClickListener {

    public static VideosFragment newInstance() {
        return new VideosFragment();
    }

    public static List<YouTubeVideo> videos;
    private VideosAdapter adapter;
    public static GetChannelVideos getChannelVideos = new GetChannelVideos();
    @BindView(R.id.refreshLayout)  SwipeRefreshLayout refreshLayout;
    @BindView(R.id.portsaid_vid)  TextView portsaid_vid;
    @BindView(R.id.tedx_vid)  TextView tedx_vid;
    private boolean searchRunning = false;
    private boolean isRefreshing = false;
    private static boolean isAtPortsiad = true;
    @BindView(R.id.spin_kit)  SpinKitView progressBar;
    @BindView(R.id.videosRecycler)  RecyclerView article_segment_recycler;
   public  static final String tedxPortsaidChnnel  = "UC-4KnPMmZzwAzW7SbVATUZQ";

    @SuppressWarnings("unchecked")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View root = inflater.inflate(R.layout.fragment_videos, container, false);
        ButterKnife.bind(this, root);

        LinearLayoutManagerEXT linearLayoutManagerEXT = new LinearLayoutManagerEXT(getContext(), LinearLayoutManager.VERTICAL, false);
        article_segment_recycler.setLayoutManager(linearLayoutManagerEXT);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefreshing && videos != null) {
                    refreshLayout.setRefreshing(false);
                } else {
                    load(progressBar, article_segment_recycler, tedxPortsaidChnnel);
                }

            }
        });

        article_segment_recycler.setAdapter(new EmptyAdapter());

        if (videos == null && !searchRunning) {
            load(progressBar, article_segment_recycler, tedxPortsaidChnnel);
        } else {
            progressBar.setVisibility(View.GONE);
            adapter = new VideosAdapter((AppCompatActivity) getActivity(), VideosFragment.videos);
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
                                isRefreshing = false;
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

        tedx_vid.setOnClickListener(this);
        portsaid_vid.setOnClickListener(this);

        return root;
    }

    private void load(final SpinKitView progressBar, final RecyclerView article_segment_recycler, final String channel) {
        if (AppController.getInstance().isThereInternet(getActivity())){
            searchRunning = true;
            new GetChannelVideosTask(getChannelVideos, new YouTubeChannel(channel, "asdas"))
                    .setGetChannelVideosTaskInterface(new GetChannelVideosTaskInterface() {
                        @Override
                        public void onGetVideos(List<YouTubeVideo> videos) {
                            refreshLayout.setRefreshing(false);
                            searchRunning = false;
                            if (videos != null) {
                                VideosFragment.videos = new ArrayList<>();
                                VideosFragment.videos.addAll(videos);

                                if (!getActivity().getSharedPreferences("TEDX", Context.MODE_PRIVATE).getBoolean("isSaved",false)){
                                    try {
                                        if (channel.equals(tedxPortsaidChnnel)){
                                            for (YouTubeVideo tubeVideo :VideosFragment.videos){
                                                try {
                                                    YouTubeVideo.storeFilter(tubeVideo,getContext());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                            getActivity().getSharedPreferences("TEDX", Context.MODE_PRIVATE).edit().putBoolean("isSaved",true).apply();
                                        }
                                    }catch (Exception e){

                                    }
                                }


                                progressBar.setVisibility(View.GONE);

                                if (VideosFragment.videos != null) {
                                    adapter = new VideosAdapter((AppCompatActivity) getActivity(), VideosFragment.videos);
                                    article_segment_recycler.setAdapter(adapter);

                                } else {
                                    MainActivity.showCustomToast(getActivity(), getString(R.string.videos_not_loaded), null, false);
                                }
                            }

                        }
                    }).executeInParallel();

        }else {
            if (getActivity().getSharedPreferences("TEDX", Context.MODE_PRIVATE).getBoolean("isSaved",false)){
                progressBar.setVisibility(View.GONE);

                VideosFragment.videos = new ArrayList<>();
                VideosFragment.videos.addAll(YouTubeVideo.getFilters(getContext()));

                if (VideosFragment.videos != null) {
                    adapter = new VideosAdapter((AppCompatActivity) getActivity(), VideosFragment.videos);
                    article_segment_recycler.setAdapter(adapter);

                } else {
                    MainActivity.showCustomToast(getActivity(), getString(R.string.videos_not_loaded), null, false);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tedx_vid:
                isAtPortsiad = false;
                change();
                break;
            case R.id.portsaid_vid:
                isAtPortsiad = true;
                change();
                break;
        }
    }

    private void change() {
        if (isAtPortsiad) {
            tedx_vid.setTextColor(Color.parseColor("#d6898989"));
            portsaid_vid.setTextColor(AppController.easyColor(getContext(), R.color.black));
            getChannelVideos = new GetChannelVideos();
            load(progressBar, article_segment_recycler, tedxPortsaidChnnel);
        } else {
            tedx_vid.setTextColor(AppController.easyColor(getContext(), R.color.black));
            portsaid_vid.setTextColor(Color.parseColor("#d6898989"));
            getChannelVideos = new GetChannelVideos();
            load(progressBar, article_segment_recycler, "UCAuUUnT6oDeKwE6v1NGQxug");
        }


    }
}