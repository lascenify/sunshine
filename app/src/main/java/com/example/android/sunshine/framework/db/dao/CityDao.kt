package com.example.android.sunshine.framework.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.android.sunshine.framework.db.entities.CityEntity

@Dao
interface CityDao{
    @Query("SELECT * FROM City WHERE cityName = :cityName")
    fun loadCityByName(cityName: String): LiveData<CityEntity?>

    @Query("SELECT * FROM City WHERE cityName IN (:names)")
    fun loadCitiesByName(names: List<String>): LiveData<List<CityEntity>>

    @Query ("SELECT count(*) from City")
    fun getCount():Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity (cityEntity: CityEntity)

    @Query("DELETE FROM City WHERE cityName=:name")
    fun deleteCityByName(name: String)
}