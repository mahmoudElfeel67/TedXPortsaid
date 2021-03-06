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

import android.content.Context;
import android.content.SharedPreferences;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.Video;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;


/**
 * Represents a YouTube video.
 */
public class YouTubeVideo implements Serializable {

	private static final String dataBaseFilterTEST="TEDXVIDEOS";
	/**
	 * YouTube video ID.
	 */
	private final String id;
	/**
	 * Video title.
	 */
	private String title;
	/**
	 * Channel (only id and name are set).
	 */
	private YouTubeChannel channel;
	/**
	 * The total number of 'likes'.
	 */
	private String likeCount;
	/**
	 * The total number of 'dislikes'.
	 */
	private String dislikeCount;
	/**
	 * The percentage of people that thumbs-up this video (format:  "<percentage>%").
	 */
	private String thumbsUpPercentageStr;
	private int thumbsUpPercentage;
	/**
	 * Video duration string (e.g. "5:15").
	 */
	private String duration;
	/**
	 *  Video duration in seconds
	 */
	private int durationInSeconds = -1;
	/**
	 * Total views count.  This can be <b>null</b> if the video does not allow the user to
	 * like/dislike it.  Format:  "<number> Views"
	 */
	private String viewsCount;
	/**
	 * Total views count.
	 */
	private BigInteger viewsCountInt;
	/**
	 * The date/time of when this video was published.
	 */
	private DateTime publishDate;
	private String publishDatePretty;
	/**
	 * Thumbnail URL.
	 */
	private String thumbnailUrl;
	/**
	 * Thumbnail URL (maximum resolution).
	 */
	private String thumbnailMaxResUrl;
	/**
	 * The language of this video.  (This tends to be ISO 639-1).
	 */
	private String language;
	/**
	 * The description of the video (set by the YouTuber/Owner).
	 */
	private String description;
	/**
	 * Set to true if the video is a current live stream.
	 */
	private boolean isLiveStream;


	public String getDuration() {
		return duration;
	}

	public YouTubeVideo(Video video) {
		this.id = video.getId();

		if (video.getSnippet() != null) {
			this.title = video.getSnippet().getTitle();

			this.channel = new YouTubeChannel(video.getSnippet().getChannelId(), video.getSnippet().getChannelTitle());


			if (video.getSnippet().getThumbnails() != null) {
				Thumbnail thumbnail = video.getSnippet().getThumbnails().getHigh();
				if (thumbnail != null)
					this.thumbnailUrl = thumbnail.getUrl();

				thumbnail = video.getSnippet().getThumbnails().getMaxres();
				if (thumbnail != null)
					this.thumbnailMaxResUrl = thumbnail.getUrl();
			}

			this.language = video.getSnippet().getDefaultAudioLanguage() != null ? video.getSnippet().getDefaultAudioLanguage()
					: (video.getSnippet().getDefaultLanguage());

			this.description = video.getSnippet().getDescription();
			if (video.getContentDetails() != null) {
				duration =VideoDuration.toHumanReadableString(video.getContentDetails().getDuration());
			}
		}



		if (video.getStatistics() != null) {
			BigInteger  likeCount = video.getStatistics().getLikeCount(),
						dislikeCount = video.getStatistics().getDislikeCount();


			this.viewsCountInt = video.getStatistics().getViewCount();

			if (likeCount != null)
				this.likeCount = String.format(Locale.getDefault(), "%,d", video.getStatistics().getLikeCount());

			if (dislikeCount != null)
				this.dislikeCount = String.format(Locale.getDefault(), "%,d", video.getStatistics().getDislikeCount());
		}
	}



	/**
	 * Extracts the video ID from the given video URL.
	 *
	 * @param url YouTube video URL.
	 * @return ID if everything went as planned; null otherwise.
	 */
	public static String getYouTubeIdFromUrl(String url) {
		if (url == null)
			return null;

		// TODO:  support playlists (i.e. video_ids=... <-- URL submitted via email by YouTube)
		final String pattern = "(?<=v=|/videos/|embed/|youtu\\.be/|/v/|/e/|video_ids=)[^#&?%]*";
		Pattern compiledPattern = Pattern.compile(pattern);
		Matcher matcher = compiledPattern.matcher(url);

		return matcher.find() ? matcher.group() /*video id*/ : null;
	}




	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public YouTubeChannel getChannel() {
		return channel;
	}



	/**
	 * Gets the {@link #publishDate} as a pretty string.
	 */
	public String getPublishDatePretty() {
		return publishDatePretty;
	}



	public String getThumbnailUrl() {
		return thumbnailUrl;
	}

	public String getThumbnailMaxResUrl() {
		return thumbnailMaxResUrl;
	}

	public String getVideoUrl() {
		return String.format("https://youtu.be/%s", id);
	}

	public String getLanguage() {
		return language;
	}

	public String getDescription() {
		return description;
	}


	public static ArrayList<YouTubeVideo> getFilters(Context context)  {

		ArrayList<YouTubeVideo> faceModels = new ArrayList<>();
		for (File file1 : context.getFilesDir().listFiles()) {
			if (file1.getName().contains(dataBaseFilterTEST) && !file1.isDirectory()) {
				try {
					faceModels.add(getFilter(context, file1.getName()));
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}

		}
		return faceModels;
	}

	private static YouTubeVideo getFilter(Context context, String fileName) throws IOException, ClassNotFoundException {
		FileInputStream fis = context.openFileInput(fileName);
		ObjectInputStream is = new ObjectInputStream(fis);
		YouTubeVideo faceModel = (YouTubeVideo) is.readObject();
		is.close();
		fis.close();
		return faceModel;
	}

	public static void storeFilter(YouTubeVideo faceModel, Context context) throws IOException {

		SharedPreferences sharedPreferences = context.getSharedPreferences("TEDX", MODE_PRIVATE);
		String last = String.valueOf(sharedPreferences.getInt("lastFilter", 0));
		FileOutputStream fos = context.openFileOutput(dataBaseFilterTEST + last, MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(faceModel);
		os.close();
		fos.close();
		sharedPreferences.edit().putInt("lastFilter", sharedPreferences.getInt("lastFilter", 0) + 1).commit();

	}


}
