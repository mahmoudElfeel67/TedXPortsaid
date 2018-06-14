/*
 * SkyTube
 * Copyright (C) 2018  Ramon Mifsud
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation (version 3 of the License).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloomers.tedxportsaid.Service.YoutubeService;


import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import timber.log.Timber;

/**
 * Task to asynchronously get videos for a specific channel.
 */
public class GetChannelVideosTask extends AsyncTaskParallel<Void, Void, List<YouTubeVideo>> {


    private YouTubeChannel channel;
    private GetChannelVideosTaskInterface getChannelVideosTaskInterface;
    private GetChannelVideos getChannelVideos;

    public GetChannelVideosTask( GetChannelVideos getChannelVideos ,YouTubeChannel channel) {
        try {
            this.channel = channel;
            this.getChannelVideos= getChannelVideos;
            if (getChannelVideos.getChannelVideos==null){
                getChannelVideos.init();
                getChannelVideos.setQuery(channel.getId());
            }


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public GetChannelVideosTask setGetChannelVideosTaskInterface(GetChannelVideosTaskInterface getChannelVideosTaskInterface) {
        this.getChannelVideosTaskInterface = getChannelVideosTaskInterface;
        return this;
    }

    @Override
    protected List<YouTubeVideo> doInBackground(Void... voids) {
        List<YouTubeVideo> videos = null;

        if (!isCancelled()) {
            videos = getChannelVideos.getNextVideos();
        }

        if (videos != null) {
            for (YouTubeVideo video : videos)
                channel.addYouTubeVideo(video);
        }

        return videos;
    }

    @Override
    protected void onPostExecute(List<YouTubeVideo> youTubeVideos) {
        if (getChannelVideosTaskInterface != null)
            getChannelVideosTaskInterface.onGetVideos(youTubeVideos);
    }

    private DateTime getOneMonthAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -2);
        Date date = calendar.getTime();
        Timber.e(new DateTime(date).toString());
        return new DateTime(date);
    }

}