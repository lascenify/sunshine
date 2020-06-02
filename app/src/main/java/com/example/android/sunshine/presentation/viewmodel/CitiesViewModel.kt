package com.example.android.sunshine.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.sunshine.core.data.AppExecutors
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.forecast.ForecastResponse
import com.example.android.sunshine.core.interactors.RemoveCity
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import javax.inject.Inject

class CitiesViewModel
@Inject constructor(
    private val interactors: Interactors,
    private val appExecutors: AppExecutors
): ViewModel(){

    val forecasts = interactors.lastForecasts.invoke()

    fun removeCity(city: ForecastEntity){
        appExecutors.diskIO().execute {
            interactors.removeCity.invoke(RemoveCity.Params(cityId = city.id?.toLong()!!))
        }
    }

}