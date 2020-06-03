
package com.example.android.sunshine.presentation.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.sunshine.R
import com.example.android.sunshine.databinding.MainActivityBinding
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.framework.di.component.CitiesManagementComponent
import com.example.android.sunshine.framework.di.component.CityForecastComponent
import com.example.android.sunshine.framework.di.component.SearchComponent
import com.example.android.sunshine.presentation.city.CityFragment
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import javax.inject.Inject
import javax.inject.Provider

typealias ForecastComponentProvider = Provider<CityForecastComponent>
class MainActivity : FragmentActivity(),
    ForecastComponentProvider {

    lateinit var forecastComp: CityForecastComponent
    lateinit var citiesManagementComponent: CitiesManagementComponent
    lateinit var searchComponent: SearchComponent

    @Inject
    lateinit var viewModel: CitiesViewModel

    lateinit var binding: MainActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (applicationContext as SunshineApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)

        forecastComp = (applicationContext as SunshineApplication).appComponent.cityForecastComp().create()
        citiesManagementComponent = (applicationContext as SunshineApplication).appComponent.citiesSettingsComp().create()
        searchComponent = (applicationContext as SunshineApplication).appComponent.searchComp().create()


        viewModel.forecasts.observe(this, Observer { forecasts ->
            if (forecasts != null){
                if (forecasts.status.isSuccessful()){
                    val viewPagerAdapter = createViewPagerAdapter(forecasts.data!!)
                    binding.viewPager.adapter = viewPagerAdapter
                }
            }
        })
    }

    private fun createViewPagerAdapter(forecasts: List<ForecastEntity>): RecyclerView.Adapter<*> {

        return object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): CityFragment {
                val cityId = forecasts[position].id!!.toLong()
                return CityFragment.create(cityId)
            }
            override fun getItemCount(): Int = forecasts.size
            override fun getItemId(position: Int): Long = forecasts[position].id?.toLong()!!
            override fun containsItem(cityId: Long): Boolean = viewModel.contains(cityId)
        }
    }

    override fun get(): CityForecastComponent = forecastComp


}