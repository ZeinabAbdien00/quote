package com.iti.android_4.ui.saved

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.iti.android_4.databinding.FragmentSavedQuotesBinding
import com.iti.android_4.ui.BaseRepository


class SavedQuotesFragment : Fragment() {
    private lateinit var binding: FragmentSavedQuotesBinding
    private lateinit var viewModel: SavedQuoteViewModel
    private lateinit var repository: BaseRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedQuotesBinding.inflate(layoutInflater)
        repository = BaseRepository()
        viewModel = SavedQuoteViewModel(repository)
        Log.d("suz", "1")

        viewModel.getRetrofitQuotes()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observation()

    }


    private fun observation() {
        viewModel.savedQuotes.observe(viewLifecycleOwner, Observer {

//            if (it != null) {
//                viewModel.getRetrofitQuotes()
//                try {
//                    binding.tvvvvvv.text = it[0].quote.toString()
//
//                } catch (e: Exception) {
//                    binding.tvvvvvv.text = "nothing"
//                }
//            }
        })
    }

}