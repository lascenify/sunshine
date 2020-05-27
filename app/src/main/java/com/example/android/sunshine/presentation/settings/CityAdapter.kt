package com.example.android.sunshine.presentation.settings

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.databinding.ItemDaySettingsBinding
import com.example.android.sunshine.framework.db.entities.CityEntity

class CityAdapter(
    private val layoutId: Int,
    private val callback: ((CityEntity) -> Unit)?
) : RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    private var cityList: List<CityEntity>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemDaySettingsBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        binding.root.setOnClickListener {
            binding.city.let {
                callback?.invoke(it!!)
            }

        }
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
            val city = cityList?.get(position)
            binding.city = city
            binding.executePendingBindings()
        }
    }


    fun update(newCitiesList: List<CityEntity>) {
        cityList = newCitiesList
        notifyDataSetChanged()
    }
}
