package com.example.android.sunshine.core.interactors

import com.example.android.sunshine.core.data.ForecastRepository
import javax.inject.Inject

/**
 * Use case that calls the repository to extract the forecast.
 * This forecast can come from the local persistence database or from the API
 */
class ForecastByCoordinates @Inject internal constructor(private val forecastRepository: ForecastRepository) {

    operator fun invoke (
        params: Params
    )  = forecastRepository.forecastByCoordinates(params.lat, params.lon, params.units)

    class Params(
        val lat: Double,
        val lon: Double,
        val units: String
    )

}

