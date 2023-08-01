package com.iti.android_4.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.android_4.adapter.search_quote.SearchQuoteRVAdapter
import com.iti.android_4.databinding.FragmentSearchBinding
import com.iti.android_4.models.search.SearchQuotes
import com.iti.android_4.ui.BaseRepository
import com.iti.android_4.models.quotes.Result


class SearchQuoteFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewModel: SearchQuoteViewModel
    private lateinit var repository: BaseRepository
    private lateinit var searchAdapter: SearchQuoteRVAdapter

    private lateinit var data: SearchQuotes


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        repository = BaseRepository()
        viewModel = SearchQuoteViewModel(repository)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTestListeners()
    }

    private fun editTestListeners() {
        binding.etSearch.addTextChangedListener {
            observation(binding.etSearch.text.toString())
        }
    }

    private fun observation(text: String) {
        viewModel.getSearchQuotes(text)
        viewModel.searchQuotes.observe(viewLifecycleOwner, Observer { response ->
            Log.d("mahmoud", viewModel.searchQuotes.value.toString())

            if (response != null) {
                Log.d("suzann", response.toString())
                setupRecyclerView(response)
            }


        })
    }

    private fun setupRecyclerView(searchQuoteResponse: SearchQuotes) {
        binding.rvSearchQuotes.apply {
            val savedData = searchQuoteResponse.results as ArrayList<Result>
            searchAdapter =
                SearchQuoteRVAdapter(savedData)

            val messageLayoutManager =
                LinearLayoutManager(requireContext(), GridLayoutManager.VERTICAL, false)

            layoutManager = messageLayoutManager
            adapter = searchAdapter
        }
    }

}