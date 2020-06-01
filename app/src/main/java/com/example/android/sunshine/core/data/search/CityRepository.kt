package com.example.android.sunshine.core.data.search

import androidx.lifecycle.LiveData
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.search.SearchResponse
import com.example.android.sunshine.framework.NetworkBoundResource
import com.example.android.sunshine.framework.RemoteBoundResource
import com.example.android.sunshine.framework.api.search.ApiCityDataSource
import com.example.android.sunshine.framework.db.entities.CitySearchedEntity
import com.example.android.sunshine.utilities.RateLimiter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class CityRepository @Inject constructor(
    private val remoteDataSource: ApiCityDataSource
){
    private val rateLimiter = RateLimiter<String>(20, TimeUnit.SECONDS)

    fun loadCityByName(cityName: String): LiveData<Resource<SearchResponse>>{
        return object : RemoteBoundResource <SearchResponse>(){
            override fun createCall(): LiveData<Resource<SearchResponse>> =
                remoteDataSource.getCityByName(cityName, "es")
        }.asLiveData()
    }
}