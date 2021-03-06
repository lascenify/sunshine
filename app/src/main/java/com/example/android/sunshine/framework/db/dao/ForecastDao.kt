package com.example.android.sunshine.framework.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.sunshine.framework.db.entities.ForecastEntity

@Dao
interface ForecastDao {

    /** SELECT QUERIES **/
    @Query("SELECT * FROM Forecast LIMIT 1")
    fun loadLastForecast(): LiveData<ForecastEntity?>

    @Query ("SELECT * FROM Forecast WHERE lat =:lat AND lon =:lon LIMIT 1")
    fun loadForecastByCoordinates(lat:Double, lon:Double): LiveData<ForecastEntity?>

    @Query("SELECT id, list, cityName, timezone FROM Forecast")
    fun loadLastForecasts(): LiveData<List<ForecastEntity>?>

    @Query("SELECT * FROM Forecast WHERE cityName =:name")
    fun loadForecastByCityName(name: String): LiveData<ForecastEntity>

    @Query("SELECT * FROM Forecast WHERE cityId =:cityId")
    fun loadForecastByCityId(cityId: Long): LiveData<ForecastEntity?>

    @Query ("SELECT count(*) from Forecast")
    fun getCount():Int

    /** INSERT QUERIES **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecast: ForecastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllForecasts(list: List<ForecastEntity>)

    @Transaction
    fun deleteAllAndInsert(forecast: ForecastEntity){
        deleteAll()
        insertForecast(forecast)
    }

    /** UPDATE QUERIES
     * In this case, there is no sense of updating a forecast.
     * If we want to update the data, a network call will take place.
     * Then we have to delete the forecast and introduce the new one
     *
     *
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateForecast(forecast: ForecastEntity)
     */

    /** DELETE QUERIES **/
    @Query("DELETE FROM Forecast WHERE lat=:lat AND lon=:lon")
    fun deleteForecastByCoordinates(lat: Double, lon: Double)

    @Query ("DELETE FROM Forecast")
    fun deleteAll()

    @Query("DELETE FROM Forecast WHERE cityId=:cityId")
    fun deleteForecastByCityId(cityId: Long)

}