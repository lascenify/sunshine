package com.example.android.sunshine.presentation.city

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.domain.OneDayForecast
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.framework.Interactors
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
    private val _forecastParams: MutableLiveData<ForecastByCoordinates.Params> = MutableLiveData()

    val forecastParams: LiveData<ForecastByCoordinates.Params>
        get() = _forecastParams

    var forecast= _forecastParams.switchMap { params ->
        if (params == null){
            AbsentLiveData.create()
        } else {
            INVALID_HOURS_DATA = true
            INVALID_DAYS_DATA = true
            interactors.forecastByCoordinates.invoke(params)
        }
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

    fun setForecastParams(params: ForecastByCoordinates.Params?){
        if (_forecastParams.value != params)
            _forecastParams.value = params
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
            val oneDayForecast = OneDayForecast(
                getDayOfWeekFromText(nextDayTxt),
                forecastListFromDay,
                null,
                null)
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