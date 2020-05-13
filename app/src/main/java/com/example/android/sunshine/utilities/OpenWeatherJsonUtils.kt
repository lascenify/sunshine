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

import android.content.ContentValues
import android.content.Context
import com.example.android.sunshine.framework.SunshinePreferences.setLocationDetails
import com.example.android.sunshine.framework.provider.WeatherContract
import org.json.JSONException
import org.json.JSONObject
import java.net.HttpURLConnection

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
object OpenWeatherJsonUtils {

    /* Weather information. Each day's forecast info is an element of the "list" array */
    private const val OWM_LIST = "list"
    /* All temperatures are children of the "temp" object */
    private const val OWM_TEMPERATURE = "temp"
    private const val OWM_WEATHER = "weather"
    private const val OWM_DESCRIPTION = "main"
    private const val OWM_MESSAGE_CODE = "cod"

    private const val OWM_WEATHER_ID = "id"

    /* Location information */
    private const val OWM_CITY = "city"
    private const val OWM_COORD = "coord"

    /* Location coordinate */
    private const val OWM_LATITUDE = "lat"
    private const val OWM_LONGITUDE = "lon"


    private const val OWM_PRESSURE = "pressure"
    private const val OWM_HUMIDITY = "humidity"

    private const val OWM_WIND = "wind"
    private const val OWM_WINDSPEED = "speed"
    private const val OWM_WIND_DIRECTION = "deg"


    /* Max temperature for the day */
    private const val OWM_MAX = "max"
    private const val OWM_MIN = "min"




    /**
     * This method parses JSON from a web response and returns an array of Strings
     * describing the weather over various days from the forecast.
     *
     *
     * Later on, we'll be parsing the JSON into structured data within the
     * getFullWeatherDataFromJson function, leveraging the data we have stored in the JSON. For
     * now, we just convert the JSON into human-readable strings.
     *
     * @param forecastJsonStr JSON response from server
     *
     * @return Array of Strings describing weather data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    @Throws(JSONException::class)
    fun getSimpleWeatherStringsFromJson(
        context: Context,
        forecastJsonStr: String?
    ): Array<String?>? { /* Weather information. Each day's forecast info is an element of the "list" array */

        /* Max temperature for the day */
        val OWM_MAX = "temp_max"
        val OWM_MIN = "temp_min"
        /* String array to hold each day's weather String */

        val forecastJson = JSONObject(forecastJsonStr)
        /* Is there an error? */if (forecastJson.has(OWM_MESSAGE_CODE)) {
            val errorCode = forecastJson.getInt(OWM_MESSAGE_CODE)
            when (errorCode) {
                HttpURLConnection.HTTP_OK -> {
                }
                HttpURLConnection.HTTP_NOT_FOUND ->  /* Location invalid */return null
                else ->  /* Server probably down */return null
            }
        }
        val weatherArray = forecastJson.getJSONArray(OWM_LIST)
        val parsedWeatherData: Array<String?>  = arrayOfNulls(weatherArray.length())
        val localDate = System.currentTimeMillis()
        val utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate)
        val startDay = SunshineDateUtils.normalizeDate(utcDate)
        for (i in 0 until weatherArray.length()) {
            var date: String
            var highAndLow: String
            /* These are the values that will be collected */
            var temp :Double
            var high: Double
            var low: Double
            var description: String
            /* Get the JSON object representing the day */
            val dayForecast = weatherArray.getJSONObject(i)
            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            val dateTimeMillis: Long = startDay + SunshineDateUtils.DAY_IN_MILLIS * i
            date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false)
            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.
             */

            val weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0)
            description = weatherObject.getString(OWM_DESCRIPTION)
            /*
             * Temperatures are sent by Open Weather Map in a child object called "temp".
             *
             * Editor's Note: Try not to name variables "temp" when working with temperature.
             * It confuses everybody. Temp could easily mean any number of things, including
             * temperature, temporary and is just a bad variable name.
             */
            val mainForecastObject = dayForecast.getJSONObject(OWM_DESCRIPTION)
            temp = mainForecastObject.getDouble(OWM_TEMPERATURE)
            high = mainForecastObject.getDouble(OWM_MAX)
            low = mainForecastObject.getDouble(OWM_MIN)
            highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low)
            parsedWeatherData[i] = "$date - $description - $highAndLow"
        }
        return parsedWeatherData
    }




    /**
     * Parse the JSON and convert it into ContentValues that can be inserted into our database.
     *
     * @param context         An application context, such as a service or activity context.
     * @param forecastJsonStr The JSON to parse into ContentValues.
     *
     * @return An array of ContentValues parsed from the JSON.
     */
    fun getWeatherContentValuesFromJson(
        context: Context?,
        forecastJsonStr: String?
    ): Array<ContentValues?>? {

        /* Max temperature for the day */
        val OWM_MAX = "temp_max"
        val OWM_MIN = "temp_min"

        val forecastJson = JSONObject(forecastJsonStr)

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            when (forecastJson.getInt(OWM_MESSAGE_CODE)) {
                HttpURLConnection.HTTP_OK -> { }
                HttpURLConnection.HTTP_NOT_FOUND -> /* Location invalid */return null
                else ->                     /* Server probably down */return null
            }
        }

        val jsonWeatherArray = forecastJson.getJSONArray(OWM_LIST)
        val cityJson = forecastJson.getJSONObject(OWM_CITY)
        val cityCoordinates = cityJson.getJSONObject(OWM_COORD)
        val cityLatitude = cityCoordinates.getDouble(OWM_LATITUDE)
        val cityLongitude = cityCoordinates.getDouble(OWM_LONGITUDE)

        setLocationDetails(context!!, cityLatitude, cityLongitude)

        val weatherContentValues =
            arrayOfNulls<ContentValues>(jsonWeatherArray.length())

        /*
         * OWM returns daily forecasts based upon the local time of the city that is being asked
         * for, which means that we need to know the GMT offset to translate this data properly.
         * Since this data is also sent in-order and the first day is always the current day, we're
         * going to take advantage of that to get a nice normalized UTC date for all of our weather.
         */
