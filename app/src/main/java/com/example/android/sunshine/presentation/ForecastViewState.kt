package com.example.android.sunshine.presentation

import com.example.android.sunshine.core.data.Status
import com.example.android.sunshine.framework.db.entities.ForecastEntity

class ForecastViewState (
    val status: Status,
    val error: String? = null,
    val data: ForecastEntity? = null
): BaseViewState(status, error){
    fun getForecast() = data
}