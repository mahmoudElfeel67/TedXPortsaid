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

import java.util.Random;

/**
 * Represents a YouTube API key.
 */
public class YouTubeAPIKey {

	private final String userAPIKey;
	private Random random = new Random();

	private static YouTubeAPIKey youTubeAPIKey = null;


	/**
	 * Constructor.  Will retrieve user's YouTube API key if set.
	 */
	private YouTubeAPIKey() {
		userAPIKey = getUserApiKey();
	}


	/**
	 * @return An instance of {@link YouTubeAPIKey}.
	 */
	public static YouTubeAPIKey get() {
		if (youTubeAPIKey == null) {
			youTubeAPIKey = new YouTubeAPIKey();
		}

		return youTubeAPIKey;
	}



	/**
	 * @return Return YouTube API key.
	 */
	public String getYouTubeAPIKey() {

		return "AIzaSyC0zs51EhuRCZwFQT8B5VJ5X7sAs_kevn0";
	}


	public boolean isUserApiKeySet() {
		return (userAPIKey != null);
	}


	/**
	 * @return User's YouTube API key (if set).  If the user did not set a key, then it will return null.
	 */
	private String getUserApiKey() {

		return "AIzaSyC0zs51EhuRCZwFQT8B5VJ5X7sAs_kevn0";
	}

}
