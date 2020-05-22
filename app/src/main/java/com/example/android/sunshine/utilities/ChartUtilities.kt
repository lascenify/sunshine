package com.example.android.sunshine.utilities

import android.graphics.Color
import com.example.android.sunshine.R
import com.example.android.sunshine.core.domain.ForecastListItem
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

object ChartUtilities {
    fun setUpChart(list: List<ForecastListItem>, lineChart: LineChart, label: String){
        val values = ArrayList<Entry>()
        val yValues = ArrayList<Double>()
        val xAxisValues = arrayListOf<String>()
        list.forEachIndexed { index, forecastItem ->
            xAxisValues.add(forecastItem.getHourOfDay())
            val value = forecastItem.getTemperature()!!
            yValues.add(value)
            values.add(Entry(index.toFloat(), value.toFloat()))
        }

        val set1 = LineDataSet(values, label)
        set1.axisDependency = YAxis.AxisDependency.LEFT
        set1.color = ColorTemplate.getHoloBlue()
        set1.valueTextColor = ColorTemplate.getHoloBlue()
        set1.lineWidth = 4.0f
        set1.setDrawCircles(false)
        set1.setDrawValues(false)
        set1.fillAlpha = 65
        set1.fillColor = Color.RED
        set1.highLightColor = Color.rgb(244, 117, 117)
        set1.setDrawCircleHole(false)
        set1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

        // create a data object with the data sets

        // create a data object with the data sets
        val data = LineData(set1)
        data.setValueTextColor(Color.WHITE)
        data.setValueTextSize(9f)

        // add some transparency to the color with "& 0x90FFFFFF"
        customSettings(lineChart, data, xAxisValues, yValues)
    }

    private fun customSettings(chart: LineChart, data: LineData, xAxisValues: ArrayList<String>, yAxisValues: ArrayList<Double>){
        chart.data = data
        chart.description.isEnabled = false
        chart.setTouchEnabled(false)

        //chart.dragDecelerationFrictionCoef = 0.9f
        // enable scaling and dragging
        chart.isDragEnabled = false
        chart.setScaleEnabled(false)
        chart.setDrawGridBackground(false)
        chart.isHighlightPerDragEnabled = true
        // set an alternative background color
        chart.setBackgroundResource(R.color.colorPrimaryDark)
        chart.setViewPortOffsets(0f, 0f, 0f, 0f)


        // get the legend (only possible after setting data)
        val l = chart.legend
        l.isEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.textColor = Color.WHITE

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 12f
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.WHITE
        //xAxis.setCenterAxisLabels(true)
        xAxis.setAvoidFirstLastClipping(false)
        xAxis.granularity = 2f // three hours
        xAxis.axisMaximum = xAxisValues.size.toFloat()
        xAxis.labelCount = xAxisValues.size
        xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)


        val leftAxis = chart.axisLeft
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.textColor = Color.WHITE
        leftAxis.setDrawGridLines(true)
        leftAxis.isGranularityEnabled = true
        leftAxis.axisMinimum = yAxisValues.min()?.minus(10)?.toFloat()!!
        leftAxis.axisMaximum = yAxisValues.max()?.plus(10)?.toFloat()!!
        leftAxis.yOffset = -9f


        val rightAxis = chart.axisRight
        rightAxis.isEnabled = false
    }
}