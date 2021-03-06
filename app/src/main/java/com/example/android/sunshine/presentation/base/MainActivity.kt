
package com.example.android.sunshine.presentation.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.sunshine.R
import com.example.android.sunshine.databinding.MainActivityBinding
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.SunshinePreferences.PREF_USER_FINISHED_ONBOARDING
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.framework.di.component.CitiesManagementComponent
import com.example.android.sunshine.framework.di.component.CityForecastComponent
import com.example.android.sunshine.framework.di.component.SearchComponent
import com.example.android.sunshine.presentation.city.CityFragment
import com.example.android.sunshine.presentation.onboarding.OnboardingActivity
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import javax.inject.Inject
import javax.inject.Provider

typealias ForecastComponentProvider = Provider<CityForecastComponent>
class MainActivity : AppCompatActivity(),
    ForecastComponentProvider {

    lateinit var forecastComp: CityForecastComponent
    lateinit var citiesManagementComponent: CitiesManagementComponent
    lateinit var searchComponent: SearchComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)

        forecastComp = (applicationContext as SunshineApplication).appComponent.cityForecastComp().create()
        citiesManagementComponent = (applicationContext as SunshineApplication).appComponent.citiesSettingsComp().create()
        searchComponent = (applicationContext as SunshineApplication).appComponent.searchComp().create()

        PreferenceManager.getDefaultSharedPreferences(this).apply {
            // Check if we need to display our OnboardingSupportFragment
            if (!getBoolean(PREF_USER_FINISHED_ONBOARDING, false)) {
                // The user hasn't seen the OnboardingSupportFragment yet, so show it
                startActivity(Intent(this@MainActivity, OnboardingActivity::class.java))
            }
        }
    }


    override fun get(): CityForecastComponent = forecastComp


}