package com.example.android.sunshine.presentation.city

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.android.sunshine.R
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.domain.OneDayForecast
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.databinding.CityFragmentBinding
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.presentation.ForecastComponentProvider
import com.example.android.sunshine.presentation.MainActivity
import com.example.android.sunshine.presentation.common.HourForecastAdapter
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import javax.inject.Inject
import kotlin.collections.ArrayList

class CityFragment :Fragment(), SharedPreferences.OnSharedPreferenceChangeListener{

    private lateinit var binding : CityFragmentBinding

    @Inject
    lateinit var viewModel : ForecastViewModel


    private lateinit var hourForecastAdapter: HourForecastAdapter
    private lateinit var dayForecastAdapter: DayForecastAdapter

    companion object {
        private var PREFERENCE_UPDATES_FLAG = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context as ForecastComponentProvider).get().inject(this)
        Log.i("viewmodel", "In CityMainForecastFragment using Viewmodel $viewModel")
    }

    /**
     * Register this class as a OnSharedPreferenceChangeListener
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.city_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setForecastParams()

        bindViews()

        initForecastList()

        setHasOptionsMenu(true)
    }

    private fun bindViews() {
        dayForecastAdapter = DayForecastAdapter(R.layout.item_day_forecast) { oneDayForecast ->
            openDetailFragment(oneDayForecast)
        }

        hourForecastAdapter = HourForecastAdapter(R.layout.item_hour_forecast) { forecastListItem ->
                updateConditionsUI(forecastListItem)
            }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.forecast = viewModel.forecast
        binding.hoursForecastLayout.recyclerviewForecastHours.adapter = hourForecastAdapter
        binding.daysForecastLayout.recyclerviewForecastDays.adapter = dayForecastAdapter
    }

    private fun initForecastList() {
        viewModel.forecast.observe(viewLifecycleOwner, Observer { resource ->
            if (resource != null){
                dayForecastAdapter.apply {
                    resource.data?.list?.let{ update(viewModel.forecastOfNextDays()) }
                }
                hourForecastAdapter.apply {
                    resource.data?.list?.let{ update(viewModel.forecastOfNextHours())}
                }
                resource.data?.list?.get(0)?.let { updateConditionsUI(it) }
                if (resource.status.isSuccessful())
                    setUpChart(viewModel.forecastOfNextHours().subList(0, 9))
            }
        })

    }

    private fun updateConditionsUI(forecastListItem: ForecastListItem){
        binding.cityConditionsLayout.listItem = forecastListItem
    }

    private fun setForecastParams() {
        val coordinates: DoubleArray = SunshinePreferences.getLocationCoordinates(requireContext())
        val units: String = context?.getString(R.string.pref_units_metric)!!
        val lat = 38.24
        val lon = -1.42
        if (!coordinates.first().isNaN() && !coordinates.last().isNaN()) {
            viewModel.setForecastParams(
                ForecastByCoordinates.Params(
                    lat,
                    lon,
                    true,
                    units
                )
            )
        }
    }

    /**
     * Method to navigate to DetailFragment
     */
    private fun openDetailFragment(oneDayForecast: OneDayForecast){
        val args = Bundle()
        args.putParcelable(getString(R.string.dayForecasted_bundle), oneDayForecast)
        findNavController().navigate(
            R.id.nav_dayFragment,
            args
        )
    }

    private fun setUpChart(list: List<ForecastListItem>){
        val values = ArrayList<Entry>()
        val yValues = ArrayList<Double>()
        val xAxisValues = arrayListOf<String>()
        list.forEachIndexed { index, forecastItem ->
            xAxisValues.add(forecastItem.getHourOfDay())
            val value = forecastItem.getTemperature()!!
            yValues.add(value)
            values.add(Entry(index.toFloat(), value.toFloat()))
        }

        val set1 = LineDataSet(values, "Temperatures of the next 24 hours")
        set1.axisDependency = AxisDependency.LEFT
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
        setupChart(binding.lineChart, data, xAxisValues, yValues)
    }

    private fun setupChart(chart: LineChart, data: LineData, xAxisValues: ArrayList<String>, yAxisValues: ArrayList<Double>){
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

    /**
     * If the apps gets destroyed, we unregister this preferenceChangeListener
     */
    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(this)
    }


    /**
     * Inflates the menu
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forecast, menu)
    }

    /**
     * Method that calls one or another method regarding what option in the menu was chosen
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                forceRefreshData()
            }
            R.id.action_open_map -> {
                openLocationInMap()
            }
            R.id.action_setings -> {
                findNavController().navigate(R.id.nav_settingsFragment)
            }
        }
        return true
    }

    private fun forceRefreshData() = viewModel.forceRefresh()

    /**
     * Method used to open the location in map from the menu
     */
    private fun openLocationInMap(){
        val location = SunshinePreferences.getPreferredWeatherLocation(requireContext())
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$location"))

        if (intent.resolveActivity(activity?.packageManager!!) != null){
            startActivity(intent)
        } else{
            Log.d(MainActivity::class.simpleName, "Couldn't call $location, no receiving apps installed!")
        }
    }


    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        PREFERENCE_UPDATES_FLAG = true
    }



    /*
    override fun onStart() {
        super.onStart()
        if (PREFERENCE_UPDATES_FLAG){
            loadWeatherData()
            PREFERENCE_UPDATES_FLAG = false
        }
    }

    private fun loadWeatherData(){
        //showWeatherDataView()
        //iewModel.loadForecast()
    }*/
    /*private fun showLoading(){
        recyclerView.visibility = View.INVISIBLE
        //binding.pbForecast.visibility = View.VISIBLE
    }*/

/*
    private fun showWeatherDataView(){
        binding.pbForecast.visibility = View.INVISIBLE
        binding.tvErrorMessage.visibility = View.INVISIBLE
        binding.recyclerviewForecast.visibility = View.VISIBLE
    }

    private fun showErrorMessage(){
        binding.pbForecast.visibility = View.INVISIBLE
        binding.tvErrorMessage.visibility = View.VISIBLE
        binding.recyclerviewForecast.visibility = View.INVISIBLE
    }*/


}