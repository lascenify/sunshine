package com.example.android.sunshine.framework.di.component

import com.example.android.sunshine.framework.di.module.SearchModule
import com.example.android.sunshine.presentation.search.SearchFragment
import dagger.Subcomponent


@Subcomponent(modules = [SearchModule::class])
interface SearchComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): SearchComponent
    }

    fun inject (fragment: SearchFragment)

}