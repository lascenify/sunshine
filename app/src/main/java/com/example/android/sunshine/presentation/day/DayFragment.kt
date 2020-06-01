package com.example.android.sunshine.presentation.day

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.android.sunshine.R
import com.example.android.sunshine.core.domain.forecast.ForecastListItem
import com.example.android.sunshine.databinding.DayFragmentBinding
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.presentation.ForecastComponentProvider
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel
import com.example.android.sunshine.presentation.common.HourForecastAdapter
import com.example.android.sunshine.utilities.ChartUtilities
import javax.inject.Inject

class DayFragment :Fragment(){

    private lateinit var mDayForecastTextView : TextView
    private val FORECAST_SHARE_HASHTAG = " #SunshineApp"

    @Inject
    lateinit var viewModel: ForecastViewModel

    private lateinit var binding: DayFragmentBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as ForecastComponentProvider).get().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.day_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //getForecastFromArguments()
        setHasOptionsMenu(true)
        setupRecyclerView()
    }

    /*private fun getForecastFromArguments() {
        val dayForecast: OneDayForecast
        val bundle = arguments
        if (bundle != null) {
            dayForecast = bundle.getParcelable(getString(R.string.dayForecasted_bundle))!!
            binding.day = dayForecast
            viewModel.setDayForecast(dayForecast)
            updateUI(dayForecast.forecastList[0])
        }
    }*/

    private fun setupRecyclerView(){
        val adapter =
            HourForecastAdapter(
                R.layout.item_hour_forecast
            ) { forecastListItem ->
                updateUI(forecastListItem)
            }

        viewModel.selectedDay.observe(viewLifecycleOwner, Observer {forecast ->
            if (forecast != null) {
                updateUI(forecast.forecastList[0])
                binding.day = forecast
                adapter.update(forecast.forecastList)
                binding.hoursForecastLayout.recyclerviewForecastHours.adapter = adapter
                val isMetric = SunshinePreferences.isMetric(requireContext())
                ChartUtilities.setUpChart(forecast.forecastList, binding.lineChart, "Temperature", isMetric)
            }
        })
    }

    private fun updateUI(forecastListItem: ForecastListItem){
        binding.layoutDayConditions.listItem = forecastListItem

    }

/*
    private fun createShareForecastIntent():Intent = ShareCompat.IntentBuilder.from(activity as Activity)
        .setType("text/plain")
        .setText(detail_forecast_tv.text.toString() + FORECAST_SHARE_HASHTAG)
        .intent

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail_forecast, menu)
        val menuItem = menu.findItem(R.id.action_share)
        menuItem.intent = createShareForecastIntent()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_setings){
            findNavController().navigate(R.id.nav_settingsFragment)
        }
        return true
    }
*/
}
