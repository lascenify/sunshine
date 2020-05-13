package com.example.android.sunshine.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.android.sunshine.R
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment :Fragment(){

    private lateinit var mDayForecastTextView : TextView
    private val FORECAST_SHARE_HASHTAG = " #SunshineApp"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.detail_fragment, container, false)
        mDayForecastTextView = rootView.findViewById(R.id.detail_forecast_tv)
        return rootView
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weatherInfo :String?
        val bundle = arguments
        if (bundle != null) {
            weatherInfo = bundle.getString(getString(R.string.dayForecasted_bundle))
            mDayForecastTextView.text = weatherInfo
        }


        setHasOptionsMenu(true)
    }


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
            findNavController().navigate(R.id.settingsFragment)
        }
        return true
    }

}
