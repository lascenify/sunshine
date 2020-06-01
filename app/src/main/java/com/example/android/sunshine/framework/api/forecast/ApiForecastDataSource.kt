package com.example.android.sunshine.framework.api.forecast

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.forecast.ForecastRemoteDataSource
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.forecast.ForecastResponse
import javax.inject.Inject

open class ApiForecastDataSource
@Inject constructor(private val forecastApi : ForecastApiService) :
    ForecastRemoteDataSource {
    override fun getForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String
    ): LiveData<Resource<ForecastResponse>>  =
        forecastApi.forecastByCoordinates(lat = lat, long = lon, units = units)

    override fun getForecastByCityName(cityName: String): LiveData<Resource<ForecastResponse>> =
        forecastApi.forecastByCity(cityName)

}