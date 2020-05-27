package com.example.android.sunshine.core.interactors

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import javax.inject.Inject

class ForecastByCity
@Inject internal constructor(
    private val forecastRepository: ForecastRepository
) {

    operator fun invoke (
        params: Params
    ): LiveData<ForecastEntity> = forecastRepository.forecastByCoordinates(params.lat, params.lon, params.units)


    class Params(
        val lat: Double,
        val lon: Double,
        val units: String
    )

}