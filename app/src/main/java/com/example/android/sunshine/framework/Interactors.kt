package com.example.android.sunshine.framework

import com.example.android.sunshine.core.interactors.*
import javax.inject.Inject

data class Interactors @Inject constructor(
    val forecastByCoordinates: ForecastByCoordinates,
    val lastForecasts: LastForecasts,
    val searchCity: SearchCity,
    val removeCity: RemoveCity
)