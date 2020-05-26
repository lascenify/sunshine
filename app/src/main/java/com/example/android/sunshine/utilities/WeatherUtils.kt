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

import android.content.Context
import com.example.android.sunshine.R
import com.example.android.sunshine.framework.SunshinePreferences
import javax.inject.Inject

/**
 * Contains useful utilities for a weather app, such as conversion between Celsius and Fahrenheit,
 * from kph to mph, and from degrees to NSEW.  It also contains the mapping of weather condition
 * codes in OpenWeatherMap to strings.  These strings are contained
 */
object WeatherUtils {

    /**
     * This method will convert a temperature from Celsius to Fahrenheit.
     * @param temperatureInCelsius Temperature in degrees Celsius(°C)
     *
     * @return Temperature in degrees Fahrenheit (°F)
     */
    fun celsiusToFahrenheit(temperatureInCelsius: Double): Double = temperatureInCelsius * 1.8 + 32

    /**
     * Temperature data is stored in Celsius by our app. Depending on the user's preference,
     * the app may need to display the temperature in Fahrenheit. This method will perform that
     * temperature conversion if necessary. It will also format the temperature so that no
     * decimal points show. Temperatures will be formatted to the following form: "21°C"
     *
     * @param context     Android Context to access preferences and resources
     * @param temperature Temperature in degrees Celsius (°C)
     *
     * @return Formatted temperature String in the following form:
     * "21°C"
     */
    fun formatTemperature(
        context: Context,
        temperature: Double
    ): String {
        var temperature = temperature
        var temperatureFormatResourceId: Int = R.string.format_temperature_celsius
        if (!SunshinePreferences.isMetric(context)) {
            temperature = celsiusToFahrenheit(temperature)
            temperatureFormatResourceId = R.string.format_temperature_fahrenheit
        }
        /* For presentation, assume the user doesn't care about tenths of a degree. */
        return String.format(
            context.getString(temperatureFormatResourceId),
            temperature
        )
    }

    /**
     * Performs the conversion Celsius - Kelvin if necessary, but returns a simpler string.
     * Temperatures will be formated to the following form: "21º"
     */
    fun formatSimpleTemperature(
        context: Context,
        temperature: Int
    ): String {
        val formattedTemp = formatTemperature(context, temperature.toDouble())
        return formattedTemp.dropLast(1)
    }


    /**
     * This method uses the wind direction in degrees to determine compass direction as a
     * String. (eg NW) The method will return the wind String in the following form: "2 km/h SW"
     *
     * @param context   Android Context to access preferences and resources
     * @param windSpeed Wind speed in kilometers / hour
     * @param degrees   Degrees as measured on a compass, NOT temperature degrees!
     * See https://www.mathsisfun.com/geometry/degrees.html
     *
     * @return Wind String in the following form: "2 km/h SW"
     */
    fun getFormattedWind(
        context: Context,
        windSpeed: Float,
        degrees: Float
    ): String {
        var windSpeed = windSpeed
        var windFormat: Int = R.string.format_wind_kmh
        if (!SunshinePreferences.isMetric(context)) {
            windFormat = R.string.format_wind_mph
            windSpeed *= .621371192237334f
        }
        var direction = "Unknown"
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N"
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE"
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E"
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE"
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S"
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW"
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W"
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW"
        }
        return String.format(context.getString(windFormat), windSpeed, direction)
    }


    /**
     * The API returns an icon path associated to each forecast item. This path
     * needs to be converted into our app naming convention.
     * @param iconPath returned from the API
     * @return the new icon name associated with the resource. This will be used
     * to get the icon of that forecast.
     */
    fun getIconResourceFromIconPath(iconPath: String?) = when(iconPath){
        "01d" -> "ic_01d"
        "01n" -> "ic_01n"
        "02d" -> "ic_02d"
        "02n" -> "ic_02n"
        "03d", "03n", "04d", "04n" -> "ic_03"
        "09d" -> "ic_09d"
        "09n" -> "ic_09n"
        "10d", "10n" -> "ic_10"
        "11d", "11n" -> "ic_11"
        "13d", "13n" -> "ic_13"
        "50d" -> "ic_50"
        "50n" -> "ic_50n"
        else -> "rainbow"
    }



}