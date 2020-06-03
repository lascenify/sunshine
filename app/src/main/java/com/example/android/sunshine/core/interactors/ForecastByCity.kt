package com.example.android.sunshine.core.interactors

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.data.forecast.ForecastRepository
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import javax.inject.Inject

class ForecastByCity
@Inject internal constructor(
    private val forecastRepository: ForecastRepository
) {

    operator fun invoke (
        params: Params
    ): LiveData<Resource<ForecastEntity>> = forecastRepository.forecastByCity(params.cityId)

    class Params(
        val cityId: Long
    )

}

