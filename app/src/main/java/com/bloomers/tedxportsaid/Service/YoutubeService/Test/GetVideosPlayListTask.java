package com.bloomers.tedxportsaid.Service.YoutubeService.Test;

import com.bloomers.tedxportsaid.Service.YoutubeService.AsyncTaskParallel;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideos;
import com.bloomers.tedxportsaid.Service.YoutubeService.GetChannelVideosTaskInterface;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeChannel;
import com.bloomers.tedxportsaid.Service.YoutubeService.YouTubeVideo;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

public class GetVideosPlayListTask extends AsyncTaskParallel<Void, Void, List<YouTubeVideo>> {


    OnVideosLoaded onVideosLoaded;
    List<YouTubeVideo> videosY = new ArrayList<>();
    private GetPlaylistVideos getChannelVideos;

    public GetVideosPlayListTask(OnVideosLoaded onVideosLoaded ) {

        this.onVideosLoaded = onVideosLoaded;
        try {
            getChannelVideos = new GetPlaylistVideos();
            getChannelVideos.init();
            getChannelVideos.setQuery("PLZr_U3ANXFZ2dekKDYb9mUJP_1GW2nH76");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected List<YouTubeVideo> doInBackground(Void... voids) {
        List<YouTubeVideo> videos = null;

        if (!isCancelled()) {
            videos = getChannelVideos.getNextVideos();
        }

        if (videos != null) {
            for (YouTubeVideo video : videos)
                videosY.add(video);;
        }

        return videos;
    }

    @Override
    protected void onPostExecute(List<YouTubeVideo> youTubeVideos) {
        onVideosLoaded.OnLoaded(videosY);
    }

    public interface OnVideosLoaded{
        void OnLoaded(List<YouTubeVideo> list);
    }

    private DateTime getOneMonthAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date date = calendar.getTime();
        Timber.e(new DateTime(date).toString());
        return new DateTime(date);
    }

}