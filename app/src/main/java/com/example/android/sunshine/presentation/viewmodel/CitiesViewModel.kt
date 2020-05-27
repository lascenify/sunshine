package com.example.android.sunshine.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.db.entities.CityEntity
import com.example.android.sunshine.utilities.AbsentLiveData
import javax.inject.Inject

class CitiesViewModel
@Inject constructor(
    private val interactors: Interactors
): ViewModel(){

    private val _citiesParams: MutableLiveData<List<String>> = MutableLiveData()
    private val citiesParams: LiveData<List<String>>
    get() = _citiesParams

    val cities = citiesParams.switchMap { citiesNames: List<String> ->
        interactors.getCityByName.invoke(citiesNames)
    }

    val temperaturesOfCities = cities.switchMap {
        interactors.
    }

    fun setCitiesParams(cities: List<String>){
        if (_citiesParams.value != cities)
            _citiesParams.value = cities
    }
}