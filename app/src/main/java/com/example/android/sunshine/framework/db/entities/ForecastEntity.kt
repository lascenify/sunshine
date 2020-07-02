package com.example.android.sunshine.framework.db.entities

import android.os.Parcelable
import androidx.room.*
import com.example.android.sunshine.core.domain.forecast.ForecastListItem
import com.example.android.sunshine.core.domain.forecast.ForecastResponse
import kotlinx.android.parcel.Parcelize


@Parcelize
@Entity(tableName = "Forecast")
data class ForecastEntity(

    @PrimaryKey (autoGenerate = false)
    val id: Int?,

    @ColumnInfo(name = "list")
    // list of forecast. In case of 5 day weather forecast, 40 elements in this list (each 3 hours)
    val list: List<ForecastListItem>?,

    @Embedded
    val city: CityEntity?) : Parcelable{

    @Ignore
    constructor(forecastResponse: ForecastResponse): this(
        id = forecastResponse.city?.cityId,
        list = forecastResponse.list,
        city = forecastResponse.city?.let { CityEntity(it) }
    )

    fun getCityName(): String = city?.name!!

    fun getActualWeatherDescription(): String  = getActualWeather().weatherItem?.get(0)?.description.toString()

    private fun getActualWeather(): ForecastListItem = list?.first()!!

    fun getActualTemperature(): Double = getActualWeather().mainInfo?.temperature!!
}
