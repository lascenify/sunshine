package com.example.android.sunshine.utilities

import android.net.Uri
import android.util.Log
import java.net.MalformedURLException
import java.net.URL

object StaticNetworkUtils {
    private const val STATIC_WEATHER_URL =
        "https://andfun-weather.udacity.com/staticweather"

    fun buildStaticUrl(locationQuery: String?): URL? {
        val builtUri =
            Uri.parse(STATIC_WEATHER_URL).buildUpon()
                .appendQueryParameter(NetworkUtils.QUERY_PARAM, locationQuery)
                .appendQueryParameter(NetworkUtils.FORMAT_PARAM, NetworkUtils.format)
                .appendQueryParameter(NetworkUtils.UNITS_PARAM, NetworkUtils.units)
                .appendQueryParameter(
                    NetworkUtils.DAYS_PARAM,
                    NetworkUtils.numDays.toString()
                )
                .build()

        var url: URL? = null
        try {
            url = URL(builtUri.toString())
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }

        Log.v(NetworkUtils.TAG, "Built URI $url")

        return url
    }
}