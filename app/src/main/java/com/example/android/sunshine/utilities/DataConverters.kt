package com.example.android.sunshine.utilities

import androidx.room.TypeConverter
import com.example.android.sunshine.core.domain.forecast.City
import com.example.android.sunshine.core.domain.forecast.ForecastListItem
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


object DataConverter {

    /**
     * Method to convert a json string to a List<ForecastListItem>.
     * The data passed as a parameter should be equal to the list json object of the response.
     * @param data: json string containing the list of forecasts.
     * @return the list of ForecastListItem casted
     */
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

    /**
     * Method to convert a list of ForecastListItem into a string with json format.
     * @param objects: list of ForecastListItem
     * @return string with json format containing all the items from the list.
     */
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
    fun stringToCity(data: String?): City?{
        if (data == null)
            return null
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(City::class.java)
        val adapter = moshi.adapter<City>(type)
        return adapter.fromJson(data)
    }


    @TypeConverter
    @JvmStatic
    fun cityToString(city: City?): String?{
        val moshi = Moshi.Builder().build()
        val type = Types.newParameterizedType(City::class.java)
        val adapter = moshi.adapter<City>(type)
        return adapter.toJson(city)
    }
}
