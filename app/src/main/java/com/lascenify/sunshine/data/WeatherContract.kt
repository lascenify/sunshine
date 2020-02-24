package com.lascenify.sunshine.data

import android.provider.BaseColumns

class WeatherContract {
    class WeatherEntry : BaseColumns {
        companion object {
            const val TABLE_NAME = "weather"
            const val _ID = "_id"
            const val COLUMN_DATE = "date"
            const val COLUMN_WEATHER_ID = "weather_id"
            const val COLUMN_MIN_TEMP = "min"
            const val COLUMN_MAX_TEMP = "max"
            const val COLUMN_HUMIDITY = "humidity"
            const val COLUMN_PRESSURE = "pressure"
            const val COLUMN_WIND_SPEED = "wind"
            const val COLUMN_DEGREES = "degrees"
        }
    }
}