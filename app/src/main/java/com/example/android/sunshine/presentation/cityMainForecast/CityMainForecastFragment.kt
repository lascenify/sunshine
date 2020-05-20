package com.example.android.sunshine.presentation.cityMainForecast

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
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.databinding.ForecastListFragmentBinding
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.di.ForecastScope
import com.example.android.sunshine.presentation.ForecastComponentProvider
import com.example.android.sunshine.presentation.MainActivity
import javax.inject.Inject

class CityMainForecastFragment :Fragment(), SharedPreferences.OnSharedPreferenceChangeListener{

    private lateinit var binding : ForecastListFragmentBinding

    @Inject
    lateinit var viewModel : ForecastViewModel


    private lateinit var hourForecastAdapter: HourForecastAdapter
    private lateinit var dayForecastAdapter: DayForecastAdapter

    companion object {
        private var PREFERENCE_UPDATES_FLAG = false
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //val component = (context.applicationContext as SunshineApplication).appComponent.cityForecastComp().create()
        //component.inject(this)

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
        binding = DataBindingUtil.inflate(inflater, R.layout.forecast_list_fragment, container, false)
        return binding.root
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setForecastParams()

        dayForecastAdapter = DayForecastAdapter(R.layout.item_day_forecast)

        hourForecastAdapter = HourForecastAdapter(R.layout.item_hour_forecast)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.forecast = viewModel.forecast
        binding.recyclerviewForecastDays.adapter = dayForecastAdapter
        binding.recyclerViewForecastHours.adapter = hourForecastAdapter

        initForecastList()

        setHasOptionsMenu(true)
    }

    private fun initForecastList() {
        Log.d("viewModelOnCity", viewModel.toString())
        viewModel.forecast.observe(viewLifecycleOwner, Observer { resource ->
            if (resource != null){
                dayForecastAdapter.apply {
                    resource.data?.list?.let{ update(viewModel.forecastOfNextDays()) }
                }
                hourForecastAdapter.apply {
                    resource.data?.list?.let{ update(viewModel.forecastOfNextHours())}
                }
            }
        })
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
    private fun openDetailFragment(forecastListItem: ForecastListItem){
        val bundle = Bundle()
        bundle.putParcelable(getString(R.string.dayForecasted_bundle), forecastListItem)
        findNavController().navigate(R.id.detailFragment, bundle)
    }


    /**
     * If the apps gets destroyed, we unregister this preferenceChangeListener
     */
    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(this)
    }

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
                findNavController().navigate(R.id.settingsFragment)
            }
        }
        return true
    }

    fun forceRefreshData() = viewModel.forceRefresh()

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