package com.example.android.sunshine.framework.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.example.android.sunshine.core.domain.forecast.Coordinates
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Coordinates")
data class CoordinatesEntity(
    @ColumnInfo(name = "lon")
    val lon: Double?,
    @ColumnInfo(name = "lat")
    val lat: Double?
) : Parcelable {
    @Ignore
    constructor(coordinates: Coordinates) : this(
        lon = coordinates.longitude,
        lat = coordinates.latitude)
}

fun CoordinatesEntity.asDomainModel(): Coordinates {
    return Coordinates(
        longitude = lon!!,
        latitude = lat!!
    )
}