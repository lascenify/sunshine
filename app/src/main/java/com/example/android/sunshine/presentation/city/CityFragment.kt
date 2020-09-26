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
import com.example.android.sunshine.databinding.CityFragmentBinding
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.presentation.base.ForecastComponentProvider
import com.example.android.sunshine.presentation.base.MainActivity
import com.example.android.sunshine.presentation.common.HourForecastAdapter
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import com.example.android.sunshine.utilities.ChartUtilities
import javax.inject.Inject

class CityFragment :Fragment(), SharedPreferences.OnSharedPreferenceChangeListener{

    private var FIRST_TIME = true
    private lateinit var binding : CityFragmentBinding

    private var cityId = 0L

    @Inject
    lateinit var viewModel : CitiesViewModel

    private lateinit var hourForecastAdapter: HourForecastAdapter
    private lateinit var dayForecastAdapter: DayForecastAdapter

    companion object {
        private var PREFERENCE_UPDATES_FLAG = false
        private const val KEY_CITY_ID = "cityId"

        fun create(cityId: Long) =
            CityFragment().apply {
                arguments = Bundle(1).apply {
                    putLong(KEY_CITY_ID, cityId)
                }
            }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)

        (context as ForecastComponentProvider).get().inject(this)
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

        val forecastId = arguments?.getLong(KEY_CITY_ID)
        if (forecastId != null)
            cityId = forecastId

        bindViews()

        initForecastList()

        setHasOptionsMenu(true)
    }

    private fun bindViews() {
        dayForecastAdapter = DayForecastAdapter(R.layout.item_day_forecast) { oneDayForecast, buttonClicked ->
            //openDayFragment(oneDayForecast)
            if (buttonClicked.equals("arrowDown")){

            } else if (buttonClicked.equals("arrowRight")){

            }
        }

        hourForecastAdapter = HourForecastAdapter(R.layout.item_hour_forecast) { forecastListItem ->
                updateConditionsUI(forecastListItem)
            }

        binding.lifecycleOwner = viewLifecycleOwner
        binding.hoursForecastLayout.recyclerviewForecastHours.adapter = hourForecastAdapter
        binding.daysForecastLayout.recyclerviewForecastDays.adapter = dayForecastAdapter

    }

    private fun initForecastList() {
        viewModel.forecasts.observe(viewLifecycleOwner, Observer { resource ->
            if (resource != null){
                if (resource.status.isSuccessful()) {
                    val forecastOfCity = viewModel.getForecastByCityId(cityId)
                    if (forecastOfCity != null) {
                        binding.forecast = forecastOfCity
                        dayForecastAdapter.apply {
                            update(viewModel.forecastOfNextDays(forecastOfCity))
                        }
                        hourForecastAdapter.apply {
                            forecastOfCity.list?.let { update(viewModel.forecastOfNextHours(forecastOfCity)) }
                        }
                        forecastOfCity.list?.get(0)?.let { updateConditionsUI(it) }
                        if (FIRST_TIME) {
                            FIRST_TIME = false
                            val isMetric = SunshinePreferences.isMetric(requireContext())
                            ChartUtilities.setUpChart(
                                viewModel.forecastOfNextHours(forecastOfCity).subList(0, 9),
                                binding.lineChart,
                                "Temperature of the next 24 hours",
                                isMetric
                            )
                        }
                    }
                }
            }
        })
        binding.listOfForecasts = viewModel.forecasts

    }

    private fun updateConditionsUI(forecastListItem: ForecastListItem){
        binding.cityConditionsLayout.listItem = forecastListItem
    }


    /**
     * Method to navigate to DetailFragment
     */
    private fun openDayFragment(oneDayForecast: OneDayForecast){
        //viewModel.setSelectedDay(oneDayForecast)
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
                //retry()
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

    //private fun retry() = viewModel.retry()

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

}