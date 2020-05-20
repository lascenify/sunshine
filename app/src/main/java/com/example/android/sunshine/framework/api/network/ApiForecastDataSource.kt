package com.example.android.sunshine.framework.api.network

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.ForecastRemoteDataSource
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.ForecastResponse
import com.example.android.sunshine.framework.api.ApiService
import io.reactivex.Single
import javax.inject.Inject

open class ApiForecastDataSource
@Inject constructor(private val api : ApiService) : ForecastRemoteDataSource {
    override fun getForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String
    ): LiveData<Resource<ForecastResponse>>  = api.forecastByCoordinates(lat = lat, long = lon, units = units)

/*
    override suspend fun getForecastByCoordinates(
        lat: Double,
        lon: Double,
        units: String
    ) = api.forecastByCoordinates(lat = lat, long = lon, units = units)

    override suspend fun getForecastByCity(city: String) = api.forecastByCity(city)
*/

}