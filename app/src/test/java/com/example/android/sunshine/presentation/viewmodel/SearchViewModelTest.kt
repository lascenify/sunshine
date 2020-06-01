package com.example.android.sunshine.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.search.SearchResponse
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.core.interactors.LastForecasts
import com.example.android.sunshine.core.interactors.SearchCity
import com.example.android.sunshine.framework.Interactors
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.util.mock
import com.example.android.sunshine.utilities.TestUtil
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class SearchViewModelTest{
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var interactors : Interactors

    @Mock
    private lateinit var forecastByCoordinates: ForecastByCoordinates

    @Mock
    private lateinit var lastForecasts: LastForecasts

    @Mock
    private lateinit var searchCity: SearchCity


    private lateinit var searchViewModel: SearchViewModel

    private lateinit var params: SearchCity.Params

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        interactors = Interactors(forecastByCoordinates, lastForecasts, searchCity)
        searchViewModel =
            SearchViewModel(
                interactors
            )
        params = SearchCity.Params("mur")
    }

    /**
     * Test that the app never calls the forecast function, even if we set the params, if
     * there is no observer listening to the livedata
     */
    @Test
    fun testNull(){
        MatcherAssert.assertThat(searchViewModel.cities, CoreMatchers.notNullValue())
        Mockito.verify(searchCity, Mockito.never()).invoke(params)
        searchViewModel.setSearchParams(params)
        Mockito.verify(searchCity, Mockito.never()).invoke(params)
    }

    @Test
    fun testCallForecast(){
        searchViewModel.cities.observeForever(mock())
        searchViewModel.setSearchParams(params)
        Mockito.verify(searchCity).invoke(params)
    }


    @ExperimentalStdlibApi
    @Test
    fun testSendResultToUI(){
        // When the forecast function is called, return a MutableLiveData<Resource<ForecastEntity>>
        val result = MutableLiveData<Resource<SearchResponse>>()
        `when`(interactors.searchCity.invoke(params)).thenReturn(result)

        // We create an observer and attach it to the livedata
        val observer = mock<Observer<Resource<SearchResponse>>>()
        searchViewModel.cities.observeForever(observer)

        // Then, we update the params to make the viewmodel invoke the forecast function
        searchViewModel.setSearchParams(params)

        // Verify that the observer onChanged function is not called at this point
        verify(observer, never()).onChanged(ArgumentMatchers.any())

        // We assign the right success result to the livedata
        val fooCity = TestUtil.createFakeCityResult()
        val fooValue = Resource.success(fooCity)
        result.value = fooValue

        // Verify that now the onChanged function is called with the right result
        verify(observer).onChanged(fooValue)
    }


}