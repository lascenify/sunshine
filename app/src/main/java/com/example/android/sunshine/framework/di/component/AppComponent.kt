package com.example.android.sunshine.framework.di.component

import android.content.Context
import com.example.android.sunshine.framework.di.AppSubcomponents
import com.example.android.sunshine.framework.di.module.ApplicationModule
import com.example.android.sunshine.framework.di.module.DataBaseModule
import com.example.android.sunshine.framework.di.module.NetworkModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class, NetworkModule::class, DataBaseModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context) : AppComponent
    }

    fun cityForecastComp(): CityForecastComponent.Factory

    fun citiesSettingsComp(): CitiesManagementComponent.Factory

    /*fun inject(cityMainForecastFragment: CityMainForecastFragment)

    fun inject(temperatureChartFragment: TemperatureChartFragment)*/

    //fun inject(detailFragment: DetailFragment)
}