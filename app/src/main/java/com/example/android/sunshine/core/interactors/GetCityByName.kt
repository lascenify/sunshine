package com.example.android.sunshine.core.interactors

import androidx.lifecycle.MutableLiveData
import com.example.android.sunshine.core.data.CityRepository
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.framework.db.entities.CityEntity
import javax.inject.Inject

class GetCityByName @Inject internal constructor(
    private val cityRepository: CityRepository
){
    operator fun invoke (
        params: List<String>
    )  = cityRepository.citiesByName(params)

}