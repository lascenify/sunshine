package com.example.android.sunshine.utilities

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.sunshine.core.domain.ForecastListItem
import com.github.mikephil.charting.charts.LineChart
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ChartUtilitiesTest{

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dataList = mutableListOf<ForecastListItem>()

    private lateinit var chart: LineChart

    @Before
    fun setup(){
        for (i in 0.. 40)
            dataList.add(TestUtil.createFakeForecastListItem())
        chart = LineChart(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testConfigureDataSet(){
        ChartUtilities.setUpChart(dataList, chart, "title", true)
        val entryCount = chart.data.entryCount
        
        assert(chart.data.entryCount == 2)
        assert(chart.data.dataSetLabels.contains("title"))
        assert(chart.data.dataSetCount == 2)
    }
}