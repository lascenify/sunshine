package com.example.android.sunshine.core.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.sunshine.core.domain.City
import com.example.android.sunshine.framework.Constants.NetworkService.RATE_LIMITER_TYPE
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.core.domain.ForecastResponse
import com.example.android.sunshine.framework.NetworkBoundResource
import com.example.android.sunshine.framework.api.network.ApiForecastDataSource
import com.example.android.sunshine.framework.db.RoomForecastDataSource
import com.example.android.sunshine.framework.db.entities.CityEntity
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

    private val repoRateLimit = RateLimiter<String>(3, TimeUnit.HOURS)

    fun forecastByCoordinates(lat: Double, lon: Double, units: String): LiveData<Resource<ForecastEntity>>{
        return object : NetworkBoundResource<ForecastEntity, ForecastResponse>(appExecutors = appExecutors){
            override fun saveCallResult(item: ForecastResponse) = forecastLocalDataSource.insert(item)
            override fun shouldFetch(data: ForecastEntity?): Boolean = data == null || repoRateLimit.shouldFetch(RATE_LIMITER_TYPE)
            override fun loadFromDb(): LiveData<ForecastEntity?>  = forecastLocalDataSource.forecastByCoordinates(lat, lon)
            override fun createCall() = forecastRemoteDataSource.getForecastByCoordinates(lat, lon, units)
            override fun onFetchFailed() = repoRateLimit.reset(RATE_LIMITER_TYPE)
        }.asLiveData()
    }

    fun forecastByCity(){}

    fun removeAllLocalData() = forecastLocalDataSource.removeAll()


    fun removeForecast(forecast: ForecastResponse) = forecastLocalDataSource.remove(forecast)

    fun removeForecastByCoordinates(lat: Double, lon: Double) = forecastLocalDataSource.removeByCoordinates(lat, lon)

}
