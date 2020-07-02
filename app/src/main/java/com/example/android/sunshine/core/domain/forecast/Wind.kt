package com.example.android.sunshine.core.domain.forecast

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Wind(
    @Json(name = "speed")
    // Wind speed. Unit default: meter/sec
    val speed:Double,

    @Json(name = "deg")
    val directionInDegrees:Double
):Parcelable