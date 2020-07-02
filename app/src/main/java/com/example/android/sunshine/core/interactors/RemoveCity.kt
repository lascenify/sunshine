package com.example.android.sunshine.core.interactors

import com.example.android.sunshine.core.data.forecast.ForecastRepository
import com.example.android.sunshine.core.domain.forecast.ForecastResponse
import javax.inject.Inject

class RemoveCity @Inject internal constructor(
    private val forecastRepository: ForecastRepository
){
    operator fun invoke (
        params: Params
    )  = forecastRepository.removeForecast(params.cityId)

    class Params(
        val cityId: Long
    )
}