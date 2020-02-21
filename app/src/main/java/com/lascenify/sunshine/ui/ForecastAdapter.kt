package com.lascenify.sunshine.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.R

class ForecastAdapter(mHandler: ForecastAdapterOnClickHandler) :
    RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder>() {


    interface ForecastAdapterOnClickHandler{
        fun onClick(input:String)
    }

    companion object{
        private var mWeatherData:Array<String?>? = null
       lateinit var mClickHandler  : ForecastAdapterOnClickHandler
    }

    init {
        mClickHandler = mHandler
    }
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ForecastAdapterViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.forecast_list_item, parent, false)
        return ForecastAdapterViewHolder(
            view
        )
    }

    override fun getItemCount(): Int = if (mWeatherData.isNullOrEmpty()) 0 else mWeatherData!!.size

    override fun onBindViewHolder(
        holder: ForecastAdapterViewHolder,
        position: Int
    ) {
        val weatherDataAtPosition = mWeatherData?.get(position)
        holder.mWeatherTextView.text = weatherDataAtPosition
    }

    fun setWeatherData (weatherData:Array<String?>?){
        mWeatherData = weatherData
        notifyDataSetChanged()
    }

    class ForecastAdapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val mWeatherTextView:TextView = itemView.findViewById(R.id.tv_weather_data)
        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val weatherForDay: String = mWeatherData?.get(adapterPosition)!!
            mClickHandler.onClick(weatherForDay)
        }
    }

}