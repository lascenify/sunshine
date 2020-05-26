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
package com.example.android.sunshine.framework

import android.content.Context
import androidx.preference.PreferenceManager
import com.example.android.sunshine.R

object SunshinePreferences {
    /*
     * Human readable location string, provided by the API.  Because for styling,
     * "Mountain View" is more recognizable than 94043.
     */
    const val PREF_CITY_NAME = "city_name"
    private const val PREF_COORD_LAT = "coord_lat"
    private const val PREF_COORD_LONG = "coord_long"

    private val defaultWeatherCoordinates = doubleArrayOf(37.4284, 122.0724)

    /**
     * Helper method to handle setting location details in Preferences (City Name, Latitude,
     * Longitude)
     *
     * @param context        Context used to get the SharedPreferences
     * @param lat      The latitude of the city
     * @param lon      The longitude of the city
     */
    fun setLocationDetails(context: Context, lat: Double, lon: Double) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.putLong(PREF_COORD_LAT, lat.toLong())
        editor.putLong(PREF_COORD_LONG, lon.toLong())
        editor.apply()
    }

    /**
     * Resets the stored location coordinates.
     *
     * @param context Context used to get the SharedPreferences
     */
    fun resetLocationCoordinates(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPreferences.edit()

        editor.remove(PREF_COORD_LAT)
        editor.remove(PREF_COORD_LONG)
        editor.apply()
    }

    /**
     * Returns the location currently set in Preferences.
     *
     * @param context Context used to get the SharedPreferences
     * @return Location The current user has set in SharedPreferences.
     */
    fun getPreferredWeatherLocation(context: Context): String {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val defaultLocation = context.getString(R.string.pref_location_default)
        return sharedPreferences.getString(context.getString(R.string.pref_location_key), defaultLocation)!!
    }

    /**
     * Returns true if the user has selected metric temperature display.
     *
     * @param context Context used to get the SharedPreferences
     * @return true If metric display should be used
     */
    fun isMetric(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val metric = context.getString(R.string.pref_celsius_unit)
        val keyForUnits = context.getString(R.string.pref_units_key)
        val preferredUnits = sharedPreferences.getString(keyForUnits, metric)


        return metric == preferredUnits
    }

    /**
     * Returns the location coordinates associated with the location.  Note that these coordinates
     * may not be set, which results in (0,0) being returned. (conveniently, 0,0 is in the middle
     * of the ocean off the west coast of Africa)
     *
     * @param context Used to get the SharedPreferences
     * @return An array containing the two coordinate values.
     */
    fun getLocationCoordinates(context: Context): DoubleArray {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val preferredCoordinates = DoubleArray(2)
        preferredCoordinates[0] = sharedPreferences.getLong(PREF_COORD_LAT, 0L).toDouble()
        preferredCoordinates[1] = sharedPreferences.getLong(PREF_COORD_LONG, 0L).toDouble()

        return preferredCoordinates
    }

    /**
     * Returns true if the latitude and longitude values are available.
     *
     * @param context used to get the SharedPreferences
     * @return true if lat/long are set
     */
    fun isLocationLatLonAvailable(context: Context): Boolean {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return (sharedPreferences.contains(PREF_COORD_LONG) && sharedPreferences.contains(
            PREF_COORD_LAT
        ))
    }

}