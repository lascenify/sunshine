package com.example.android.sunshine.core.domain.forecast

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class WeatherItem(
    @Json(name = "id")
    val id:Long,

    @Json(name = "main")
    val mainName:String,

    @Json(name = "description")
    val description:String,

    @Json(name = "icon")
    val icon:String
):Parcelable