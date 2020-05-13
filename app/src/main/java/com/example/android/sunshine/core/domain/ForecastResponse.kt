package com.example.android.sunshine.core.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
data class ForecastResponse(

    @Json(name = "cod")
    // internal parameter
    val code:String?,

    @Json(name = "message")
    // internal parameter
    val message:Long?,

    @Json(name = "cnt")
    // number of lines returned by the API call
    val numberOfLines:Long?,

    @Json(name = "list")
    // list of forecast. In case of 5 day weather forecast, 40 elements in this list (each 3 hours)
    val list:List<ForecastListItem>?,

    @Json(name = "city")
    // city from which the forecast is done
    val city:City?
): Parcelable