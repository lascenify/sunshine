package com.example.android.sunshine.framework

import android.app.Application
import com.example.android.sunshine.framework.di.component.AppComponent
import com.example.android.sunshine.framework.di.component.DaggerAppComponent

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