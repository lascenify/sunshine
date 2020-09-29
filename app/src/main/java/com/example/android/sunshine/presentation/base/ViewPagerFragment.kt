package com.example.android.sunshine.presentation.base

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.sunshine.R
import com.example.android.sunshine.databinding.FragmentViewPagerBinding
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.presentation.city.CityFragment
import com.example.android.sunshine.presentation.onboarding.OnboardingActivity
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import javax.inject.Inject

class ViewPagerFragment : Fragment() {

    @Inject
    lateinit var viewModel: CitiesViewModel

    private lateinit var binding: FragmentViewPagerBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context.applicationContext as SunshineApplication).appComponent.inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        PreferenceManager.getDefaultSharedPreferences(context).apply {
            if (getBoolean(SunshinePreferences.PREF_USER_FIRST_TIME, false)) {
                PreferenceManager.getDefaultSharedPreferences(context).edit().apply {
                    putBoolean(SunshinePreferences.PREF_USER_FIRST_TIME, false)
                    apply()
                }
                findNavController().navigate(R.id.nav_searchFragment)

            }
        }
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.forecasts.observe(viewLifecycleOwner, Observer { forecasts ->
            if (forecasts != null){
                if (forecasts.status.isSuccessful() && forecasts.data?.isNotEmpty()!!){
                    val viewPagerAdapter = createViewPagerAdapter(forecasts.data)
                    binding.viewPager.adapter = viewPagerAdapter
                    binding.springDotsIndicator.setViewPager2(binding.viewPager)
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
        }
    }

}
