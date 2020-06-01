package com.example.android.sunshine.framework.di.module

import androidx.lifecycle.ViewModel
import com.example.android.sunshine.presentation.viewmodel.SearchViewModel
import dagger.Binds
import dagger.Module

@Module
abstract class SearchModule {
    @Binds
    abstract fun bindViewModel(viewModel: SearchViewModel): ViewModel
}