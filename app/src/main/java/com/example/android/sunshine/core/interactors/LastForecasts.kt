package com.example.android.sunshine.core.interactors

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.forecast.ForecastRepository
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import javax.inject.Inject

class LastForecasts
@Inject internal constructor(
    private val forecastRepository: ForecastRepository
) {

    operator fun invoke (): LiveData<Resource<List<ForecastEntity>>>
            = forecastRepository.lastForecasts(false)
}