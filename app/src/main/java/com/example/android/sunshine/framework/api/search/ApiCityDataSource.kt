package com.example.android.sunshine.framework.api.search

import androidx.lifecycle.LiveData
import com.algolia.search.saas.places.PlacesClient
import com.example.android.sunshine.core.data.search.CityRemoteDataSource
import com.example.android.sunshine.core.domain.search.SearchResponse
import javax.inject.Inject

class ApiCityDataSource @Inject constructor(
    private val apiService: CityApiService
) : CityRemoteDataSource{

    override fun getCityByName(cityName: String, language: String) =
        apiService.searchCity(cityName, language)

}