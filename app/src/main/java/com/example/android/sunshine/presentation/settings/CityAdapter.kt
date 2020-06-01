package com.example.android.sunshine.presentation.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.databinding.ItemDaySettingsBinding
import com.example.android.sunshine.framework.db.entities.CityEntity
import com.example.android.sunshine.framework.db.entities.ForecastEntity
import com.example.android.sunshine.utilities.WeatherUtils

class CityAdapter(
    private val layoutId: Int
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private var cityList: List<ForecastEntity>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemDaySettingsBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = cityList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdForPosition(position)

    private fun getLayoutIdForPosition(position: Int) = layoutId


    inner class ViewHolder(private val binding: ItemDaySettingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val temperature = cityList?.get(position)?.getActualTemperature()!!
            val city = cityList?.get(position)?.city
            binding.city = city
            binding.temperature = WeatherUtils.formatSimpleTemperature(itemView.context, temperature.toInt())
            binding.executePendingBindings()
        }
    }


    fun update(newList: List<ForecastEntity>) {
        cityList = newList
        notifyDataSetChanged()
    }
}
