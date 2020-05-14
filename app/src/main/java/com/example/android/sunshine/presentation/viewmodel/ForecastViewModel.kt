package com.example.android.sunshine.presentation.viewmodel

import android.content.Context
import androidx.annotation.UiThread
import androidx.lifecycle.*
import com.example.android.sunshine.R
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.presentation.ForecastViewState
import kotlinx.coroutines.*
import javax.inject.Inject

class ForecastViewModel
@Inject constructor(private val interactors: Interactors): ViewModel() {
    private val forecastParams: MutableLiveData<ForecastByCoordinates.Params> = MutableLiveData()

    val forecast= forecastParams.switchMap {
        interactors.forecastByCoordinates.invoke(it)
    }

    fun setForecastParams(params: ForecastByCoordinates.Params){
        if (forecastParams.value == params)
            return
        forecastParams.postValue(params)
    }


}