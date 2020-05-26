package com.example.android.sunshine.framework.di.module

import androidx.lifecycle.ViewModel
import com.example.android.sunshine.framework.di.ForecastScope
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class CityForecastModule {

    @Binds
    @ForecastScope
    abstract fun bindViewModel(viewModel: ForecastViewModel): ViewModel

}