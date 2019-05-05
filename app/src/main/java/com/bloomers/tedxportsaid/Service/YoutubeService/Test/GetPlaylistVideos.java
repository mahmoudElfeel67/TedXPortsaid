package com.bloomers.tedxportsaid.Service.YoutubeService.Test;

import com.bloomers.tedxportsaid.Service.YoutubeService.GetYouTubeVideos;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeAPI;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeAPIKey;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetPlaylistVideos extends GetYouTubeVideos {
    // The list of playlist videos returned by the YouTube Data API
    private YouTube.PlaylistItems.List playlistItemsList;

    // Number of videos to return each time
    private static final long MAX_RESULTS = 45L;

    @Override
    public void init() throws IOException {
        playlistItemsList = YouTubeAPI.create().playlistItems().list("contentDetails");
        playlistItemsList.setFields("items(contentDetails/videoId), nextPageToken");
        playlistItemsList.setKey(YouTubeAPIKey.get().getYouTubeAPIKey());
        playlistItemsList.setMaxResults(MAX_RESULTS);

    }

    @Override
    public List<YouTubeVideo> getNextVideos() {
        List<YouTubeVideo> videoList = new ArrayList<>();

        if (!noMoreVideoPages()) {
            try {


                PlaylistItemListResponse response = playlistItemsList.execute();

                StringBuilder videoIds = new StringBuilder();

                List<PlaylistItem> items = response.getItems();
                if(items != null) {
                    for(PlaylistItem item : items) {
                        videoIds.append(item.getContentDetails().getVideoId());
                        videoIds.append(',');
                    }
                }

                // get video details by supplying the videos IDs
                GetVideosDetailsByIDs getVideo = new GetVideosDetailsByIDs();
                getVideo.init(videoIds.toString());

                videoList = getVideo.getNextVideos();


            } catch (IOException e) {

            }
        }

        return videoList;
    }

    /**
     * Set the Playlist ID to search for
     * @param query Playlist ID
     */
    @Override
    public void setQuery(String query) {
        playlistItemsList.setPlaylistId(query);
    }

    @Override
    public boolean noMoreVideoPages() {
        return false;
    }
}