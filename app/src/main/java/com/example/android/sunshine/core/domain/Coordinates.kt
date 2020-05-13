package com.example.android.sunshine.core.domain

import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
class Coordinates(
    @Json(name = "lon")
    val longitude:Double?,
    @Json(name = "lat")
    val latitude:Double?): Parcelable