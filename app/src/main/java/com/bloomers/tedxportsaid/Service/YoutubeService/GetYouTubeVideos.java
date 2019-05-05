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

import java.io.IOException;
import java.util.List;


/**
 * Returns a list of YouTube videos.
 *
 * <p>Do not run this directly, but rather use {@link GetYouTubeVideosTask}.</p>
 */
public abstract class GetYouTubeVideos {
	String nextPageToken = null;
	boolean noMoreVideoPages = false;

	/**
	 * Initialise this object.
	 *
	 * @throws IOException
	 */
	protected abstract void init() throws IOException;


	/**
	 * Sets user's query. [optional]
	 */
    public void setQuery(String query) {
	}


	/**
	 * Gets the next page of videos.
	 *
	 * @return List of {@link YouTubeVideo}s.
	 */
	protected abstract List<YouTubeVideo> getNextVideos();


	/**
	 * @return True if YouTube states that there will be no more video pages; false otherwise.
	 */
	protected abstract boolean noMoreVideoPages();


	/**
	 * Reset the fetching of videos. This will be called when a swipe to refresh is done.
	 */
    void reset() {
		nextPageToken = null;
		noMoreVideoPages = false;
	}
}
