package com.example.android.sunshine.framework.db.entities

import android.os.Parcelable
import androidx.room.*
import com.example.android.sunshine.core.domain.City
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity (tableName = "City")
data class CityEntity(
    @PrimaryKey
    @ColumnInfo(name = "cityId")
    val cityId: Int,
    @ColumnInfo(name = "cityName")
    val name:String,
    @Embedded
    val coordinates: CoordinatesEntity?,
    /*@ColumnInfo(name = "cityCountry")
    val country:String,*/
    @ColumnInfo(name = "timezone")
    val timezone: Long,
    @ColumnInfo(name = "sunriseTime")
    val sunriseTime:Long,
    @ColumnInfo(name = "sunsetTime")
    val sunsetTime:Long) :Parcelable{
    @Ignore
    constructor(city: City) : this (
        cityId = city.cityId,
        name = city.name,
        coordinates = city.coordinates?.let { CoordinatesEntity(it) },
        //country = city.country,
        timezone = city.timezone,
        sunriseTime = city.sunriseTime,
        sunsetTime = city.sunsetTime)
}

fun CityEntity.asDomainModel():City{
    return City(
        cityId = cityId,
        name = name,
        coordinates = coordinates?.asDomainModel(),
        //country = country,
        timezone = timezone,
        sunriseTime = sunriseTime,
        sunsetTime = sunsetTime
    )
}