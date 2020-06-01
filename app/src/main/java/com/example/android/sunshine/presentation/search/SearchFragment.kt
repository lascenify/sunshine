package com.example.android.sunshine.presentation.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.sunshine.R
import com.example.android.sunshine.core.interactors.SearchCity
import com.example.android.sunshine.databinding.SearchFragmentBinding
import com.example.android.sunshine.presentation.MainActivity
import com.example.android.sunshine.presentation.viewmodel.CitiesViewModel
import com.example.android.sunshine.presentation.viewmodel.SearchViewModel
import javax.inject.Inject

class SearchFragment : Fragment(){

    private lateinit var binding: SearchFragmentBinding

    @Inject
    lateinit var viewModel: SearchViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (context as MainActivity).searchComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupResponseRecyclerView()
        setListeners()


    }

    private fun setupResponseRecyclerView(){
        val adapter = SearchResultAdapter(R.layout.item_search_result){ result ->
            viewModel.forecastCity(result).observe(viewLifecycleOwner, Observer { result ->
                if (result.status.isSuccessful())
                    findNavController().popBackStack()
            })
            //findNavController().popBackStack()
        }
        binding.recyclerViewSearchResults.adapter = adapter
        viewModel.cities.observe(viewLifecycleOwner, Observer { result ->
            if (result != null){
                if (result.status.isSuccessful()){
                    adapter.update(result.data?.hits!!)
                }
            }
        })
    }

    private fun setListeners() {
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.isNotEmpty() == true && newText.count() > 3) {
                    viewModel.setSearchParams(SearchCity.Params(newText))
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null)
                    viewModel.setSearchParams(SearchCity.Params(query))
                return true
            }
        })
    }
}