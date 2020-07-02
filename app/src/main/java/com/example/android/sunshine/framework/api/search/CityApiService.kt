package com.example.android.sunshine.framework.api.search

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.search.SearchResponse
import com.example.android.sunshine.framework.Constants.NetworkService.GET_CITY
import com.example.android.sunshine.framework.Constants.NetworkService.PARAM_CITY_SEARCH
import com.example.android.sunshine.framework.Constants.NetworkService.PARAM_LANG
import com.example.android.sunshine.framework.Constants.NetworkService.PARAM_LAT
import retrofit2.http.GET
import retrofit2.http.Query

interface CityApiService {
    @GET(GET_CITY)
    fun searchCity(
        @Query(PARAM_CITY_SEARCH)
        query: String,
        @Query(PARAM_LANG)
        lang: String
    ): LiveData<Resource<SearchResponse>>
}