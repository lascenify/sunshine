package com.example.android.sunshine.core.data

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.domain.ForecastResponse
import com.example.android.sunshine.framework.db.entities.ForecastEntity

interface ForecastLocalDataSource {

    fun forecastByCoordinates(
        lat: Double,
        lon: Double
    ) : LiveData<ForecastEntity?>

    fun count(): Int

    fun insert(forecast: ForecastResponse)

    fun remove(forecast: ForecastResponse)


}