package com.example.android.sunshine.framework.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.android.sunshine.core.data.AppExecutors
import com.example.android.sunshine.core.data.forecast.ForecastRepository
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.api.forecast.ApiForecastDataSource
import com.example.android.sunshine.framework.db.RoomForecastDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule (private val application: SunshineApplication) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context = application

    @Provides
    @Singleton
    fun provideRepository(
        localDataSource: RoomForecastDataSource,
        remoteDataSource: ApiForecastDataSource,
        appExecutors: AppExecutors): ForecastRepository =
        ForecastRepository(
            appExecutors,
            remoteDataSource,
            localDataSource
        )


    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

}