package com.example.android.sunshine.core.interactors

import com.example.android.sunshine.core.data.CityRepository
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.core.domain.City
import javax.inject.Inject


class InsertCityToForecast
@Inject internal constructor(
    private val cityRepository: CityRepository
) {
    operator fun invoke(
        city: City
    ) = cityRepository.insert(city)

}