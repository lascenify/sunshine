package com.example.android.sunshine.presentation

import com.example.android.sunshine.core.data.Status

open class BaseViewState (val baseStatus:Status, val baseError: String?){
    fun isLoading() = baseStatus == Status.LOADING
    fun getErrorMessage() = baseError
    fun shouldShowErrorMessage() = !baseError.isNullOrEmpty()
}