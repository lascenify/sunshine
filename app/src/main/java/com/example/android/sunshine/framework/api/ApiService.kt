package com.example.android.sunshine.framework.api

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.ForecastResponse
import com.example.android.sunshine.framework.Constants.NetworkService.FORECAST
import com.example.android.sunshine.framework.Constants.NetworkService.PARAM_CITY
import com.example.android.sunshine.framework.Constants.NetworkService.PARAM_CITY_ID
import com.example.android.sunshine.framework.Constants.NetworkService.PARAM_LAT
import com.example.android.sunshine.framework.Constants.NetworkService.PARAM_LON
import com.example.android.sunshine.framework.Constants.NetworkService.PARAM_UNITS
import io.reactivex.Single


import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

interface ApiService{

    @GET(FORECAST)
    fun forecastByCoordinates(
        @Query(PARAM_LAT)
        lat:Double,
        @Query(PARAM_LON)
        long: Double,
        @Query(PARAM_UNITS)
        units:String
    ): LiveData<Resource<ForecastResponse>>


    @GET(FORECAST)
    fun forecastByCity(
        @Query(PARAM_CITY)
        city: String
    ): LiveData<Resource<ForecastResponse>>

    @GET(FORECAST)
    fun forecastByCityId(
        @Query(PARAM_CITY_ID)
        cityId: Long
    ): LiveData<Resource<ForecastResponse>>
}