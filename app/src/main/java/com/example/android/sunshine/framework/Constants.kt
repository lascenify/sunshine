package com.example.android.sunshine.framework

import com.example.android.sunshine.BuildConfig

object Constants {
    /** Tag that will identify this job **/
    const val SUNSHINE_SYNC_WORK_NAME = "sunshine_syncing_work"

    object NetworkService{
        const val BASE_FORECAST_URL = "https://api.openweathermap.org/data/2.5/"
        const val FORECAST_API_KEY_VALUE =
            BuildConfig.OPEN_WEATHER_API_KEY
        const val RATE_LIMITER_TYPE = "data"

        const val FORECAST = "forecast"
        const val FORECAST_API_KEY_QUERY = "appid"
        const val PARAM_LAT = "lat"
        const val PARAM_LON = "lon"
        const val PARAM_UNITS = "units"
        const val PARAM_CITY = "q"
        const val PARAM_CITY_ID = "id"

        const val BASE_CITY_URL = "https://places-dsn.algolia.net/1/places/"
        const val GET_CITY = "query"
        const val CITY_API_KEY_VALUE =
            BuildConfig.OPEN_WEATHER_API_KEY
        const val PARAM_CITY_SEARCH = "query"
        const val PARAM_LANG = "language"
        const val CITY_API_KEY_QUERY = "X-Algolia-API-Key"
        const val CITY_APP_ID_QUERY = "X-Algolia-Application-Id"

    }

    object Database{
        const val DATABASE_NAME = "sunshine"
    }
}