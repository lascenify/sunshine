package com.example.android.sunshine.core.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class ForecastListItem(

    @Json(name = "dt")
    //Time in which the forecast is done, in millis
    val dt:Long?,

    @Json(name = "rain")
    val rain: Rain?,

    @Json(name = "snow")
    val snow: Snow?,

    @Json(name = "main")
    // Forecast main information
    val mainInfo: ForecastMainInfo?,

    @Json(name = "weather")
    // More info weather condition codes
    val weatherItem: List<WeatherItem?>?,

    @Json(name = "clouds")
    val clouds: Clouds?,

    @Json(name = "wind")
    val wind: Wind?,

    @Json(name = "sys")
    val sys: Sys?,

    @Json(name = "dt_txt")
    //Time in which the forecast is done, in txt
    val dt_txt:String?
):Parcelable{

    fun getHourOfDay() = dt_txt?.substringAfter(" ")?.substringBeforeLast(":") ?: "00:00"

    fun getTemperature() = mainInfo?.temperature.toString()

    fun getIcon() = weatherItem?.first()?.icon
}


