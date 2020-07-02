package com.example.android.sunshine.framework.di.component

import com.example.android.sunshine.framework.di.module.CitiesManagementModule
import com.example.android.sunshine.presentation.settings.CitiesManagementFragment
import dagger.Subcomponent

@Subcomponent(modules = [CitiesManagementModule::class])
interface CitiesManagementComponent {
    @Subcomponent.Factory
    interface Factory{
        fun create(): CitiesManagementComponent
    }

    fun inject (fragment: CitiesManagementFragment)

}