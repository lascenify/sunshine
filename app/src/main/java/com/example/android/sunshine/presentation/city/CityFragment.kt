package com.example.android.sunshine.presentation.city

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.android.sunshine.core.domain.forecast.ForecastListItem
import com.example.android.sunshine.core.domain.forecast.OneDayForecast
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.databinding.CityFragmentBinding
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.presentation.ForecastComponentProvider
import com.example.android.sunshine.presentation.MainActivity
import com.example.android.sunshine.presentation.common.HourForecastAdapter
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel
import com.example.android.sunshine.utilities.ChartUtilities
import javax.inject.Inject

class CityFragment :Fragment(), SharedPreferences.OnSharedPreferenceChangeListener{

    private var FIRST_TIME = true
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

        FIRST_TIME = true
        setForecastParams()

        bindViews()

        initForecastList()

        setHasOptionsMenu(true)
    }

    private fun bindViews() {
        dayForecastAdapter = DayForecastAdapter(R.layout.item_day_forecast) { oneDayForecast ->
            openDayFragment(oneDayForecast)
        }

        hourForecastAdapter = HourForecastAdapter(R.layout.item_hour_forecast) { forecastListItem ->
                updateConditionsUI(forecastListItem)
            }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.hoursForecastLayout.recyclerviewForecastHours.adapter = hourForecastAdapter
        binding.daysForecastLayout.recyclerviewForecastDays.adapter = dayForecastAdapter
    }

    private fun initForecastList() {
        viewModel.forecast.removeObservers(viewLifecycleOwner)
        viewModel.forecast.observe(viewLifecycleOwner, Observer { resource ->
            // CHANGE CONDITION TO SHOW PROGRESS BAR
            if (resource != null && resource.status.isSuccessful()){
                //viewModel.insertCity(resource.data?.city?.toDomain()!!) // remove later
                binding.forecast = viewModel.forecast
                dayForecastAdapter.apply {
                    resource.data?.list?.let{ update(viewModel.forecastOfNextDays()) }
                }
                hourForecastAdapter.apply {
                    resource.data?.list?.let{ update(viewModel.forecastOfNextHours())}
                }
                resource.data?.list?.get(0)?.let { updateConditionsUI(it) }
                if (FIRST_TIME) {
                    FIRST_TIME = false
                    val isMetric = SunshinePreferences.isMetric(requireContext())
                    ChartUtilities.setUpChart(
                        viewModel.forecastOfNextHours().subList(0, 9),
                        binding.lineChart,
                        "Temperature of the next 24 hours",
                        isMetric
                    )
                    Log.d("chart", binding.lineChart.xAxis.labelCount.toString())
                    Log.d("chart", binding.lineChart.axisLeft.labelCount.toString())
                }
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
        val lon = -1.4199
        if (!coordinates.first().isNaN() && !coordinates.last().isNaN()) {
            viewModel.setForecastParams(
                ForecastByCoordinates.Params(
                    lat,
                    lon,
                    units
                )
            )
        }
    }

    /**
     * Method to navigate to DetailFragment
     */
    private fun openDayFragment(oneDayForecast: OneDayForecast){
        viewModel.setSelectedDay(oneDayForecast)
        val action = CityFragmentDirections.actionCityFragmentToDayFragment()
        findNavController().navigate(action)
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
                retry()
            }
            R.id.action_open_map -> {
                openLocationInMap()
            }
            R.id.action_setings -> {
                findNavController().navigate(R.id.nav_settingsFragment)
            }

            R.id.action_manage_cities -> {
                findNavController().navigate(R.id.nav_citiesManagementFragment)
            }
        }
        return true
    }

    private fun retry() = viewModel.retry()

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