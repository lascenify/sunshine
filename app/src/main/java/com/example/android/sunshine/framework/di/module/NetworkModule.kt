package com.example.android.sunshine.framework.di.module

import com.example.android.sunshine.framework.Constants
import com.example.android.sunshine.framework.api.forecast.ForecastApiService
import com.example.android.sunshine.framework.api.network.CityRequestInterceptor
import com.example.android.sunshine.framework.api.network.LiveDataCallAdapterFactory
import com.example.android.sunshine.framework.api.network.ForecastRequestInterceptor
import com.example.android.sunshine.framework.api.search.CityApiService
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule{
    @Provides
    @Singleton
    fun provideForecastService(moshi: Moshi, client: OkHttpClient) : ForecastApiService {
        val customClient = client.newBuilder()
            .addInterceptor(ForecastRequestInterceptor())
            .build()
        return Retrofit.Builder()
            .baseUrl(Constants.NetworkService.BASE_FORECAST_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(customClient)
            .build()
            .create(ForecastApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideCityService(moshi: Moshi, client: OkHttpClient): CityApiService {
        /*val customClient = client.newBuilder()
            .addInterceptor(CityRequestInterceptor())
            .build()*/
        return Retrofit.Builder()
            .baseUrl(Constants.NetworkService.BASE_CITY_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .client(client)
            .build()
            .create(CityApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient{
        return OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi{
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }


}