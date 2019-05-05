package com.bloomers.tedxportsaid.Service.YoutubeService.Test;

import com.bloomers.tedxportsaid.R;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetYouTubeVideos;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeAPI;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeAPIKey;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Get today's featured YouTube videos.
 */
public class GetFeaturedVideos extends GetYouTubeVideos {

    protected YouTube.Videos.List videosList = null;


    @Override
    public void init() throws IOException {
        videosList = YouTubeAPI.create().videos().list("snippet, statistics, contentDetails");
        videosList.setFields("items(id, snippet/defaultAudioLanguage, snippet/defaultLanguage, snippet/publishedAt, snippet/title, snippet/channelId, snippet/channelTitle," +
                "snippet/thumbnails, contentDetails/duration, statistics)," +
                "nextPageToken");
        videosList.setKey(YouTubeAPIKey.get().getYouTubeAPIKey());
        videosList.setChart("mostPopular");
        videosList.setMaxResults(getMaxResults());

    }


    @Override
    public List<YouTubeVideo> getNextVideos() {
        List<Video> searchResultList = null;

        if (!noMoreVideoPages()) {
            try {

                // set the preferred region (placed here to reflect any changes performed at runtime)


                // communicate with YouTube
                VideoListResponse response = videosList.execute();

                // get videos
                searchResultList = response.getItems();

            } catch (IOException e) {

            }
        }

        return toYouTubeVideoList(searchResultList);
    }


    /**
     * Converts {@link List} of {@link Video} to {@link List} of {@link YouTubeVideo}.
     *
     * @param videoList {@link List} of {@link Video}.
     * @return {@link List} of {@link YouTubeVideo}.
     */
    private List<YouTubeVideo> toYouTubeVideoList(List<Video> videoList) {
        List<YouTubeVideo> youTubeVideoList = new ArrayList<>();

        if (videoList != null) {
            for (Video video : videoList) {
                youTubeVideoList.add(new YouTubeVideo(video));
            }
        }

        return youTubeVideoList;
    }




    @Override
    public boolean noMoreVideoPages() {
        return false;
    }


    /**
     * @return The maximum number of items that should be retrieved per YouTube query.
     */
    protected Long getMaxResults() {
        return 50L;
    }

}