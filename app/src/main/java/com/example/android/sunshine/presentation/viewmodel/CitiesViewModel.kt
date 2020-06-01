package com.example.android.sunshine.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.android.sunshine.framework.Interactors
import javax.inject.Inject

class CitiesViewModel
@Inject constructor(
    private val interactors: Interactors
): ViewModel(){

    val forecasts = interactors.lastForecasts.invoke()
}