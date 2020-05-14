package com.example.android.sunshine.presentation

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.R
import com.example.android.sunshine.core.domain.ForecastListItem
import kotlinx.android.parcel.Parcelize


class ForecastAdapter(//mHandler: ForecastAdapterOnClickHandler
    private val forecastList : MutableList<ForecastListItem> = mutableListOf(),
    private val itemClickListener : (ForecastListItem) -> Unit
) : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {

    inner class ViewHolder (view: View) : RecyclerView.ViewHolder(view){
       fun bind(forecastListItem: ForecastListItem){
           itemView.findViewById<TextView>(R.id.tv_weather_data).text = forecastListItem.mainInfo?.temperature.toString()
           itemView.setOnClickListener{itemClickListener.invoke(forecastListItem)}
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.forecast_list_item, parent, false))
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

}