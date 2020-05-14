package com.example.android.sunshine.presentation.cityMainForecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.BR
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.databinding.ItemHourForecastBinding
import com.example.android.sunshine.presentation.viewmodel.ForecastViewModel
import com.example.android.sunshine.utilities.getNextDayOfYearFromTxt

class HourForecastAdapter(
    private val layoutId: Int
) : RecyclerView.Adapter<HourForecastAdapter.ViewHolder>() {

    private var forecastList: List<ForecastListItem>? = null

    inner class ViewHolder (private val binding: ItemHourForecastBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val forecast = forecastList?.get(position)
            binding.setVariable(BR.forecast, forecast)
            binding.forecastListItem = forecast
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemHourForecastBinding = DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = if (forecastList == null) 0 else forecastList!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdForPosition(position)

    private fun getLayoutIdForPosition(position: Int) = layoutId
    /**
     * Called whenever the list is updated.
     * Changes are being listened in the view.
     */
    fun update(newForecastList: List<ForecastListItem>){
        this.forecastList = newForecastList
        fetchNextHoursForecast()
        notifyDataSetChanged()
    }

    /**
     * We only need the next hours to be displayed in this RecyclerView.
     * These are the hours in which the forecast has been done and the next day.
     */
    private fun fetchNextHoursForecast(){
        val todayCompleteTxt = forecastList?.first()?.dt_txt!!//.substringBefore(" ")
        val todayTxt = todayCompleteTxt.substringBefore(" ")
        val tomorrowTxt = getNextDayOfYearFromTxt(todayTxt)
        forecastList = forecastList?.filter { it.dt_txt?.contains(todayTxt) == true || it.dt_txt?.contains(tomorrowTxt) == true }
    }

}