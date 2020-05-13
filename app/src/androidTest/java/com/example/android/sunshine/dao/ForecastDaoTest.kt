package com.example.android.sunshine.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.sunshine.core.domain.Coordinates
import com.example.android.sunshine.framework.db.dao.ForecastDao
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.utilities.TestUtil
import com.example.android.sunshine.utilities.getOrAwaitValue
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ForecastDaoTest : DbTest(){

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var forecastDao: ForecastDao
    private lateinit var fakeForecast1: ForecastEntity
    private lateinit var fakeForecast2: ForecastEntity


    private lateinit var coordinates1: Coordinates
    private lateinit var coordinates2: Coordinates

    @Before
    fun setup(){
        coordinates1 = Coordinates(-1.42, 38.24)
        coordinates2 = Coordinates(50.0, 50.0)
        forecastDao = db.weatherForecastDao()
        fakeForecast1 = ForecastEntity(TestUtil.createFakeForecastResponse(coordinates1))
        fakeForecast2 = ForecastEntity(TestUtil.createFakeForecastResponse(coordinates2))
    }

    @Test
    fun testInsertAndReadForecast(){
        forecastDao.insertForecast(fakeForecast1)
        /**
         * We need an observer for the liveData to get its value. If we don't call
         * getOrAwaitValue or set an observer, the value of the livedata returned by the DAO
         * will always be null.
         */
        val forecastEntity = forecastDao.loadLastForecast().getOrAwaitValue()
        assertThat(forecastEntity?.list, notNullValue())
        assertThat(forecastEntity?.list?.size, `is`(2))
    }

    /**
     * Test the deleteAllAndInsert function on the DAO
     */
    @Test
    fun testDeleteAllAndInsert(){
        forecastDao.deleteAllAndInsert(fakeForecast1)
        val count = forecastDao.getCount()
        val forecast = forecastDao.loadLastForecast().getOrAwaitValue()
        assertThat(count, `is`(1))
        assertThat(forecast?.city?.coordinates?.lat, `is`(coordinates1.latitude))
        assertThat(forecast?.city?.coordinates?.lon, `is`(coordinates1.longitude))
    }

    /**
     * Test that we get the expected forecast passing the coordinates
     */
    @Test
    fun testForecastByCoordinates(){
        /**
         * the fake forecast response has the same coordinates that we are passing later:
         * lat 38.24
         * lon -1.42
         */
        forecastDao.deleteAllAndInsert(fakeForecast1)
        val forecast = forecastDao.loadForecastByCoordinates(
            lon = coordinates1.longitude!!,
            lat = coordinates1.latitude!!).getOrAwaitValue()

        assertThat(forecast, notNullValue())
        assertThat(forecast?.city?.coordinates?.lat, `is`(coordinates1.latitude))
        assertThat(forecast?.city?.coordinates?.lon, `is`(coordinates1.longitude))
    }

    @Test
    fun testDeleteSingleForecast(){
        forecastDao.deleteAllAndInsert(fakeForecast1)
        forecastDao.insertForecast(fakeForecast2)

        val countBeforeDelete = forecastDao.getCount()
        forecastDao.deleteForecastByCoordinates(
            lat = coordinates1.latitude!!,
            lon = coordinates1.longitude!!)

        val countAfterDelete = forecastDao.getCount()
        val resultingForecast = forecastDao.loadLastForecast().getOrAwaitValue()

        assertThat(countBeforeDelete, `is`(2))
        assertThat(countAfterDelete, `is`(1))
        assertThat(resultingForecast?.city?.coordinates?.lat, `is`(coordinates2.latitude))
        assertThat(resultingForecast?.city?.coordinates?.lon, `is`(coordinates2.longitude))
    }

    @Test
    fun testDeleteAllForecasts(){
        forecastDao.deleteAllAndInsert(fakeForecast1)
        forecastDao.insertForecast(fakeForecast2)

        val countBeforeDelete = forecastDao.getCount()
        forecastDao.deleteAll()
        val countAfterDelete = forecastDao.getCount()
        assertThat(countBeforeDelete, `is`(2))
        assertThat(countAfterDelete, `is`(0))
    }
}