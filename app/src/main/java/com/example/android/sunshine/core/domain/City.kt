
package com.example.android.sunshine.core.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class City(
    @Json(name = "id")
    val cityId: Int,

    @Json(name = "name")
    val name:String,

    @Json(name = "coord")
    val coordinates: Coordinates?,

    /*@Json(name = "country")
    val country:String,*/

    @Json(name = "timezone")
    val timezone: Long,

    @Json(name = "sunrise")
    val sunriseTime:Long,

    @Json(name = "sunset")
    val sunsetTime:Long): Parcelable