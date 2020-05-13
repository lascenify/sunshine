package com.example.android.sunshine.framework

import android.app.Application
import com.example.android.sunshine.core.data.AppExecutors
import com.example.android.sunshine.framework.db.AppDatabase
import com.example.android.sunshine.framework.api.ApiService
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.core.interactors.*
import com.example.android.sunshine.framework.db.RoomForecastDataSource
import com.example.android.sunshine.framework.api.network.ApiForecastDataSource
import com.example.android.sunshine.framework.di.AppComponent
import com.example.android.sunshine.framework.di.DaggerAppComponent
import com.example.android.sunshine.presentation.viewmodel.SunshineViewModelFactory
import dagger.android.DaggerApplication
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class SunshineApplication : Application(){
    // Instance of the AppComponent that will be used by all the Activities in the project
    val appComponent: AppComponent by lazy {
        // Creates an instance of AppComponent using its Factory constructor
        // We pass the applicationContext that will be used as Context in the graph

        DaggerAppComponent.factory().create(applicationContext)
    }
    /*
    override fun onCreate() {
        super.onCreate()
        val forecastRepository =
            ForecastRepository(
                forecastLocalDataSource = RoomForecastDataSource(
                    AppDatabase.getInstance(
                        applicationContext
                    ).weatherForecastDao()
                ),
                forecastRemoteDataSource = ApiForecastDataSource(
                    provideService()
                ),
                appExecutors = AppExecutors()
            )

        SunshineViewModelFactory.inject(
            this,
            Interactors(
                forecastByCoordinates = ForecastByCoordinates(forecastRepository)
            )
        )

    }
*/
    /*private fun provideService() : ApiService =
        Retrofit.Builder().baseUrl(Constants.NetworkService.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
*/
}