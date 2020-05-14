package com.example.android.sunshine.presentation.viewmodel

import android.text.format.DateUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.domain.OneDayForecast
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.utilities.getDayOfWeekFromDt
import java.util.*
import javax.inject.Inject

class ForecastViewModel
@Inject constructor(private val interactors: Interactors): ViewModel() {
    private var firstHoursCounter = 0
    private lateinit var daysForecast: MutableList<OneDayForecast>
    private lateinit var hoursFromTodayForecast: MutableList<ForecastListItem>
    private val forecastParams: MutableLiveData<ForecastByCoordinates.Params> = MutableLiveData()

    val forecast= forecastParams.switchMap {
        interactors.forecastByCoordinates.invoke(it)
    }

    fun setForecastParams(params: ForecastByCoordinates.Params){
        if (forecastParams.value == params)
            return
        forecastParams.postValue(params)
    }

    /**
     * 40 registros
     * 5 dias: 3 dias con 8 registros. Primer dia depende, ultimo dia depende
     * si son las 12, me devuelve 12, 15, 18, 21 -> 4 registros
     * si son las 18, me devuelve 18, 21 -> 2 registros
     * los 3 dias siguientes -> 8 registros
     * el ultimo dia -> 8 - registros del primer dia
     * si primer dia 4 registros -> ultimo dia 4
     * si primer dia 2 -> ultimo dia 6
     *
     *
     *
     * Lo q voy a hacer : recyler horizontal poner cada 3 horas hasta mañana
     * recycler vertical: resumen de los 4 proximos dias (temp maxima y minima, nombre dia e icono)
     */

    /**
     * We start from the second day and add the rest to the second recycler

    private fun fillOtherDays() {
        var dayCounter = 0
        val forecastList = forecast.value?.data!!.list!!
        val restOfDays = forecastList.subList(firstHoursCounter, forecastList.size - 1)
        var currentDay = createNewDay(restOfDays[dayCounter].dt!!)
        restOfDays.forEach { forecastItem ->
            val hour = forecastItem.getHourOfDay()
            if (isItANewDay(hour)){
                dayCounter ++
                currentDay = createNewDay(restOfDays[dayCounter].dt!!)
            }
            else{
                currentDay.forecastList.add(forecastItem)
            }
        }
    }

    private fun createNewDay(dt: Long): OneDayForecast {
        return OneDayForecast(
            getDayOfWeekFromDt(dt),
            mutableListOf(),
            null,
            null
        )
    }

    private fun fillFirstTwoDays(){
        hoursFromTodayForecast = mutableListOf()
        var SECOND_DAY_FLAG = false
        val forecastList = forecast.value?.data!!.list!!
        forecastList.forEach { forecastItem ->
            val hour = forecastItem.getHourOfDay()
            if (isItANewDay(hour)) {
                if (!SECOND_DAY_FLAG) {
                    SECOND_DAY_FLAG = true
                    hoursFromTodayForecast.add(forecastItem)
                }
                else
                    return@forEach
            }
            else {
                hoursFromTodayForecast.add(forecastItem)
                if (!SECOND_DAY_FLAG)
                    firstHoursCounter ++
            }
        }
    }
     */


    private fun isItANewDay(hour: String) = hour == "00:00"


    fun fetchForecastAtPosition(position: Int) = forecast.value?.data?.list?.get(position)
}