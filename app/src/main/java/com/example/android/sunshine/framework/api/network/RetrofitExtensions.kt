package com.example.android.sunshine.framework.api.network

import com.example.android.sunshine.core.data.Resource
import retrofit2.Response

/**
 * Extension function that maps [Response] to [Resource]
 */
fun <ResultType> Response<ResultType>.toResource(): Resource<ResultType> {
    val error = errorBody()?.toString() ?: message()
    return when{
        isSuccessful -> {
            val body = body()
            when {
                body != null -> Resource.success(body)
                else -> Resource.error(error, null)
            }
        }
        else -> Resource.error(error, null)
    }
}