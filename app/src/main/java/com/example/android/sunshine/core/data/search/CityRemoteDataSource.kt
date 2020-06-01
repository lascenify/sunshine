package com.example.android.sunshine.core.data.search

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.search.SearchResponse

interface CityRemoteDataSource {
    fun getCityByName(
        cityName: String,
        language: String
    ): LiveData<Resource<SearchResponse>>
}