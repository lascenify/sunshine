package com.example.android.sunshine.framework.di

import com.example.android.sunshine.framework.di.module.DataBaseModule
import com.example.android.sunshine.framework.di.module.NetworkModule
import dagger.Module

@Module(
    subcomponents =
    [//ApplicationModule::class, NetworkModule::class, DataBaseModule::class]
]
)
class AppSubcomponents