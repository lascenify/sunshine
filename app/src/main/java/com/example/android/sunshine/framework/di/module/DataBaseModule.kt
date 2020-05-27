package com.example.android.sunshine.framework.di.module

import android.content.Context
import androidx.room.Room
import com.example.android.sunshine.framework.Constants
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.db.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class DataBaseModule () {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            Constants.Database.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()
        //).build()
    }

    @Singleton
    @Provides
    fun provideForecastDao(db: AppDatabase) = db.weatherForecastDao()

    @Singleton
    @Provides
    fun provideCityDao(db: AppDatabase) = db.cityDao()
}