package com.example.android.sunshine.presentation.city

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.core.domain.forecast.OneDayForecast
import com.example.android.sunshine.databinding.ItemDayForecastBinding


class DayForecastAdapter(
    private val layoutId: Int,
    private val callback: ((OneDayForecast, String) -> Unit)?
) : RecyclerView.Adapter<DayForecastAdapter.ViewHolder>() {

    private var dayList: List<OneDayForecast>? = null

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
        binding.arrowDownButton.setOnClickListener {
            binding.expandableDayDetail.visibility = GONE;
            binding.arrowDownButton.visibility = GONE;
            binding.arrowRightButton.visibility = GONE;
            /*binding.day?.let {
                callback?.invoke(it, "arrowDown")

            }*/
        }

        binding.arrowRightButton.setOnClickListener {
            binding.expandableDayDetail.visibility = VISIBLE;
            binding.arrowDownButton.visibility = GONE;
            binding.arrowRightButton.visibility = GONE;
            /*binding.day?.let {
                callback?.invoke(it, "arrowRight")
            }*/
        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = dayList?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdForPosition(position)

    private fun getLayoutIdForPosition(position: Int) = layoutId

    /**
     * Called whenever the list is updated.
     * Changes are being listened in the view.
     */
    fun update(newForecastList: List<OneDayForecast>) {
        dayList = newForecastList
        notifyDataSetChanged()
    }

}