package com.example.android.sunshine.framework.di

import com.example.android.sunshine.framework.di.component.CitiesManagementComponent
import com.example.android.sunshine.framework.di.component.CityForecastComponent
import dagger.Module

@Module(
    subcomponents =
    [CityForecastComponent::class, CitiesManagementComponent::class]
)
class AppSubcomponents