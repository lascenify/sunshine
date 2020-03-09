package com.example.android.sunshine.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.R
import com.example.android.sunshine.MainActivity
import com.example.android.sunshine.data.SunshinePreferences
import com.example.android.sunshine.utilities.NetworkUtils
import com.example.android.sunshine.utilities.OpenWeatherJsonUtils
import java.io.IOException

class ForecastListFragment :Fragment(),
    ForecastAdapter.ForecastAdapterOnClickHandler, SharedPreferences.OnSharedPreferenceChangeListener{
    companion object{
        private var PREFERENCE_UPDATES_FLAG = false
        private lateinit var mRecyclerView: RecyclerView
        private var mForecastAdapter: ForecastAdapter? = null
        private lateinit var mErrorMessageTextView: TextView
        private lateinit var mProgressBar: ProgressBar
        private lateinit var mContext : Context

        private fun showWeatherDataView(){
            mErrorMessageTextView.visibility = View.INVISIBLE
            mRecyclerView.visibility = View.VISIBLE
        }

        private fun showErrorMessage(){
            mErrorMessageTextView.visibility = View.VISIBLE
            mRecyclerView.visibility = View.INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(context).registerOnSharedPreferenceChangeListener(this)

    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.forecast_list_fragment, container, false)
        mRecyclerView = rootView.findViewById(R.id.recyclerview_forecast)
        mErrorMessageTextView = rootView.findViewById(R.id.tv_error_message)
        mProgressBar = rootView.findViewById(R.id.pb_forecast)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mForecastAdapter =
            ForecastAdapter(this)
        mRecyclerView.adapter = mForecastAdapter

        setHasOptionsMenu(true)
        mContext = context!!
        loadWeatherData()
    }

    override fun onStart() {
        super.onStart()
        if (PREFERENCE_UPDATES_FLAG){
            loadWeatherData()
            PREFERENCE_UPDATES_FLAG = false
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(context).unregisterOnSharedPreferenceChangeListener(this)
    }


    private fun invalidateData() {
        mForecastAdapter!!.setWeatherData(null)
    }

    private fun loadWeatherData(){
        showWeatherDataView()
        val location = SunshinePreferences.getPreferredWeatherLocation(context)
        OpenWeatherAsyncTask().execute(location)
    }


    class StaticWeatherAsyncTask : AsyncTask<String, Void, Array<String?>?>() {
        override fun onPreExecute() {
            mProgressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): Array<String?>? {
            val location = params[0]
            val url = NetworkUtils.buildStaticUrl(location)
            try {
                val response = NetworkUtils.getResponseFromHttpUrl(url!!)
                val jsonWeatherData = OpenWeatherJsonUtils.getSimpleStaticWeatherStringsFromJson(
                    mContext, response)
                return jsonWeatherData!!
            } catch (e : IOException){
                e.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(result: Array<String?>?) {
            mProgressBar.visibility = View.INVISIBLE
            if (result != null) {
                showWeatherDataView()
                mForecastAdapter?.setWeatherData(result)
            }
            else{
                showErrorMessage()
            }
        }
    }

    class OpenWeatherAsyncTask : AsyncTask<String, Void, Array<String?>?>() {
        override fun onPreExecute() {
            mProgressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: String?): Array<String?>? {
            val location = params[0]
            val url = NetworkUtils.buildUrl(location)
            try {
                val response = NetworkUtils.getResponseFromHttpUrl(url!!)
                val jsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(
                    mContext, response)
                return jsonWeatherData!!
            } catch (e : IOException){
                e.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(result: Array<String?>?) {
            mProgressBar.visibility = View.INVISIBLE
            if (result != null) {
                showWeatherDataView()
                mForecastAdapter?.setWeatherData(result)
            }
            else{
                showErrorMessage()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forecast, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_refresh -> {
                invalidateData()
                loadWeatherData()
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

    private fun openLocationInMap(){
        val location = SunshinePreferences.getPreferredWeatherLocation(context)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$location"))

        if (intent.resolveActivity(activity?.packageManager!!) != null){
            startActivity(intent)
        } else{
            Log.d(MainActivity::class.simpleName, "Couldn't call $location, no receiving apps installed!")
        }
    }

    override fun onClick(weatherForecastForADay: String) {
        val bundle = Bundle()
        bundle.putString(getString(R.string.dayForecasted_bundle), weatherForecastForADay)
        findNavController().navigate(R.id.detailFragment, bundle)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        PREFERENCE_UPDATES_FLAG = true
    }


}