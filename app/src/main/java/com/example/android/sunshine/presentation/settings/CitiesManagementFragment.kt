package com.example.android.sunshine.presentation.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.R
import com.example.android.sunshine.core.data.AppExecutors
import com.example.android.sunshine.databinding.CitiesManagementFragmentBinding
import com.example.android.sunshine.presentation.base.MainActivity
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import javax.inject.Inject

class CitiesManagementFragment : Fragment(){

    private lateinit var binding: CitiesManagementFragmentBinding

    @Inject
    lateinit var viewModel: CitiesViewModel

    @Inject
    lateinit var appExecutors: AppExecutors

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).citiesManagementComponent.inject(this)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.cities_management_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setListeners()
    }

    private fun setUpRecyclerView() {
        val adapter = CityAdapter(R.layout.item_day_settings)
        binding.recyclerviewCities.adapter = adapter
        viewModel.forecasts.observe(viewLifecycleOwner, Observer { forecasts ->
            if (forecasts != null){
                if (forecasts.status.isSuccessful()){
                    val listOfForecasts = forecasts.data!!
                    adapter.apply {
                        update(listOfForecasts)
                    }
                }
            }
        })

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val city = adapter.cityList!![viewHolder.adapterPosition]
                viewModel.removeCity(city)
            }
        }).attachToRecyclerView(binding.recyclerviewCities)

    }

    private fun setListeners(){
        binding.fabAddCity.setOnClickListener { openSearchBar() }
    }

    private fun openSearchBar(){
        findNavController().navigate(R.id.nav_searchFragment)
    }

    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                AppExecutors.diskIO()?.execute {
                    val position = viewHolder.adapterPosition
                    val task = mAdapter.getTasks()?.get(position)!!
                    mDatabase.taskDao()?.deleteTask(task)
                }
            }
        }).attachToRecyclerView(recyclerViewTasks)
    }*/
}