package com.example.android.sunshine.presentation.onboarding

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.sunshine.R
import com.example.android.sunshine.databinding.FragmentViewPagerBinding
import com.example.android.sunshine.framework.SunshineApplication
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.presentation.city.CityFragment
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import javax.inject.Inject


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ViewPagerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
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
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.forecasts.observe(viewLifecycleOwner, Observer { forecasts ->
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

}
