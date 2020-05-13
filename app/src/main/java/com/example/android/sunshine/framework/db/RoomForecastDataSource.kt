package com.example.android.sunshine.framework.db

import androidx.lifecycle.LiveData
import com.example.android.sunshine.framework.db.dao.ForecastDao
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.framework.db.entities.asLiveData
import com.example.android.sunshine.core.data.ForecastLocalDataSource
import com.example.android.sunshine.core.domain.ForecastResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

open class RoomForecastDataSource @Inject constructor(private val forecastDao: ForecastDao) :
    ForecastLocalDataSource {

    //private val forecastDao = AppDatabase.getInstance(context).weatherForecastDao()


    override fun forecastByCoordinates(lat: Double, lon: Double): LiveData<ForecastEntity?> {
        val result1 = forecastDao.loadAll()
        val c = result1.value
        val other = forecastDao.loadForecastByCoordinates(lat, lon)
        val a = other.value
        val otheerrr = forecastDao.loadLastForecastt()

        val result = forecastDao.loadLastForecast()//loadForecastByCoordinates(lat, lon)//.asLiveData()
        val result22 =  result.value
        return result
    }

    override fun count(): Int = forecastDao.getCount()

    override fun insert(forecast: ForecastResponse) {
        forecastDao.deleteAllAndInsert(forecast = ForecastEntity(forecast))
    }

    override fun remove(forecast: ForecastResponse) {
        forecastDao.deleteForecast(forecast = ForecastEntity(forecast))
    }
}