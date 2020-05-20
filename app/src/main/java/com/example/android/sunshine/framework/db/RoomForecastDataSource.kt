package com.example.android.sunshine.framework.db

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.ForecastLocalDataSource
import com.example.android.sunshine.core.domain.ForecastResponse
import com.example.android.sunshine.framework.db.dao.ForecastDao
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import javax.inject.Inject

open class RoomForecastDataSource @Inject constructor(private val forecastDao: ForecastDao) :
    ForecastLocalDataSource {

    //private val forecastDao = AppDatabase.getInstance(context).weatherForecastDao()


    override fun forecastByCoordinates(lat: Double, lon: Double): LiveData<ForecastEntity?> =
        forecastDao.loadLastForecast()
        /*
        val result1 = forecastDao.loadAll()
        val c = result1.value
        val other = forecastDao.loadForecastByCoordinates(lat, lon)
        val a = other.value
        val otheerrr = forecastDao.loadLastForecastt()

        val result = *///loadForecastByCoordinates(lat, lon)//.asLiveData()
        /*return result
    }*/

    override fun count(): Int = forecastDao.getCount()

    override fun insert(forecast: ForecastResponse) {
        forecastDao.deleteAllAndInsert(forecast = ForecastEntity(forecast))
    }

    override fun remove(forecast: ForecastResponse) {
        val coordinates = forecast.city?.coordinates!!
        forecastDao.deleteForecastByCoordinates(
            lat = coordinates.latitude!!,
            lon = coordinates.longitude!!)
    }

    override fun removeAll() = forecastDao.deleteAll()


    override fun removeByCoordinates(lat: Double, lon: Double) = forecastDao.deleteForecastByCoordinates(lat, lon)
}