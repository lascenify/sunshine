package com.example.android.sunshine.framework.di.component

import com.example.android.sunshine.framework.di.ForecastScope
import com.example.android.sunshine.framework.di.module.CityForecastModule
import com.example.android.sunshine.presentation.cityMainForecast.CityMainForecastFragment
import com.example.android.sunshine.presentation.cityMainForecast.TemperatureChartFragment
import dagger.Component
import dagger.Subcomponent

@ForecastScope
@Subcomponent(modules = [CityForecastModule::class])
interface CityForecastComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): CityForecastComponent
    }
    fun inject (fragment: TemperatureChartFragment)

    fun inject (fragment: CityMainForecastFragment)
}