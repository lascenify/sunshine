package com.example.android.sunshine.core.interactors

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.framework.db.entities.asDomainModel
import com.example.android.sunshine.presentation.ForecastViewState
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel
import javax.inject.Inject

/**
 * Use case that calls the repository to extract the forecast.
 * This forecast can come from the local persistence database or from the API
 */
class ForecastByCoordinates @Inject internal constructor(private val forecastRepository: ForecastRepository) {

    operator fun invoke (
        params: Params
    )  = //forecastRepository.testingForecast(params.lat, params.lon, params.units)
        forecastRepository.forecastByCoordinates(params.lat, params.lon, params.units)
        //.map { onForecastResultReady(it) }





    fun onForecastResultReady(resource: Resource<ForecastEntity>): ForecastViewState =
        ForecastViewState(
            status = resource.status,
            error = resource.message,
            data = resource.data
        )

    class Params(
        val lat: Double,
        val lon: Double,
        val fetchRequired: Boolean,
        val units: String
    )

}

