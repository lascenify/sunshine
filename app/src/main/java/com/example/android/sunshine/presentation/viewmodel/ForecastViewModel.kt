package com.example.android.sunshine.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.forecast.ForecastListItem
import com.example.android.sunshine.core.domain.forecast.OneDayForecast
import com.example.android.sunshine.core.interactors.ForecastByCity
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.framework.di.ForecastScope
import com.example.android.sunshine.utilities.AbsentLiveData
import com.example.android.sunshine.utilities.getDayOfWeekFromText
import com.example.android.sunshine.utilities.getNextDayOfYearFromTxt
import javax.inject.Inject

@ForecastScope
class ForecastViewModel
@Inject constructor(
    private val interactors: Interactors
): ViewModel() {

    private var INVALID_HOURS_DATA = false
    private var INVALID_DAYS_DATA = false

    private val daysForecast: MutableList<OneDayForecast> = mutableListOf()
    private val hoursForecast: MutableList<ForecastListItem> = mutableListOf()
    private val _forecastParams: MutableLiveData<ForecastByCity.Params> = MutableLiveData()
    val forecastParams: LiveData<ForecastByCity.Params>
        get() = _forecastParams

    private val _selectedDay = MutableLiveData<OneDayForecast>()

    val selectedDay: LiveData<OneDayForecast>
        get() = _selectedDay

    var forecast: LiveData<Resource<ForecastEntity>> = _forecastParams.switchMap { params ->
        if (params == null){
            AbsentLiveData.create()
        } else {
            INVALID_HOURS_DATA = true
            INVALID_DAYS_DATA = true
            interactors.forecastByCity.invoke(params)
        }
    }

    fun setForecastParams(params: ForecastByCity.Params?){
        if (_forecastParams.value != params)
            _forecastParams.value = params
    }

    fun setSelectedDay(oneDayForecast: OneDayForecast){
        _selectedDay.value = oneDayForecast
    }

    fun forecastOfNextHours(): MutableList<ForecastListItem> {
        if (hoursForecast.isEmpty() || INVALID_HOURS_DATA)
            fetchNextHoursForecast()
        return hoursForecast
    }

    fun forecastOfNextDays(): MutableList<OneDayForecast>{
        if (daysForecast.isEmpty() || INVALID_DAYS_DATA)
            fetchNextDaysForecast()
        return daysForecast
    }


    /**
     * In the second RecyclerView, we will display the main info of the forecast for the next days
     */
    private fun fetchNextDaysForecast() {
        INVALID_DAYS_DATA = false
        daysForecast.clear()
        val forecastList = forecast.value?.data?.list!!
        var previousDayTxt = forecastList.first().dt_txt!!.substringBefore(" ")
        // Next 4 days
        for (x in 0 until 4){
            val nextDayTxt = getNextDayOfYearFromTxt(previousDayTxt)
            val forecastListFromDay = forecastList.filter { it.dt_txt?.contains(nextDayTxt) == true}
            val oneDayForecast =
                OneDayForecast(
                    getDayOfWeekFromText(nextDayTxt),
                    forecastListFromDay,
                    null,
                    null
                )
            oneDayForecast.calculateTemperatures()
            daysForecast.add(oneDayForecast)
            previousDayTxt = nextDayTxt
        }

    }

    /**
     * We only need the next hours to be displayed in the first RecyclerView.
     * These are the hours in which the forecast has been done and the next day.
     */
    private fun fetchNextHoursForecast(){
        INVALID_HOURS_DATA = false
        val forecastList = forecast.value?.data?.list!!
        val todayCompleteTxt = forecastList.first().dt_txt!!//.substringBefore(" ")
        val todayTxt = todayCompleteTxt.substringBefore(" ")
        val tomorrowTxt = getNextDayOfYearFromTxt(todayTxt)
        hoursForecast.clear()
        hoursForecast.addAll(forecastList.filter { it.dt_txt?.contains(todayTxt) == true || it.dt_txt?.contains(tomorrowTxt) == true })
    }

    /**
     * This function is used if the user wants to manually refresh the data.
     * The update of the value of forecastParams will make the interactors to call
     * the forecast function again.
     */
    fun retry(){
        _forecastParams.value?.let {
            _forecastParams.value = it
        }
    }
}