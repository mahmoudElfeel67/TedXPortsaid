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

import com.bloomers.tedxportsaid.BuildConfig;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.youtube.YouTube;


/**
 * Represents YouTube API service.
 */
public class YouTubeAPI {

	/**
	 * Returns a new instance of {@link YouTube}.
	 *
	 * @return {@link YouTube}
	 */
	public static YouTube create() {
		HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
		JsonFactory jsonFactory = com.google.api.client.extensions.android.json.AndroidJsonFactory.getDefaultInstance();
		return new YouTube.Builder(httpTransport, jsonFactory, new HttpRequestInitializer() {
			private String getSha1() {
				return "E8:30:3D:5F:04:12:23:A3:59:7E:FA:8A:87:5B:F3:A0:87:3B:77:30";
			}
			@Override
			public void initialize(HttpRequest request) {
				request.getHeaders().set("X-Android-Package", BuildConfig.APPLICATION_ID);
				request.getHeaders().set("X-Android-Cert", getSha1());
			}
		}).setApplicationName("+").build();
	}

}
