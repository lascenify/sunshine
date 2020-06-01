package com.example.android.sunshine.core.interactors

import com.example.android.sunshine.core.data.forecast.ForecastRepository
import com.example.android.sunshine.core.data.search.CityRepository
import javax.inject.Inject

/**
 *
 */
class SearchCity @Inject internal constructor(
    private val cityRepository: CityRepository
){
    operator fun invoke (
        params: Params
    )  = cityRepository.loadCityByName(params.cityQuery)

    class Params(
        val cityQuery: String
    )
}