package com.example.android.sunshine.framework.di

import com.example.android.sunshine.framework.di.component.CityForecastComponent
import dagger.Module

@Module(
    subcomponents =
    [CityForecastComponent::class]
)
class AppSubcomponents