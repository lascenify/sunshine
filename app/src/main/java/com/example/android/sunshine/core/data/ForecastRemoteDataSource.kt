package com.example.android.sunshine.core.data

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.domain.ForecastResponse
import io.reactivex.Single

interface ForecastRemoteDataSource{

    fun getForecastByCoordinates(
        lat : Double,
        lon : Double,
        units : String
    ): LiveData<Resource<ForecastResponse>>


    /*fun getForecastByCoordinates (lat:Double, lon:Double, units:String): Observable<ForecastResponse> =
        apiService.forecastByCoordinates(
            lat = lat,
            long = lon,
            units = units)
     */

}