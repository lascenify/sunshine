package com.example.android.sunshine.framework.db

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.CityLocalDataSource
import com.example.android.sunshine.core.domain.City
import com.example.android.sunshine.framework.db.dao.CityDao
import com.example.android.sunshine.framework.db.entities.CityEntity
import javax.inject.Inject


class RoomCityDataSource
@Inject constructor(
    private val cityDao: CityDao
) : CityLocalDataSource {
    override fun cityByName(name: String): LiveData<CityEntity?> = cityDao.loadCityByName(name)

    override fun citiesByName(names: List<String>) = cityDao.loadCitiesByName(names)

    override fun insert(city: City) = cityDao.insertCity(CityEntity(city))

}