package com.example.android.sunshine.framework.di.component

import com.example.android.sunshine.framework.di.ForecastScope
import com.example.android.sunshine.framework.di.module.CityForecastModule
import com.example.android.sunshine.presentation.city.CityFragment
import com.example.android.sunshine.presentation.day.DayFragment
import dagger.Subcomponent

@ForecastScope
@Subcomponent(modules = [CityForecastModule::class])
interface CityForecastComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): CityForecastComponent
    }

    fun inject (fragment: CityFragment)

    fun inject (fragment: DayFragment)
}