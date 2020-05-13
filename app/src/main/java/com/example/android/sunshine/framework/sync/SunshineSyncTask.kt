package com.example.android.sunshine.framework.sync

import android.content.Context
import android.util.Log
import com.example.android.sunshine.framework.provider.WeatherContract
import com.example.android.sunshine.utilities.NetworkUtils
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils

object SunshineSyncTask{
    private const val SYNCING_ERROR_TAG = "sync_error"
    private const val SYNCING_ERROR_MESSAGE = "Couldn't perform syncing"

    /**
     * Performs the network request to update the weather data.
     */
    @Synchronized
    fun syncWeather(context: Context):Array<String?>?{
        try{
            /** URL to perform the request */
            val requestURL = NetworkUtils.getUrl(context)
            /** Perform the request and get a JSON back */
            val jsonWeatherResponse = NetworkUtils.getResponseFromHttpUrl(requestURL!!)
            /** gets the content values from the JSON file */
            val weatherContentValues = OpenWeatherJsonUtils.getWeatherContentValuesFromJson(context, jsonWeatherResponse)
            /** if the data is valid, delete the old data and insert this new data */
            if (!weatherContentValues.isNullOrEmpty()){
                val sunshineContentResolver = context.contentResolver
                sunshineContentResolver.delete(
                    WeatherContract.WeatherEntry.CONTENT_URI,
                    null,
                    null)
                sunshineContentResolver.bulkInsert(
                    WeatherContract.WeatherEntry.CONTENT_URI,
                    weatherContentValues)
            }
            return OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(context, jsonWeatherResponse)
            //ForecastListFragment.updateForecastData(weatherStrings)
        } catch (e:Exception){
            Log.e(SYNCING_ERROR_TAG, SYNCING_ERROR_MESSAGE)
            e.printStackTrace()
            return null
        }
    }
}