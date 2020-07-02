package com.example.android.sunshine.repository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.android.sunshine.core.data.forecast.ForecastRepository
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.forecast.Coordinates
import com.example.android.sunshine.core.domain.forecast.ForecastResponse
import com.example.android.sunshine.framework.api.forecast.ApiForecastDataSource
import com.example.android.sunshine.framework.db.RoomForecastDataSource
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.util.ApiUtil
import com.example.android.sunshine.util.InstantAppExecutors
import com.example.android.sunshine.util.mock
import com.example.android.sunshine.utilities.TestUtil
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@RunWith(JUnit4::class)
class ForecastRepositoryTest {

    @Mock
    private lateinit var localDataSource: RoomForecastDataSource
    @Mock
    private lateinit var remoteDataSource: ApiForecastDataSource

    private lateinit var repository: ForecastRepository

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private lateinit var fakeForecast: ForecastEntity
    private lateinit var fakeForecastResponse: ForecastResponse

    private lateinit var dbData :MutableLiveData<ForecastEntity>

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        repository =
            ForecastRepository(
                InstantAppExecutors(),
                remoteDataSource,
                localDataSource
            )
        val coordinates =
            Coordinates(
                -1.42,
                38.24
            )
        lat = coordinates.latitude!!
        lon = coordinates.longitude!!
        fakeForecastResponse = TestUtil.createFakeForecastResponse(coordinates)
        fakeForecast = ForecastEntity(fakeForecastResponse)
        dbData = MutableLiveData<ForecastEntity>()

    }
    /**
     * Asserts that when the forecast is in the local db, the repository extracts it from there
     */
    @Test
    fun goToNetworkIfRateLimiterShouldFetch(){

        val forecastLiveData: MutableLiveData<ForecastEntity> = MutableLiveData()
        forecastLiveData.postValue(fakeForecast)

        val mockedObserver = mock<Observer<Resource<ForecastEntity>>>()

        `when`(localDataSource.forecastByCoordinates(lat, lon)).thenReturn(forecastLiveData)
        val call = ApiUtil.successCall(fakeForecastResponse)
        `when`(remoteDataSource.getForecastByCoordinates(lat, lon, "metric")).thenReturn(call)

        repository.forecastByCoordinates(lat, lon, "metric").observeForever(mockedObserver)

        verify(remoteDataSource).getForecastByCoordinates(lat, lon, "metric")

        verify(mockedObserver).onChanged(Resource.success(fakeForecast))

    }

    /**
     * Asserts that, if the forecast in local does not match the query or there is no forecast
     * in the local database, a network call is made
     */
    @Test
    fun goToNetworkIfLocalDataEmpty(){

        dbData.value = null
        // When calling the db, return an empty livedata
        `when`(localDataSource.forecastByCoordinates(lat, lon)).thenReturn(dbData)

        // When calling the api, return a successful response
        val call = ApiUtil.successCall(fakeForecastResponse)
        `when`(remoteDataSource.getForecastByCoordinates(lat, lon, "metric")).thenReturn(call)

        val mockedObserver = mock<Observer<Resource<ForecastEntity>>>()

        // Call
        repository.forecastByCoordinates(lat, lon, "metric").observeForever(mockedObserver)

        verify(remoteDataSource).getForecastByCoordinates(lat, lon, "metric")
    }

    /**
     * Asserts that, if there is local data available and this local data is not very old (check RateLimiter),
     * we should not call the API
     */
    @Test
    fun dontGoToNetwork(){
        dbData.postValue(fakeForecast)

        val mockedObserver = mock<Observer<Resource<ForecastEntity>>>()

        `when`(localDataSource.forecastByCoordinates(lat, lon)).thenReturn(dbData)
        val call = ApiUtil.successCall(fakeForecastResponse)
        `when`(remoteDataSource.getForecastByCoordinates(lat, lon, "metric")).thenReturn(call)

        repository.forecastByCoordinates(lat, lon, "metric").observeForever(mockedObserver)

        verify(remoteDataSource).getForecastByCoordinates(lat, lon, "metric")

        repository.forecastByCoordinates(lat, lon, "metric")
        verifyNoMoreInteractions(remoteDataSource)
    }



}