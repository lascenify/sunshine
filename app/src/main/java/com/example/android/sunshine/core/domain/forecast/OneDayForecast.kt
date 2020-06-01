package com.example.android.sunshine.core.domain.forecast

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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

    fun getMaxTemperature(): Int {
        if (maxTemperature == null)
            calculateTemperatures()
        return maxTemperature!!
    }

    fun getMinTemperature(): Int {
        if (minTemperature == null)
            calculateTemperatures()
        return minTemperature!!
    }

}