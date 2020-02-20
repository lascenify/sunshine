/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lascenify.sunshine

import android.content.Context
import android.icu.lang.UCharacter
import android.os.AsyncTask
import android.os.Bundle
import android.renderscript.Script
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.R
import com.lascenify.sunshine.data.SunshinePreferences
import com.lascenify.sunshine.utilities.NetworkUtils
import com.lascenify.sunshine.utilities.OpenWeatherJsonUtils
import java.io.IOException

class MainActivity : AppCompatActivity() {

    companion object{
        private lateinit var mRecyclerView: RecyclerView
        private var mForecastAdapter: ForecastAdapter? = null
        private lateinit var mErrorMessageTextView: TextView
        private lateinit var mProgressBar: ProgressBar
        private lateinit var mContext :Context

        private fun showWeatherDataView(){
            mErrorMessageTextView.visibility = INVISIBLE
            mRecyclerView.visibility = VISIBLE
        }

        private fun showErrorMessage(){
            mErrorMessageTextView.visibility = VISIBLE
            mRecyclerView.visibility = INVISIBLE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast)
        mContext = applicationContext
        mRecyclerView = findViewById(R.id.recyclerview_forecast)
        mErrorMessageTextView = findViewById(R.id.tv_error_message)
        mProgressBar = findViewById(R.id.pb_forecast)

        val layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.layoutManager = layoutManager
        mRecyclerView.setHasFixedSize(true)
        mForecastAdapter = ForecastAdapter()
        mRecyclerView.adapter = mForecastAdapter

        loadWeatherData()
    }


    private fun loadWeatherData(){
        showWeatherDataView()
        val location = SunshinePreferences.getPreferredWeatherLocation(applicationContext)
        OpenWeatherAsyncTask().execute(location)
    }


    class OpenWeatherAsyncTask : AsyncTask<String, Void, Array<String?>?>() {
        override fun onPreExecute() {
            mProgressBar.visibility = VISIBLE
        }

        override fun doInBackground(vararg params: String?): Array<String?>? {
            val location = params[0]
            val url = NetworkUtils.buildUrl(location)
            try {
                val response = NetworkUtils.getResponseFromHttpUrl(url!!)
                val jsonWeatherData = OpenWeatherJsonUtils.getSimpleWeatherStringsFromJson(mContext, response)
                return jsonWeatherData!!
            } catch (e : IOException){
                e.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(result: Array<String?>?) {
            mProgressBar.visibility = INVISIBLE
            if (result != null) {
                showWeatherDataView()
                mForecastAdapter?.setWeatherData(result)
            }
            else{
                showErrorMessage()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(applicationContext).inflate(R.menu.forecast, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_refresh) {
            mForecastAdapter = null
            loadWeatherData()
        }
        return true
    }
}