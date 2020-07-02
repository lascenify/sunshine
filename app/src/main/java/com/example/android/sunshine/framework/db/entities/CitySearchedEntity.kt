package com.example.android.sunshine.framework.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "CitiesForSearch")
data class CitySearchedEntity(
    @ColumnInfo(name = "administrative")
    val administrative: String?,
    @ColumnInfo(name = "Country")
    val country: String?,
    @Embedded
    val coord: CoordinatesEntity?,
    @ColumnInfo(name = "fullName")
    val name: String?,
    @ColumnInfo(name = "county")
    val county: String?,
    @ColumnInfo(name = "importance")
    val importance: Int?,
    @PrimaryKey
    @ColumnInfo(name = "Id")
    val id: String
) : Parcelable
