package com.example.android.sunshine.framework.db.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.example.android.sunshine.core.domain.ForecastMainInfo
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Main")
data class ForecastMainInfoEntity(
    @ColumnInfo(name = "temp")
    val temperature:Double,

    @ColumnInfo(name = "feelsLike")
    val feelsLike:Double,

    @ColumnInfo(name = "tempMin")
    val minTemperature:Double,

    @ColumnInfo(name = "tempMax")
    val maxTemperature:Double,

    @ColumnInfo(name = "pressure")
    // Atmospheric pressure on the sea level by default
    val pressure:Double,

    @ColumnInfo(name = "seaLevel")
    // Atmospheric pressure on the sea level
    val seaLevel:Long,

    @ColumnInfo(name = "grndLevel")
    // Atmospheric pressure on the ground level
    val grndLevel:Long,

    @ColumnInfo(name = "humidity")
    // % Humidity
    val humidity:Double,

    @ColumnInfo(name = "tempKf")
    // internal parameter
    val tempKf:Double
): Parcelable {
    @Ignore
    constructor(forecastMainInfo: ForecastMainInfo): this(
        temperature = forecastMainInfo.temperature,
        feelsLike = forecastMainInfo.feelsLike,
        minTemperature = forecastMainInfo.minTemperature,
        maxTemperature = forecastMainInfo.maxTemperature,
        pressure = forecastMainInfo.pressure,
        seaLevel = forecastMainInfo.seaLevel,
        grndLevel = forecastMainInfo.grndLevel,
        humidity = forecastMainInfo.humidity,
        tempKf = forecastMainInfo.tempKf)
}
