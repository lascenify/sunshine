package com.example.android.sunshine.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.android.sunshine.core.data.Resource

object ApiUtil {
    fun <T:Any> successCall(data: T) = createCall(Resource.success(data))

    fun <T:Any> createCall(resource: Resource<T>) = MutableLiveData<Resource<T>>().apply {
        value = resource
    } as LiveData<Resource<T>>
}