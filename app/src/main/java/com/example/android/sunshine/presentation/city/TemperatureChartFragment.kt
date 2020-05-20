package com.example.android.sunshine.presentation.city

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.core.cartesian.series.Column
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.example.android.sunshine.R
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.databinding.TempChartFragmentBinding
import com.example.android.sunshine.presentation.ForecastComponentProvider
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel
import javax.inject.Inject

class TemperatureChartFragment : Fragment(){

    private lateinit var binding: TempChartFragmentBinding

    @Inject
    lateinit var viewModel: ForecastViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as ForecastComponentProvider).get().inject(this)
        Log.i("viewmodel", "In TemperatureChartFragment using Viewmodel $viewModel")

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.temp_chart_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var forecastOfNextHours = listOf<ForecastListItem>()
        Log.d("viewModelOnTemp", viewModel.toString())
        viewModel.forecast.observe(viewLifecycleOwner, Observer { resource ->
            if (resource != null){
                if (resource.status.isSuccessful()){
                    forecastOfNextHours = viewModel.forecastOfNextHours()
                    fillChart(forecastOfNextHours)
                }
            }
        })

    }

    fun fillChart(forecastOfNextHours: List<ForecastListItem>){

        binding.tempChartView.setProgressBar(binding.chartPb)
        val cartesian = AnyChart.column()

        val dataList = arrayListOf<DataEntry>()

        forecastOfNextHours.forEach {forecastItem ->
            val hour = forecastItem.getHourOfDay()
            val value = forecastItem.getTemperature()
            dataList.add(ValueDataEntry(hour, value))
        }

        val column: Column = cartesian.column(dataList)

        column.tooltip()
            .titleFormat("{%X}")
            .position(Position.CENTER_BOTTOM)
            .anchor(Anchor.CENTER_BOTTOM)
            .offsetX(0.0)
            .offsetY(5.0)
            .format("\${%Value}{groupsSeparator: }")

        cartesian.animation(true)
        cartesian.title("Top 10 Cosmetic Products by Revenue")

        cartesian.yScale().minimum(0.0)

        cartesian.yAxis(0).labels().format("\${%Value}{groupsSeparator: }")

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
        cartesian.interactivity().hoverMode(HoverMode.BY_X)

        cartesian.xAxis(0).title("Product")
        cartesian.yAxis(0).title("Revenue")


        binding.tempChartView.setChart(cartesian)
    }
}