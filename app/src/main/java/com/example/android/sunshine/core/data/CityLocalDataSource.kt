package com.example.android.sunshine.core.data

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.domain.City
import com.example.android.sunshine.framework.db.entities.CityEntity


interface CityLocalDataSource {

    fun cityByName(
        name: String
    ): LiveData<CityEntity?>

    fun citiesByName(
        names: List<String>
    ): LiveData<List<CityEntity>>

    fun insert(city: City)
}