package com.bloomers.tedxportsaid.Service.YoutubeService.Test;


import java.io.IOException;

public class GetVideosDetailsByIDs extends GetFeaturedVideos {

    /**
     * Initialise object.
     *
     * @param videoIds		Comma separated videos IDs.
     * @throws IOException
     */
    public void init(String videoIds) throws IOException {
        super.init();
        super.videosList.setId(videoIds);
        super.videosList.setChart(null);
    }

}