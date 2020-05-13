package com.example.android.sunshine.utilities

import androidx.room.TypeConverter
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.domain.WeatherItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


object DataConverter {

    @TypeConverter
    @JvmStatic
    fun stringToList(data: String?): List<ForecastListItem>? {
        if (data == null) {
            return emptyList()
        }
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ForecastListItem::class.java)
        val adapter = moshi.adapter<List<ForecastListItem>>(type)
        return adapter.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun listToString(objects: List<ForecastListItem>): String {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, ForecastListItem::class.java)
        val adapter = moshi.adapter<List<ForecastListItem>>(type)
        return adapter.toJson(objects)
    }

    @TypeConverter
    @JvmStatic
    fun weatherStringToList(data: String?): List<WeatherItem>? {
        if (data == null) {
            return emptyList()
        }

        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, WeatherItem::class.java)
        val adapter = moshi.adapter<List<WeatherItem>>(type)
        return adapter.fromJson(data)
    }

    @TypeConverter
    @JvmStatic
    fun weatherListToString(objects: List<WeatherItem>): String {
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(List::class.java, WeatherItem::class.java)
        val adapter = moshi.adapter<List<WeatherItem>>(type)
        return adapter.toJson(objects)
    }
}
