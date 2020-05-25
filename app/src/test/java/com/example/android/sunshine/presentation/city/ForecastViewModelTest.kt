package com.example.android.sunshine.presentation.city

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.Coordinates
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.util.mock
import com.example.android.sunshine.utilities.TestUtil
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ForecastViewModelTest{
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var interactors :Interactors

    @Mock
    private lateinit var forecastByCoordinates: ForecastByCoordinates


    private lateinit var forecastViewModel: ForecastViewModel

    private lateinit var params: ForecastByCoordinates.Params

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        interactors = Interactors(forecastByCoordinates)
        forecastViewModel = ForecastViewModel(interactors)
        params = ForecastByCoordinates.Params(0.0, 0.0, "metric")
    }

    /**
     * Test that the app never calls the forecast function, even if we set the params, if
     * there is no observer listening to the livedata
     */
    @Test
    fun testNull(){
        assertThat(forecastViewModel.forecast, notNullValue())
        verify(forecastByCoordinates, never()).invoke(params)
        forecastViewModel.setForecastParams(params)
        verify(forecastByCoordinates, never()).invoke(params)
    }

    @Test
    fun testCallForecast(){
        forecastViewModel.forecast.observeForever(mock())
        forecastViewModel.setForecastParams(params)
        verify(forecastByCoordinates).invoke(params)
    }

    @Test
    fun testSendResultToUI(){
        // When the forecast function is called, return a MutableLiveData<Resource<ForecastEntity>>
        val forecast = MutableLiveData<Resource<ForecastEntity>>()
        `when`(interactors.forecastByCoordinates.invoke(params)).thenReturn(forecast)

        // We create an observer and attach it to the livedata
        val observer = mock<Observer<Resource<ForecastEntity>>>()
        forecastViewModel.forecast.observeForever(observer)

        // Then, we update the params to make the viewmodel invoke the forecast function
        forecastViewModel.setForecastParams(params)

        // Verify that the observer onChanged function is not called at this point
        verify(observer, never()).onChanged(ArgumentMatchers.any())

        // We assign the right success result to the livedata
        val fooForecast = TestUtil.createFakeForecastEntity(Coordinates(params.lon, params.lat))
        val fooValue = Resource.success(fooForecast)
        forecast.value = fooValue

        // Verify that now the onChanged function is called with the right result
        verify(observer).onChanged(fooValue)
    }


    @Test
    fun testLoadForecast(){
        forecastViewModel.forecast.observeForever(mock())
        verifyNoMoreInteractions(interactors.forecastByCoordinates)

        forecastViewModel.setForecastParams(params)
        verify(interactors.forecastByCoordinates).invoke(params)
    }

    @Test
    fun testRetry(){
        /**
         * Even if we set the params or call the retry function,
         * there is no interactions with the forecast function
         * because no observer is attached to the livedata.
         */
        forecastViewModel.setForecastParams(params)
        verifyNoMoreInteractions(interactors.forecastByCoordinates)
        forecastViewModel.retry()
        verifyNoMoreInteractions(interactors.forecastByCoordinates)

        // Verify that there is an interaction when we attach the observer
        val observer = mock<Observer<Resource<ForecastEntity>>>()
        forecastViewModel.forecast.observeForever(observer)
        verify(interactors.forecastByCoordinates).invoke(params)

        reset(interactors.forecastByCoordinates)

        // If we retry, then there must be an interaction with the invoke function
        forecastViewModel.retry()
        verify(interactors.forecastByCoordinates).invoke(params)

        // But if we remove the observer, the interaction must not occur
        forecastViewModel.forecast.removeObserver(observer)
        forecastViewModel.retry()
        verifyNoMoreInteractions(interactors.forecastByCoordinates)
    }

    @Test
    fun testNullForecast(){
        val observer = mock<Observer<Resource<ForecastEntity>>>()
        forecastViewModel.setForecastParams(params)
        forecastViewModel.setForecastParams(null)
        forecastViewModel.forecast.observeForever(observer)
        verify(observer).onChanged(null)
    }

    @Test
    fun testDontRefreshOnSameData(){
        val newParams = ForecastByCoordinates.Params(5.0, 5.0, "imperial")

        val observer = mock<Observer<ForecastByCoordinates.Params>>()
        forecastViewModel.forecastParams.observeForever(observer)
        verifyNoMoreInteractions(observer)
        forecastViewModel.setForecastParams(params)
        verify(observer).onChanged(params)

        reset(observer)

        forecastViewModel.setForecastParams(params)
        verifyNoMoreInteractions(observer)

        forecastViewModel.setForecastParams(newParams)
        verify(observer).onChanged(newParams)
    }


    @Test
    fun testNoRetryWithoutParams(){
        forecastViewModel.retry()
        verifyNoMoreInteractions(interactors.forecastByCoordinates)
    }


    @Test
    fun testFetchNextDaysForecast(){
        val listOfForecastItems = mutableListOf<ForecastListItem>()
        val forecast = MutableLiveData<Resource<ForecastEntity>>()
        `when`(interactors.forecastByCoordinates.invoke(params)).thenReturn(forecast)
        forecastViewModel.forecast.observeForever(mock())
        forecastViewModel.setForecastParams(params)

        val forecastAt12PM = TestUtil.createFakeForecastEntityAt12PM()
        forecast.value = Resource.success(forecastAt12PM)
        var nextDaysForecast = forecastViewModel.forecastOfNextDays()
        nextDaysForecast.forEach { listOfForecastItems.addAll(it.forecastList) }
        assertThat(listOfForecastItems.size, `is`(32))
        assertThat(listOfForecastItems.first().dt_txt, `is`("2020-05-12 00:00:00"))
        assertThat(listOfForecastItems.last().dt_txt, `is`("2020-05-15 21:00:00"))

        val forecastAt11PM = TestUtil.createFakeForecastEntityAt11PM()
        forecast.value = Resource.success(forecastAt11PM)
        forecastViewModel.retry()
        nextDaysForecast = forecastViewModel.forecastOfNextDays()
        listOfForecastItems.clear()
        nextDaysForecast.forEach { listOfForecastItems.addAll(it.forecastList) }
        assertThat(listOfForecastItems.size, `is`(39))
        assertThat(listOfForecastItems.first().dt_txt, `is`("2020-05-12 00:00:00"))
        assertThat(listOfForecastItems.last().dt_txt, `is`("2020-05-15 18:00:00"))


        val forecastAt4PM = TestUtil.createFakeForecastEntityAt4PM()
        forecast.value = Resource.success(forecastAt4PM)
        forecastViewModel.retry()
        nextDaysForecast = forecastViewModel.forecastOfNextDays()
        listOfForecastItems.clear()
        nextDaysForecast.forEach { listOfForecastItems.addAll(it.forecastList) }
        assertThat(listOfForecastItems.size, `is`(37))
        assertThat(listOfForecastItems.first().dt_txt, `is`("2020-05-12 00:00:00"))
        assertThat(listOfForecastItems.last().dt_txt, `is`("2020-05-15 12:00:00"))
    }


    @Test
    fun testFetchNextHoursForecast(){
        val listOfForecastItems = mutableListOf<ForecastListItem>()
        val forecast = MutableLiveData<Resource<ForecastEntity>>()
        `when`(interactors.forecastByCoordinates.invoke(params)).thenReturn(forecast)
        forecastViewModel.forecast.observeForever(mock())
        forecastViewModel.setForecastParams(params)

        val forecastAt12PM = TestUtil.createFakeForecastEntityAt12PM()
        forecast.value = Resource.success(forecastAt12PM)
        listOfForecastItems.addAll(forecastViewModel.forecastOfNextHours())
        assertThat(listOfForecastItems.size, `is`(16))
        assertThat(listOfForecastItems.first().dt_txt, `is`("2020-05-11 00:00:00"))
        assertThat(listOfForecastItems.last().dt_txt, `is`("2020-05-12 21:00:00"))

        val forecastAt11PM = TestUtil.createFakeForecastEntityAt11PM()
        forecast.value = Resource.success(forecastAt11PM)
        forecastViewModel.retry()
        listOfForecastItems.clear()
        listOfForecastItems.addAll(forecastViewModel.forecastOfNextHours())
        assertThat(listOfForecastItems.first().dt_txt, `is`("2020-05-11 21:00:00"))
        assertThat(listOfForecastItems.last().dt_txt, `is`("2020-05-12 21:00:00"))


        val forecastAt4PM = TestUtil.createFakeForecastEntityAt4PM()
        forecast.value = Resource.success(forecastAt4PM)
        forecastViewModel.retry()
        listOfForecastItems.clear()
        listOfForecastItems.addAll(forecastViewModel.forecastOfNextHours())
        assertThat(listOfForecastItems.first().dt_txt, `is`("2020-05-11 15:00:00"))
        assertThat(listOfForecastItems.last().dt_txt, `is`("2020-05-12 21:00:00"))
    }
}