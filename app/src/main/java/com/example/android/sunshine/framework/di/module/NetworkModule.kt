package com.example.android.sunshine.framework.di.module

import android.os.Environment
import com.example.android.sunshine.framework.Constants
import com.example.android.sunshine.framework.api.ApiService
import com.example.android.sunshine.framework.api.network.LiveDataCallAdapterFactory
import com.example.android.sunshine.framework.api.network.RequestInterceptor
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule{
    @Provides
    @Singleton
    fun provideService(moshi: Moshi, client: OkHttpClient) : ApiService =
        Retrofit.Builder()
            .baseUrl(Constants.NetworkService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(ApiService::class.java)


    @Provides
    @Singleton
    fun provideNonCachedOkHttpClient(): OkHttpClient{
        return OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(RequestInterceptor())
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