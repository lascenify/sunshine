/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.utilities

import android.net.Uri
import android.util.Log
import com.example.android.sunshine.BuildConfig.OPEN_WEATHER_API_KEY
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

/**
 * These utilities will be used to communicate with the weather servers.
 */
object NetworkUtils {
    private val TAG = NetworkUtils::class.java.simpleName

    private const val STATIC_WEATHER_URL =
        "https://andfun-weather.udacity.com/staticweather"
    private const val FORECAST_BASE_URL = "https://api.openweathermap.org/data/2.5/forecast"
    /*
     * NOTE: These values only effect responses from OpenWeatherMap, NOT from the fake weather
     * server. They are simply here to allow us to teach you how to build a URL if you were to use
     * a real API.If you want to connect your app to OpenWeatherMap's API, feel free to! However,
     * we are not going to show you how to do so in this course.
     */
/* The format we want our API to return */
    private const val format = "json"
    /* The units we want our API to return */
    var units = "metric"
    /* The number of days we want our API to return */
    private const val numDays = 14
    private const val apiKey = OPEN_WEATHER_API_KEY

    private const val QUERY_PARAM = "q"
    private const val LAT_PARAM = "lat"
    private const val LON_PARAM = "lon"
    private const val FORMAT_PARAM = "mode"
    private const val UNITS_PARAM = "units"
    private const val DAYS_PARAM = "cnt"
    private const val APP_ID = "appid"
    /**
     * Builds the URL used to talk to the weather server using a location. This location is based
     * on the query capabilities of the weather provider that we are using.
     *
     * @param locationQuery The location that will be queried for.
     * @return The URL to use to query the weather server.
     */
    fun buildUrl(locationQuery: String?): URL? {
        val uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
            .appendQueryParameter(QUERY_PARAM, locationQuery)
            .appendQueryParameter(FORMAT_PARAM, format)
            .appendQueryParameter(UNITS_PARAM, units)
            .appendQueryParameter(DAYS_PARAM, numDays.toString())
            .appendQueryParameter(APP_ID, apiKey)
        return URL(uri.toString())
    }


    fun buildStaticUrl(locationQuery: String?): URL? {
        val builtUri =
            Uri.parse(STATIC_WEATHER_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAM, locationQuery)
                .appendQueryParameter(FORMAT_PARAM, format)
                .appendQueryParameter(UNITS_PARAM, units)
                .appendQueryParameter(
                    DAYS_PARAM,
                    numDays.toString()
                )
                .build()

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        Log.v(TAG, "Built URI $url")

        return url
    }

    /**
     * Builds the URL used to talk to the weather server using latitude and longitude of a
     * location.
     *
     * @param lat The latitude of the location
     * @param lon The longitude of the location
     * @return The Url to use to query the weather server.
     */
    fun buildUrl(lat: Double?, lon: Double?): URL? {
        val uri = Uri.parse(FORECAST_BASE_URL).buildUpon()
            .appendQueryParameter(LAT_PARAM, lat.toString())
            .appendQueryParameter(LON_PARAM, lon.toString())
            .appendQueryParameter(FORMAT_PARAM, format)
            .appendQueryParameter(UNITS_PARAM, units)
            .appendQueryParameter(DAYS_PARAM, numDays.toString())
        return URL(uri.toString())
    }

    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */

    @Throws(IOException::class)
    fun getResponseFromHttpUrl(url: URL): String? {
        val urlConnection =
            url.openConnection() as HttpURLConnection
        return try {
            val `in` = urlConnection.inputStream
            val scanner = Scanner(`in`)
            scanner.useDelimiter("\\A")
            val hasInput = scanner.hasNext()
            if (hasInput) {
                scanner.next()
            } else {
                null
            }
        } finally {
            urlConnection.disconnect()
        }
    }
}