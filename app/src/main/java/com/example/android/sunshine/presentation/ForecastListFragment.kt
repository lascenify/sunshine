package com.example.android.sunshine.presentation

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.R
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.interactors.ForecastByCoordinates
import com.example.android.sunshine.framework.SunshineApplication
import javax.inject.Inject

class ForecastListFragment :Fragment(), SharedPreferences.OnSharedPreferenceChangeListener{

    //private lateinit var binding : ForecastListFragmentBinding

    @Inject lateinit var viewModel : ForecastViewModel

    lateinit var recyclerView: RecyclerView


    companion object {
        private var PREFERENCE_UPDATES_FLAG = false
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as SunshineApplication).appComponent.inject(this)
    }

    /**
     * Register this class as a OnSharedPreferenceChangeListener
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //binding = DataBindingUtil.inflate(inflater, R.layout.forecast_list_fragment, container, false)
        //return binding.root
        val rootView = inflater.inflate(R.layout.forecast_list_fragment, container, false)
        recyclerView = rootView.findViewById(R.id.recyclerview_forecast)
        return rootView
    }

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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ForecastAdapter(){
            openDetailFragment(it)
        }
        recyclerView.adapter = adapter

        //viewModel = ViewModelProvider(this, SunshineViewModelFactory).get(ForecastViewModel::class.java)

        val coordinates : DoubleArray = SunshinePreferences.getLocationCoordinates(requireContext())
        val units : String = context?.getString(R.string.pref_units_metric)!!
        val lat = 38.24
        val lon = -1.42
        if (!coordinates.first().isNaN() && !coordinates.last().isNaN()){
            viewModel.setForecastParams(ForecastByCoordinates.Params(
                lat,
                lon,
                true,
                units
            ))
        }

        viewModel.forecast.observe(viewLifecycleOwner, Observer { resource ->
            if (resource != null){
                adapter.apply {
                    resource.data?.list?.let { adapter.update(it) }
                }
            }
            //resource.data?.list?.let { adapter.update(it)}
        }

            /*Observer { forecastViewState ->
            adapter.update(forecastViewState.data?.list!!)
        }*/)



        setHasOptionsMenu(true)
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

    private fun showLoading(){
        recyclerView.visibility = View.INVISIBLE
        //binding.pbForecast.visibility = View.VISIBLE
    }

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
                /*invalidateData()
                loadWeatherData()*/
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