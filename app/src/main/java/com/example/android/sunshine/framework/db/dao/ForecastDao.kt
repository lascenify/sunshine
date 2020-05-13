package com.example.android.sunshine.framework.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.sunshine.framework.db.entities.ForecastEntity

@Dao
interface ForecastDao {

    /** SELECT QUERIES **/
    @Query("SELECT * FROM Forecast LIMIT 1")
    fun loadLastForecast(): LiveData<ForecastEntity?>


    @Query("SELECT * FROM Forecast LIMIT 1")
    fun loadLastForecastt(): ForecastEntity?

    @Query("SELECT * FROM Forecast")
    fun loadAll(): LiveData<List<ForecastEntity>?>

    @Query ("SELECT * FROM Forecast WHERE id = :id")
    fun loadForecastById(id:Int):ForecastEntity

    @Query ("SELECT * FROM Forecast ORDER BY abs(lat-:lat) AND abs(lon-:lon) LIMIT 1")
    fun loadForecastByCoordinates(lat:Double, lon:Double): LiveData<ForecastEntity?>

    @Query ("SELECT count(*) from Forecast")
    fun getCount():Int



    /** INSERT QUERIES **/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertForecast(forecast: ForecastEntity)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun insertAll (forecasts : List<ForecastEntity>)

    @Transaction
    fun deleteAllAndInsert(forecast: ForecastEntity){
        deleteAll()
        insertForecast(forecast)
    }

    /** UPDATE QUERIES **/
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateForecast(forecast: ForecastEntity)


    /** DELETE QUERIES **/
    @Delete
    fun deleteForecast(forecast: ForecastEntity)

    @Query ("DELETE FROM Forecast")
    fun deleteAll()


}