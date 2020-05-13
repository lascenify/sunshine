package com.example.android.sunshine.core.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class ForecastMainInfo(
    @Json(name = "temp")
    val temperature:Double,

    @Json(name = "feels_like")
    val feelsLike:Double,

    @Json(name = "temp_min")
    val minTemperature:Double,

    @Json(name = "temp_max")
    val maxTemperature:Double,

    @Json(name = "pressure")
    // Atmospheric pressure on the sea level by default
    val pressure:Double,

    @Json(name = "sea_level")
    // Atmospheric pressure on the sea level
    val seaLevel:Long,

    @Json(name = "grnd_level")
    // Atmospheric pressure on the ground level
    val grndLevel:Long,

    @Json(name = "humidity")
    // % Humidity
    val humidity:Double,

    @Json(name = "temp_kf")
    // internal parameter
    val tempKf:Double
):Parcelable