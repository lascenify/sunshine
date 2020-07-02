package com.example.android.sunshine.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.android.sunshine.core.data.AppExecutors
import com.example.android.sunshine.core.domain.forecast.ForecastListItem
import com.example.android.sunshine.core.domain.forecast.OneDayForecast
import com.example.android.sunshine.core.interactors.RemoveCity
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.utilities.getDayOfWeekFromText
import com.example.android.sunshine.utilities.getNextDayOfYearFromTxt
import javax.inject.Inject

class CitiesViewModel
@Inject constructor(
    private val interactors: Interactors,
    private val appExecutors: AppExecutors
): ViewModel(){
    private var INVALID_HOURS_DATA = false
    private var INVALID_DAYS_DATA = false

    private val daysForecast: MutableList<OneDayForecast> = mutableListOf()
    private val hoursForecast: MutableList<ForecastListItem> = mutableListOf()
    val forecasts = interactors.lastForecasts.invoke()

    fun removeCity(city: ForecastEntity){
        appExecutors.diskIO().execute {
            interactors.removeCity.invoke(RemoveCity.Params(cityId = city.id?.toLong()!!))
        }
    }

    fun contains(cityId: Long): Boolean =
        forecasts.value?.data?.any { it.city?.cityId?.toLong() == cityId }!!

    fun getForecastByCityId(cityId: Long) =
        forecasts.value?.data?.find { it.id == cityId.toInt() }

    fun forecastOfNextHours(forecastEntity: ForecastEntity): MutableList<ForecastListItem> {
        if (hoursForecast.isEmpty() || INVALID_HOURS_DATA)
            fetchNextHoursForecast(forecastEntity)
        return hoursForecast
    }

    fun forecastOfNextDays(forecastEntity: ForecastEntity): MutableList<OneDayForecast>{
        if (daysForecast.isEmpty() || INVALID_DAYS_DATA)
            fetchNextDaysForecast(forecastEntity)
        return daysForecast
    }


    /**
     * In the second RecyclerView, we will display the main info of the forecast for the next days
     */
    private fun fetchNextDaysForecast(forecastEntity: ForecastEntity) {
        INVALID_DAYS_DATA = false
        daysForecast.clear()
        val forecastList = forecastEntity.list!!
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
    private fun fetchNextHoursForecast(forecastEntity: ForecastEntity){
        INVALID_HOURS_DATA = false
        val forecastList = forecastEntity.list!!
        val todayCompleteTxt = forecastList.first().dt_txt!!//.substringBefore(" ")
        val todayTxt = todayCompleteTxt.substringBefore(" ")
        val tomorrowTxt = getNextDayOfYearFromTxt(todayTxt)
        hoursForecast.clear()
        hoursForecast.addAll(forecastList.filter { it.dt_txt?.contains(todayTxt) == true || it.dt_txt?.contains(tomorrowTxt) == true })
    }

}