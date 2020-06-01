package com.example.android.sunshine.core.data.forecast

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.forecast.ForecastResponse

interface ForecastRemoteDataSource{

    fun getForecastByCoordinates(
        lat : Double,
        lon : Double,
        units : String
    ): LiveData<Resource<ForecastResponse>>


    fun getForecastByCityName(
        cityName: String
    ): LiveData<Resource<ForecastResponse>>

    /*fun getForecastByCoordinates (lat:Double, lon:Double, units:String): Observable<ForecastResponse> =
        apiService.forecastByCoordinates(
            lat = lat,
            long = lon,
            units = units)
     */

}