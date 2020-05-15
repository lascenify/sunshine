package com.example.android.sunshine.presentation.cityMainForecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.BR
import com.example.android.sunshine.core.domain.ForecastListItem
import com.example.android.sunshine.core.domain.OneDayForecast
import com.example.android.sunshine.databinding.ItemDayForecastBinding
import com.example.android.sunshine.utilities.getDayOfWeekFromText
import com.example.android.sunshine.utilities.getNextDayOfYearFromTxt


class DayForecastAdapter(
    private val layoutId: Int
) : RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    private var dayList: MutableList<OneDayForecast>? = null

    inner class ViewHolder(private val binding: ItemDayForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val forecast = dayList?.get(position)
            binding.day = forecast
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemDayForecastBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = if (dayList == null) 0 else dayList!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdForPosition(position)

    private fun getLayoutIdForPosition(position: Int) = layoutId

    /**
     * Called whenever the list is updated.
     * Changes are being listened in the view.
     */
    fun update(newForecastList: List<ForecastListItem>) {
        dayList = mutableListOf()
        fetchNextDaysForecast(newForecastList)
        notifyDataSetChanged()
    }

    /**
     * We only need the next hours to be displayed in this RecyclerView.
     * These are the hours in which the forecast has been done and the next day.
     */
    private fun fetchNextDaysForecast(forecastList: List<ForecastListItem>) {
        var previousDayTxt = forecastList.first().dt_txt!!.substringBefore(" ")

        // Next 4 days
        for (x in 0 until 4){
            val nextDayTxt = getNextDayOfYearFromTxt(previousDayTxt)
            val forecastListFromDay = forecastList.filter { it.dt_txt?.contains(nextDayTxt) == true}
            val oneDayForecast = OneDayForecast(
                getDayOfWeekFromText(nextDayTxt),
                forecastListFromDay,
                null,
                null)
            oneDayForecast.calculateTemperatures()
            dayList?.add(oneDayForecast)
            previousDayTxt = nextDayTxt
        }

    }

}
    /*
    class DayForecastAdapter(//mHandler: ForecastAdapterOnClickHandler
    private val forecastList : MutableList<ForecastListItem> = mutableListOf(),
    private val itemClickListener : (ForecastListItem) -> Unit
) : RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
       fun bind(forecastListItem: ForecastListItem){
           itemView.findViewById<TextView>(R.id.tv_item_day_of_week).text = forecastListItem.mainInfo?.temperature.toString()
           itemView.setOnClickListener{itemClickListener.invoke(forecastListItem)}
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_day_forecast, parent, false))
    }

    override fun getItemCount(): Int = forecastList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    fun update(newForecastList: List<ForecastListItem>){
        forecastList.clear()
        forecastList.addAll(newForecastList)

        notifyDataSetChanged()
    }

}*/