package com.example.android.sunshine.framework.di.module

import androidx.lifecycle.ViewModel
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import dagger.Binds
import dagger.Module


@Module
abstract class CitiesManagementModule {

    @Binds
    abstract fun bindViewModel(viewModel: CitiesViewModel): ViewModel

}