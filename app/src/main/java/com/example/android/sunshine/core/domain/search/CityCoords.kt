package com.example.android.sunshine.core.domain.search

import android.os.Parcelable
import com.example.android.sunshine.framework.db.entities.CoordinatesEntity
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize


@Parcelize
@JsonClass(generateAdapter = true)
class CityCoords(
    @Json(name = "lng")
    val longitude:Double?,
    @Json(name = "lat")
    val latitude:Double?): Parcelable{
    constructor(coordinates: CoordinatesEntity): this(
        longitude = coordinates.lon,
        latitude = coordinates.lat
    )
}