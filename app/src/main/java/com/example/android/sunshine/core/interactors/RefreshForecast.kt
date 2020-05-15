package com.example.android.sunshine.core.interactors

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import javax.inject.Inject

class RefreshForecast @Inject internal constructor(private val repository: ForecastRepository){
    operator fun invoke(
        params: Params
    ): LiveData<Resource<ForecastEntity>>{
        repository.removeForecastByCoordinates(params.lat, params.lon)
        return repository.forecastByCoordinates(params.lat, params.lon, params.units)
    }


    class Params(
        val lat: Double,
        val lon: Double,
        val fetchRequired: Boolean,
        val units: String
    )
}