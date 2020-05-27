package com.example.android.sunshine.presentation.settings

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.R
import com.example.android.sunshine.core.data.AppExecutors
import com.example.android.sunshine.databinding.CitiesManagementFragmentBinding
import com.example.android.sunshine.framework.SunshinePreferences
import com.example.android.sunshine.framework.db.entities.CityEntity
import com.example.android.sunshine.presentation.ForecastComponentProvider
import com.example.android.sunshine.presentation.MainActivity
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import javax.inject.Inject

class CitiesManagementFragment : Fragment(){

    private lateinit var binding: CitiesManagementFragmentBinding

    @Inject
    lateinit var viewModel: CitiesViewModel


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
        SunshinePreferences.addCityToForecasts(requireContext(), "Cieza")
        val cities = SunshinePreferences.getListOfCities(requireContext())
        viewModel.setCitiesParams(cities)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        val adapter = CityAdapter(R.layout.item_day_settings) { city ->
            Log.d("city", city.toString())
        }
        binding.recyclerviewCities.adapter = adapter
        viewModel.cities.observe(viewLifecycleOwner, Observer { listOfCities ->
            if (listOfCities != null) {
                //binding.forecast = viewModel.forecast
                adapter.apply {
                    update(listOfCities)
                }
            }
        })
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