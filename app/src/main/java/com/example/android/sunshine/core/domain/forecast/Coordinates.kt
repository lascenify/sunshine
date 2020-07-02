package com.example.android.sunshine.core.domain.forecast

import android.os.Parcelable
import com.example.android.sunshine.framework.db.entities.CoordinatesEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
class Coordinates(
    @Json(name = "lon")
    val longitude:Double?,
    @Json(name = "lat")
    val latitude:Double?): Parcelable{
    constructor(coordinates: CoordinatesEntity): this(
        longitude = coordinates.lon,
        latitude = coordinates.lat
    )
}