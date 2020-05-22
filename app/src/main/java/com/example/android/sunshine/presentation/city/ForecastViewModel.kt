package com.example.android.sunshine.presentation.city

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.domain.OneDayForecast
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.di.ForecastScope
import com.example.android.sunshine.utilities.getDayOfWeekFromText
import com.example.android.sunshine.utilities.getNextDayOfYearFromTxt
import javax.inject.Inject

@ForecastScope
class ForecastViewModel
@Inject constructor(
    private val interactors: Interactors
): ViewModel() {

    private val daysForecast: MutableList<OneDayForecast> = mutableListOf()
    private val hoursForecast: MutableList<ForecastListItem> = mutableListOf()
    private val forecastParams: MutableLiveData<ForecastByCoordinates.Params> = MutableLiveData()

    var forecast= forecastParams.switchMap {
        interactors.forecastByCoordinates.invoke(it)
    }

    fun forecastOfNextHours(): MutableList<ForecastListItem> {
        if (hoursForecast.isEmpty())
            fetchNextHoursForecast()
        return hoursForecast
    }

    fun forecastOfNextDays(): MutableList<OneDayForecast>{
        if (daysForecast.isEmpty())
            fetchNextDaysForecast()
        return daysForecast
    }

    fun setForecastParams(params: ForecastByCoordinates.Params){
        if (forecastParams.value != params)
            forecastParams.postValue(params)
    }


    fun forceRefresh(){
        /*forecast = forecastParams.switchMap {
            interactors.refreshForecast.invoke(it)
        }*/
    }


    /**
     * In the second RecyclerView, we will display the main info of the forecast for the next days
     */
    private fun fetchNextDaysForecast() {
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
        val forecastList = forecast.value?.data?.list!!
        val todayCompleteTxt = forecastList.first().dt_txt!!//.substringBefore(" ")
        val todayTxt = todayCompleteTxt.substringBefore(" ")
        val tomorrowTxt = getNextDayOfYearFromTxt(todayTxt)
        hoursForecast.clear()
        hoursForecast.addAll(forecastList.filter { it.dt_txt?.contains(todayTxt) == true || it.dt_txt?.contains(tomorrowTxt) == true })
    }

}