package com.example.android.sunshine.core.domain

import java.time.DayOfWeek

data class OneDayForecast(
    val dayOfWeek: String,
    val forecastList: MutableList<ForecastListItem>,
    var maxTemperature: Double?,
    var minTemperature: Double?
)