package com.example.android.sunshine.repository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.Coordinates
import com.example.android.sunshine.core.domain.ForecastResponse
import com.example.android.sunshine.framework.api.network.ApiForecastDataSource
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
    private lateinit var coordinates: Coordinates
    private lateinit var fakeForecast: ForecastEntity
    private lateinit var fakeForecastResponse: ForecastResponse

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        repository = ForecastRepository(InstantAppExecutors(), remoteDataSource, localDataSource)
        coordinates = Coordinates(-1.42, 38.24)
        fakeForecastResponse = TestUtil.createFakeForecastResponse(coordinates)
        fakeForecast = ForecastEntity(fakeForecastResponse)
    }
    /**
     * Asserts that when the forecast is in the local db, the repository extracts it from there
     */
    @Test
    fun dontGoToNetwork(){
        val lat = coordinates.latitude!!
        val lon = coordinates.longitude!!
        val forecastLiveData: MutableLiveData<ForecastEntity> = MutableLiveData()
        forecastLiveData.postValue(fakeForecast)

        val mockedObserver = mock<Observer<Resource<ForecastEntity>>>()

        `when`(localDataSource.forecastByCoordinates(lat, lon)).thenReturn(forecastLiveData)
        val call = ApiUtil.successCall(fakeForecastResponse)
        `when`(remoteDataSource.getForecastByCoordinates(lat, lon, "metric")).thenReturn(call)

        repository.forecastByCoordinates(lat, lon, "metric").observeForever(mockedObserver)

        // Verify that the repo DOES NOT make a network call
        verify(remoteDataSource, never()).getForecastByCoordinates(lat, lon, "metric")

        verify(mockedObserver).onChanged(Resource.success(fakeForecast))

    }


    /**
     * Asserts that, if the forecast in local does not match the query or there is no forecast
     * in the local database, a network call is made
    */
    @Test
    fun goToNetwork(){
        val lat = coordinates.latitude!!
        val lon = coordinates.longitude!!
        // There is no data on the local database
        //localDataSource.removeAll()

        val dbData = MutableLiveData<ForecastEntity>()
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

}