package com.example.android.sunshine.core.data

import androidx.lifecycle.LiveData
import com.example.android.sunshine.framework.Constants.NetworkService.RATE_LIMITER_TYPE
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.core.domain.ForecastResponse
import com.example.android.sunshine.framework.NetworkBoundResource
import com.example.android.sunshine.framework.api.network.ApiForecastDataSource
import com.example.android.sunshine.framework.db.RoomForecastDataSource
import com.example.android.sunshine.utilities.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRepository @Inject constructor(
    private val appExecutors: AppExecutors = AppExecutors(),
    private val forecastRemoteDataSource: ApiForecastDataSource,
    private val forecastLocalDataSource: RoomForecastDataSource
){

    private val repoRateLimit = RateLimiter<String>(10, TimeUnit.MINUTES)

    /*fun testingForecast(lat: Double, lon: Double, units: String): LiveData<Resource<ForecastEntity>>
            = forecastRemoteDataSource.getForecastByCoordinates(lat, lon, units)
*/

    fun forecastByCoordinates(lat: Double, lon: Double, units: String): LiveData<Resource<ForecastEntity>>{
        return object : NetworkBoundResource<ForecastEntity, ForecastResponse>(appExecutors = appExecutors){
            override fun saveCallResult(item: ForecastResponse) = forecastLocalDataSource.insert(item)
            override fun shouldFetch(data: ForecastEntity?): Boolean = data == null
            //return data == null || data.isEmpty() || repoRateLimit.shouldFetch(owner)
            override fun loadFromDb(): LiveData<ForecastEntity?>  = forecastLocalDataSource.forecastByCoordinates(lat, lon)
            override fun createCall() = forecastRemoteDataSource.getForecastByCoordinates(lat, lon, units)
            override fun onFetchFailed() = repoRateLimit.reset(RATE_LIMITER_TYPE)
        }.asLiveData()
    }
/*
    fun forecastByCityName(city: String): LiveData<Resource<ForecastEntity>>{
        return object : NetworkBoundResource<ForecastEntity, ForecastResponse>(appExecutors = appExecutors){
            override fun saveCallResult(item: ForecastResponse) = forecastLocalDataSource.insert(item)
            override fun shouldFetch(data: ForecastEntity?): Boolean = data == null
            //return data == null || data.isEmpty() || repoRateLimit.shouldFetch(owner)
            override fun loadFromDb() = forecastLocalDataSource.lastForecast()
            override fun createCall() = forecastRemoteDataSource.getForecastByCity(city)
            override fun onFetchFailed() = repoRateLimit.reset(RATE_LIMITER_TYPE)
        }.asLiveData()
    }*/


    /*
    suspend fun getLastForecast() = forecastLocalDataSource.lastForecast()

    suspend fun insertForecast(forecast: ForecastResponse) = forecastLocalDataSource.insert(forecast)

    suspend fun removeForecast(forecast: ForecastResponse) = forecastLocalDataSource.remove(forecast)*/

}
