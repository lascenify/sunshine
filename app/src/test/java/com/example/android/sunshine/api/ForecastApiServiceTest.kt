package com.example.android.sunshine.api

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.sunshine.framework.Constants
import com.example.android.sunshine.framework.api.forecast.ForecastApiService
import com.example.android.sunshine.framework.api.network.LiveDataCallAdapterFactory
import com.example.android.sunshine.framework.api.network.ForecastRequestInterceptor
import com.example.android.sunshine.utilities.getOrAwaitValue
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import org.hamcrest.CoreMatchers.notNullValue

import org.hamcrest.CoreMatchers.`is`

@RunWith(JUnit4::class)
class ForecastApiServiceTest {
    @Rule
    @JvmField
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var forecastApiService: ForecastApiService
    private lateinit var mockWebServer: MockWebServer
    private lateinit var moshi: Moshi
    private lateinit var client: OkHttpClient
    @Before
    fun createService(){
        mockWebServer = MockWebServer()
        client = OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .addInterceptor(ForecastRequestInterceptor())
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        forecastApiService = Retrofit.Builder()
            .baseUrl(Constants.NetworkService.BASE_FORECAST_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(client)
            .build()
            .create(ForecastApiService::class.java)
    }

    @After
    fun stopService(){
        mockWebServer.shutdown()
    }

    @Test
    fun getForecastByCity(){
        val cityName = "Cieza"
        val mockResponse = MockResponse()
        mockResponse.addHeader(Constants.NetworkService.PARAM_CITY, cityName)
        mockWebServer.enqueue(mockResponse)

        val forecast = forecastApiService.forecastByCity(cityName).getOrAwaitValue().data
        assertThat(forecast, notNullValue())
        assertThat(forecast?.list?.size, `is`(40))
        assertThat(forecast?.city?.name, `is`(cityName))
    }

    @Test
    fun getForecastByCoordinates(){
        val lat = 38.24
        val lon = -1.42
        val units = "metric"
        val mockResponse = MockResponse()
        mockResponse.addHeader(Constants.NetworkService.PARAM_LAT, lat)
        mockResponse.addHeader(Constants.NetworkService.PARAM_LON, lon)
        mockWebServer.enqueue(mockResponse)

        val forecast = forecastApiService.forecastByCoordinates(lat = lat, long = lon, units = units).getOrAwaitValue().data
        assertThat(forecast, notNullValue())
        assertThat(forecast?.city?.name, `is`("Cieza"))
        assertThat(forecast?.list?.size, `is`(40))

    }

    @Test
    fun getForecastByCityId(){
        val cityId = 2519425L
        val mockResponse = MockResponse()
        mockResponse.addHeader(Constants.NetworkService.PARAM_CITY_ID, cityId)
        mockWebServer.enqueue(mockResponse)

        val forecast = forecastApiService.forecastByCityId(cityId = cityId).getOrAwaitValue().data
        assertThat(forecast, notNullValue())
        assertThat(forecast?.city?.name, `is`("Cieza"))
        assertThat(forecast?.list?.size, `is`(40))
    }
}