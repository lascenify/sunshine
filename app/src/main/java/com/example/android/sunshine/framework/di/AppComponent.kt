package com.example.android.sunshine.framework.di

import android.app.Application
import android.content.Context
import com.example.android.sunshine.framework.di.module.DataBaseModule
import com.example.android.sunshine.framework.di.module.NetworkModule
import com.example.android.sunshine.presentation.ForecastListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppSubcomponents::class, /*ApplicationModule::class,*/ DataBaseModule::class, NetworkModule::class])
interface AppComponent {
    @Component.Factory
    interface Factory{
        fun create(@BindsInstance context: Context) : AppComponent
    }

    fun inject(forecastListFragment: ForecastListFragment)

    //fun inject(detailFragment: DetailFragment)
}