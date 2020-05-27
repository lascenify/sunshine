package com.example.android.sunshine.core.data

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.domain.City
import com.example.android.sunshine.framework.db.RoomCityDataSource
import com.example.android.sunshine.framework.db.entities.CityEntity
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CityRepository @Inject constructor(
    private val appExecutors: AppExecutors = AppExecutors(),
    private val cityLocalDataSource: RoomCityDataSource
){
    fun cityByName(cityName: String): LiveData<CityEntity?>  = cityLocalDataSource.cityByName(cityName)

    fun citiesByName(nameOfCities: List<String>): LiveData<List<CityEntity>> = cityLocalDataSource.citiesByName(nameOfCities)

    fun insert(city: City) = appExecutors.diskIO().execute {
        cityLocalDataSource.insert(city)
    }

}
