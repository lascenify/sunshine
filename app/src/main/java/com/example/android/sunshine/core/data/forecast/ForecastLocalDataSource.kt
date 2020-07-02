package com.example.android.sunshine.core.data.forecast

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.domain.forecast.ForecastResponse
import com.example.android.sunshine.framework.db.entities.ForecastEntity

interface ForecastLocalDataSource {

    fun forecastByCoordinates(
        lat: Double,
        lon: Double
    ) : LiveData<ForecastEntity?>

    fun forecastByCityId(
        cityId: Long
    ): LiveData<ForecastEntity?>

    fun lastForecasts(): LiveData<List<ForecastEntity>?>

    fun count(): Int

    fun insert(forecast: ForecastResponse)

    fun insertAll(list: List<ForecastResponse>)

    fun remove(cityId: Long)

    fun removeAll()

    fun removeByCoordinates(
        lat: Double,
        lon: Double
    )
}