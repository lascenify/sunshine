package com.example.android.sunshine.utilities

import androidx.test.core.app.ApplicationProvider
import com.example.android.sunshine.core.domain.forecast.ForecastListItem
import com.github.mikephil.charting.charts.LineChart
import org.junit.Before
import org.junit.Test
import org.hamcrest.MatcherAssert.assertThat

import org.hamcrest.Matchers.`is`


class ChartUtilitiesTest{


    private val dataList = mutableListOf<ForecastListItem>()

    private lateinit var chart: LineChart

    @Before
    fun setup(){
        for (i in 0 until 40)
            dataList.add(TestUtil.createFakeForecastListItem())
        chart = LineChart(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun testConfigureDataSet(){
        ChartUtilities.setUpChart(dataList, chart, "title", true)
        assertThat(chart.data.entryCount, `is`(40))
        assertThat(chart.data.dataSetLabels.contains("title"), `is`(true))
        assertThat(chart.data.dataSetCount, `is`(1))
    }
}