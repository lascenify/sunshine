package com.example.android.sunshine.framework.di.module

import androidx.lifecycle.ViewModelProvider
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel_Factory
import com.example.android.sunshine.presentation.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module


@Module
abstract class ViewModelModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory


}