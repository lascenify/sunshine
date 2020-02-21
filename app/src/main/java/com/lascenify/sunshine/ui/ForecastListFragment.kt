package com.lascenify.sunshine.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.R
import com.lascenify.sunshine.data.SunshinePreferences
import com.lascenify.sunshine.utilities.NetworkUtils
import com.lascenify.sunshine.utilities.OpenWeatherJsonUtils
import java.io.IOException

class ForecastListFragment :Fragment(),
    ForecastAdapter.ForecastAdapterOnClickHandler {

    companion object{
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


    private fun loadWeatherData(){
        showWeatherDataView()
        val location = SunshinePreferences.getPreferredWeatherLocation(context)
        StaticWeatherAsyncTask().execute(location)
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
        inflater.inflate(R.menu.forecast_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_refresh) {
            mForecastAdapter = null
            loadWeatherData()
        }
        else if (id == R.id.action_open_map){
            val location = SunshinePreferences.getPreferredWeatherLocation(context)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=$location"))
            startActivity(intent)
        }
        return true
    }

    override fun onClick(weatherForecastForADay: String) {
        val bundle = Bundle()
        bundle.putString(getString(R.string.dayForecasted_bundle), weatherForecastForADay)
        findNavController().navigate(R.id.detailFragment, bundle)
    }
}