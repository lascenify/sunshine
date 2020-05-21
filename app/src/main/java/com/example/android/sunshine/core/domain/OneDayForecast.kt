package com.example.android.sunshine.core.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.DayOfWeek

@Parcelize
data class OneDayForecast(
    val dayOfWeek: String,
    val forecastList: List<ForecastListItem>,
    private var maxTemperature: Int?,
    private var minTemperature: Int?
): Parcelable{
    fun calculateTemperatures(){
        var min = forecastList[0].mainInfo?.temperature!!
        var max = forecastList[0].mainInfo?.temperature!!
        forecastList.forEach {forecastItem ->
            val temperature = forecastItem.mainInfo?.temperature!!
            if(temperature > max)
                max = temperature
            if (temperature < min)
                min = temperature
        }
        maxTemperature = max.toInt()
        minTemperature = min.toInt()
    }

    /**
     * We get one icon from the middle of the day
     */
    fun getIcon() = forecastList[3].getIcon()

    fun getMaxTemperature(): String {
        if (maxTemperature == null)
            calculateTemperatures()
        return maxTemperature.toString() + "º"
    }

    fun getMinTemperature(): String {
        if (minTemperature == null)
            calculateTemperatures()
        return minTemperature.toString() + "º"
    }

}