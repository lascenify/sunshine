package com.example.android.sunshine.presentation.day

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.sunshine.core.domain.OneDayForecast
import com.example.android.sunshine.framework.di.ForecastScope
import javax.inject.Inject

@ForecastScope
class DayViewModel @Inject constructor(
): ViewModel(){
    private var _forecast: MutableLiveData<OneDayForecast>  = MutableLiveData()

    val forecast: LiveData<OneDayForecast>
        get() = _forecast

    fun setDayForecast(day: OneDayForecast){
        if (day != _forecast.value)
            _forecast.postValue(day)
    }

}