package com.bloomers.tedxportsaid.Service.YoutubeService.Test;

import com.bloomers.tedxportsaid.Service.YoutubeService.AsyncTaskParallel;

import java.io.IOException;

import timber.log.Timber;

public class GetPlaylistTask extends AsyncTaskParallel<Void, Void, YouTubePlaylist> {
    private String playlistId;

    private GetPlaylist getPlaylist;

    OnVideosLoaded onVideosLoaded;

    public GetPlaylistTask(String playlistId,OnVideosLoaded onVideosLoaded) {
        this.playlistId = playlistId;
        this.onVideosLoaded = onVideosLoaded;

    }

    @Override
    protected YouTubePlaylist doInBackground(Void... voids) {
        try {
            getPlaylist = new GetPlaylist();
            getPlaylist.init(playlistId);
            return getPlaylist.getNextPlaylists().get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(YouTubePlaylist youTubePlaylist) {
        onVideosLoaded.OnLoaded(youTubePlaylist);
    }


    public  interface OnVideosLoaded{
        void OnLoaded(YouTubePlaylist youTubePlaylist);
    }
}