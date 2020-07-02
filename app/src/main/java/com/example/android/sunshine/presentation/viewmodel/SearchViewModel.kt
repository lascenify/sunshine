package com.example.android.sunshine.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.search.SearchResponse
import com.example.android.sunshine.core.domain.search.SearchResult
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.core.interactors.SearchCity
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.utilities.AbsentLiveData
import javax.inject.Inject

class SearchViewModel @Inject internal constructor(
    val interactors: Interactors
): ViewModel(){
    private val _searchParams: MutableLiveData<SearchCity.Params> = MutableLiveData()

    val cities: LiveData<Resource<SearchResponse>> = _searchParams.switchMap { params ->
        if (params == null){
            AbsentLiveData.create()
        } else{
            interactors.searchCity.invoke(params)
        }
    }

    fun setSearchParams(params: SearchCity.Params){
        if (_searchParams.value != params)
            _searchParams.value = params
    }

    fun forecastCity(result: SearchResult): LiveData<Resource<ForecastEntity>>{
        return interactors.forecastByCoordinates.invoke(ForecastByCoordinates.Params(
            result.coordinates.latitude!!,
            result.coordinates.longitude!!,
            "metric"))
    }
}