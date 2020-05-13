package com.example.android.sunshine.framework

import com.example.android.sunshine.BuildConfig

object Constants {
    /** Tag that will identify this job **/
    const val SUNSHINE_SYNC_WORK_NAME = "sunshine_syncing_work"

    object NetworkService{
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
        const val API_KEY_VALUE =
            BuildConfig.OPEN_WEATHER_API_KEY
        const val RATE_LIMITER_TYPE = "data"

        const val FORECAST = "forecast"

        const val API_KEY_QUERY = "appid"
        const val PARAM_LAT = "lat"
        const val PARAM_LON = "lon"
        const val PARAM_UNITS = "units"
        const val PARAM_CITY = "q"
        const val PARAM_CITY_ID = "id"

    }

    object Database{
        const val DATABASE_NAME = "sunshine"
    }
}