package com.example.android.sunshine.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.sunshine.core.domain.ForecastResponse
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
    private lateinit var fakeForecastResponse: ForecastResponse

    @Before
    fun setup(){
        forecastDao = db.weatherForecastDao()
        fakeForecastResponse = TestUtil.createFakeForecastResponse()
    }

    @Test
    fun testInsertAndReadForecast(){
        forecastDao.insertForecast(ForecastEntity(fakeForecastResponse))
        val forecastEntity = forecastDao.loadLastForecast().getOrAwaitValue()
        assertThat(forecastEntity?.list, notNullValue())
        assertThat(forecastEntity?.list?.size, `is`(2))
    }

/*@Test
fun testDeleteAllAndInsert(){
    val lat = 38.24
    val lon = -1.42
    forecastDao.deleteAllAndInsert(ForecastEntity(fakeForecastResponse))
    val count = forecastDao.getCount()
    val forecast = forecastDao.loadLastForecast()
    assertThat(count, `is`(1))
    assertThat(forecast.city?.coordinates?.lat, `is`(lat))
    assertThat(forecast.city?.coordinates?.lon, `is`(lon))
}*/
}