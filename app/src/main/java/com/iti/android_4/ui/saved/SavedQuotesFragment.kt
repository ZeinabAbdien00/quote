package com.iti.android_4.ui.saved

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.android_4.adapter.save_quote.SaveQuoteRVAdapter
import com.iti.android_4.databinding.FragmentSavedQuotesBinding
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel
import com.iti.android_4.ui.BaseRepository


class SavedQuotesFragment : Fragment() {
    private lateinit var binding: FragmentSavedQuotesBinding
    private lateinit var viewModel: SavedQuoteViewModel
    private lateinit var repository: BaseRepository
    private lateinit var savedAdapter: SaveQuoteRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedQuotesBinding.inflate(layoutInflater)
        repository = BaseRepository()
        viewModel = SavedQuoteViewModel(repository)
        viewModel.getSavedQuotes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observation()
        viewModel.getSavedQuotes()
    }


    private fun observation() {
        viewModel.getSavedQuotes()
        viewModel.savedQuotes.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setupRecyclerView(it)
            }
        })
    }

    private fun setupRecyclerView(savedQuoteList: List<SavedQuoteLocalDataModel>) {
        binding.rvSavedQuotes.apply {
            val savedData = savedQuoteList as ArrayList<SavedQuoteLocalDataModel>
            savedAdapter =
                SaveQuoteRVAdapter(savedData)

            val messageLayoutManager =
                LinearLayoutManager(requireContext(), GridLayoutManager.VERTICAL, false)

            layoutManager = messageLayoutManager
            adapter = savedAdapter
        }
    }
}