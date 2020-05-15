package com.example.android.sunshine.core.domain

import java.time.DayOfWeek

data class OneDayForecast(
    val dayOfWeek: String,
    val forecastList: List<ForecastListItem>,
    private var maxTemperature: Double?,
    private var minTemperature: Double?
){
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
        maxTemperature = max
        minTemperature = min
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