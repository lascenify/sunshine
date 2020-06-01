package com.example.android.sunshine.framework.api.network

import com.example.android.sunshine.framework.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastRequestInterceptor @Inject constructor(): Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url
            .newBuilder()
            .addQueryParameter(Constants.NetworkService.FORECAST_API_KEY_QUERY, Constants.NetworkService.FORECAST_API_KEY_VALUE)
            .build()
        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }

}