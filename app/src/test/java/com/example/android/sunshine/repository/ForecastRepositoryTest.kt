package com.example.android.sunshine.repository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.android.sunshine.core.data.AppExecutors
import com.example.android.sunshine.core.data.ForecastRepository
import com.example.android.sunshine.core.data.Resource
import com.example.android.sunshine.core.domain.ForecastResponse
import com.example.android.sunshine.framework.api.ApiService
import com.example.android.sunshine.framework.api.network.ApiForecastDataSource
import com.example.android.sunshine.framework.db.RoomForecastDataSource
import com.example.android.sunshine.framework.db.dao.ForecastDao
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
import org.mockito.ArgumentCaptor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import rx.Single

@RunWith(JUnit4::class)
class ForecastRepositoryTest {

//    private val forecastDao = mock(ForecastDao::class.java)
    @Mock
    private lateinit var localDataSource: RoomForecastDataSource
//    private val apiService = mock(ApiService::class.java)
    @Mock
    private lateinit var remoteDataSource: ApiForecastDataSource

    private lateinit var repository: ForecastRepository

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        repository = ForecastRepository(InstantAppExecutors(), remoteDataSource, localDataSource)
    }
    /**
     * Asserts that when the forecast is in the local db, the repository extracts it from there
     */
    @Test
    fun dontGoToNetwork(){
        val lat = 38.24
        val lon = -1.42
        val fakeForecastResponse = TestUtil.createFakeForecastResponse()
        val fakeForecastEntity = ForecastEntity(fakeForecastResponse)
        val forecastLiveData: MutableLiveData<ForecastEntity> = MutableLiveData()
        forecastLiveData.postValue(fakeForecastEntity)

        val mockedObserver = mock<Observer<Resource<ForecastEntity>>>()

        `when`(localDataSource.forecastByCoordinates(lat, lon)).thenReturn(forecastLiveData)
        val call = ApiUtil.successCall(fakeForecastResponse)
        `when`(remoteDataSource.getForecastByCoordinates(lat, lon, "metric")).thenReturn(call)

        repository.forecastByCoordinates(lat, lon, "metric").observeForever(mockedObserver)

        // Verify that the repo DOES NOT make a network call
        verify(remoteDataSource, never()).getForecastByCoordinates(lat, lon, "metric")
        // Verify that the repo DOES CALL the local db
        //verify(localDataSource.forecastByCoordinates(lat, lon))

        verify(mockedObserver).onChanged(Resource.success(fakeForecastEntity))

    }


    /**
     * Asserts that, if the forecast in local does not match the query or there is no forecast
     * in the local database, a network call is made

    @Test
    fun goToNetwork(){
        val lat = 38.24
        val lon = -1.42
        // There is no data on the local database
        //forecastDao.deleteAll()

        val dbData = MutableLiveData<ForecastEntity>()
        `when`(forecastDao.loadForecastByCoordinates(lat, lon)).thenReturn(dbData)

        val fakeResponse = TestUtil.createFakeForecastResponse()
        val call = ApiUtil.successCall(fakeResponse)
        `when`(apiService.forecastByCoordinates(lat, lon, "metric")).thenReturn(call)
        val observer = mock<Observer<Resource<ForecastEntity>>>()

        // Call
        repository.forecastByCoordinates(lat, lon, "metric").observeForever(observer)

        // Verify that the remote call never gets done
        //verify(apiService, never()).forecastByCoordinates(lat, lon, "metric")

        val updatedDbData = MutableLiveData<ForecastEntity>()
        `when`(forecastDao.loadForecastByCoordinates(lat, lon)).thenReturn(updatedDbData)
        dbData.value = null
        verify(apiService).forecastByCoordinates(lat, lon, "metric")


    }*/

}