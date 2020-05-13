package com.example.android.sunshine.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.SunshineApplication

object SunshineViewModelFactory : ViewModelProvider.Factory{

    lateinit var application: Application

    lateinit var dependencies: Interactors

    fun inject(application: Application, dependencies: Interactors) {
        SunshineViewModelFactory.application = application
        SunshineViewModelFactory.dependencies = dependencies
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(SunshineApplication::class.java, Interactors::class.java)
            .newInstance(
                application,
                dependencies
            )
    }

}