//        long now = System.currentTimeMillis();
//        long normalizedUtcStartDay = SunshineDateUtils.normalizeDate(now);


        /*
         * OWM returns daily forecasts based upon the local time of the city that is being asked
         * for, which means that we need to know the GMT offset to translate this data properly.
         * Since this data is also sent in-order and the first day is always the current day, we're
         * going to take advantage of that to get a nice normalized UTC date for all of our weather.
         */
//        long now = System.currentTimeMillis();
//        long normalizedUtcStartDay = SunshineDateUtils.normalizeDate(now);
        val normalizedUtcStartDay: Long = SunshineDateUtils.getNormalizedUtcDateForToday()

        for (i in 0 until jsonWeatherArray.length()) {
            var dateTimeMillis: Long
            var pressure: Double
            var humidity: Int
            var windSpeed: Double
            var windDirection: Double
            var high: Double
            var low: Double
            var weatherId: Int

            /* Get the JSON object representing the day */
            val dayForecast = jsonWeatherArray.getJSONObject(i)

            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */
            dateTimeMillis = normalizedUtcStartDay + SunshineDateUtils.DAY_IN_MILLIS * i
            val dayForecastMainInfo = dayForecast.getJSONObject(OWM_DESCRIPTION)
            pressure = dayForecastMainInfo.getDouble(OWM_PRESSURE)
            humidity = dayForecastMainInfo.getInt(OWM_HUMIDITY)
            high = dayForecastMainInfo.getDouble(OWM_MAX)
            low = dayForecastMainInfo.getDouble(OWM_MIN)

            val windObject = dayForecast.getJSONObject(OWM_WIND)
            windSpeed = windObject.getDouble(OWM_WINDSPEED)
            windDirection = windObject.getDouble(OWM_WIND_DIRECTION)

            /*
             * Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.
             */
            val weatherObject =
                dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0)
            weatherId = weatherObject.getInt(OWM_WEATHER_ID)



            val weatherValues = ContentValues()
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DATE, dateTimeMillis)
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_HUMIDITY, humidity)
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_PRESSURE, pressure)
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WIND_SPEED, windSpeed)
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_DEGREES, windDirection)
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MAX_TEMP, high)
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_MIN_TEMP, low)
            weatherValues.put(WeatherContract.WeatherEntry.COLUMN_WEATHER_ID, weatherId)
            weatherContentValues[i] = weatherValues
        }

        return weatherContentValues
    }









    fun getSimpleStaticWeatherStringsFromJson(
        context: Context,
        forecastJsonStr: String?
    ): Array<String?>? {


        /* String array to hold each day's weather String */
        var parsedWeatherData: Array<String?>? = null

        val forecastJson = JSONObject(forecastJsonStr)

        /* Is there an error? */
        if (forecastJson.has(OWM_MESSAGE_CODE)) {
            val errorCode = forecastJson.getInt(OWM_MESSAGE_CODE)
            when (errorCode) {
                HttpURLConnection.HTTP_OK -> {
                }
                HttpURLConnection.HTTP_NOT_FOUND ->  /* Location invalid */return null
                else ->  /* Server probably down */return null
            }
        }

        val weatherArray = forecastJson.getJSONArray(OWM_LIST)

        parsedWeatherData = arrayOfNulls(weatherArray.length())

        val localDate = System.currentTimeMillis()
        val utcDate = SunshineDateUtils.getUTCDateFromLocal(localDate)
        val startDay = SunshineDateUtils.normalizeDate(utcDate)

        for (i in 0 until weatherArray.length()) {
            var date: String
            var highAndLow: String
            /* These are the values that will be collected */
            var dateTimeMillis: Long
            var high: Double
            var low: Double
            var description: String
            /* Get the JSON object representing the day */
            val dayForecast = weatherArray.getJSONObject(i)
            /*
             * We ignore all the datetime values embedded in the JSON and assume that
             * the values are returned in-order by day (which is not guaranteed to be correct).
             */dateTimeMillis = startDay + SunshineDateUtils.DAY_IN_MILLIS * i
            date = SunshineDateUtils.getFriendlyDateString(context, dateTimeMillis, false)

            /** Description is in a child array called "weather", which is 1 element long.
             * That element also contains a weather code.*/
            val weatherObject = dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0)
            description = weatherObject.getString(OWM_DESCRIPTION)
            /** Temperatures are sent by Open Weather Map in a child object called "temp".*/
            val temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE)
            high = temperatureObject.getDouble(OWM_MAX)
            low = temperatureObject.getDouble(OWM_MIN)
            highAndLow = SunshineWeatherUtils.formatHighLows(context, high, low)
            parsedWeatherData[i] = "$date - $description - $highAndLow"
        }

        return parsedWeatherData
    }
}