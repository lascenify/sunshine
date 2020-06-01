package com.example.android.sunshine.presentation.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.core.domain.search.SearchResponse
import com.example.android.sunshine.core.domain.search.SearchResult
import com.example.android.sunshine.databinding.ItemDayForecastBinding
import com.example.android.sunshine.databinding.ItemSearchResultBinding

class SearchResultAdapter (
    private val layoutId: Int,
    private val callback: ((SearchResult) -> Unit)?
) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>(){
    private var resultList: List<SearchResult>? = null

    inner class ViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int){
            val result = resultList?.get(position)
            binding.result = result
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSearchResultBinding =
            DataBindingUtil.inflate(layoutInflater, viewType, parent, false)
        binding.root.setOnClickListener {
            binding.result.let {
                callback?.invoke(it!!)
            }

        }
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = resultList?.size ?: 0


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemViewType(position: Int): Int = getLayoutIdForPosition(position)

    private fun getLayoutIdForPosition(position: Int) = layoutId

    fun update(newList: List<SearchResult>){
        resultList = newList
        notifyDataSetChanged()
    }
}