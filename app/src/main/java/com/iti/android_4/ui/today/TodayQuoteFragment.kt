package com.iti.android_4.ui.today

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.iti.android_4.R
import com.iti.android_4.databinding.FragmentTodayQuoteBinding
import com.iti.android_4.models.Quotes
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random


class TodayQuoteFragment : Fragment() {

    private lateinit var binding: FragmentTodayQuoteBinding
    private lateinit var viewModel: TodayQuotesViewModel
    private lateinit var repository: TodayQuotesRepository
    private var toggle: Boolean = true
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTodayQuoteBinding.inflate(layoutInflater)
        repository = TodayQuotesRepository()
        viewModel = TodayQuotesViewModel(Application(), repository)
        sharedPreferences = activity!!.getSharedPreferences("MyShared" , Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
        observation()
        onClickToSaveFavorite()

    }

    private fun init() {
        toggle = if(sharedPreferences.getBoolean("favorite" , false)){
            binding.todayLayout.btnFavorite.setImageResource(R.drawable.ic_favorite)
            false
        } else{
            binding.todayLayout.btnFavorite.setImageResource(R.drawable.ic_favorite_border)
            true

        }
    }

    private fun observation() {
        viewModel.getBreakingNews()
        viewModel.quotes.observe(viewLifecycleOwner, Observer { response ->
            if (response != null) {
                binding.todayLayout.tvQuoteContent.text = response.results[Random.nextInt(20)].content.toString()
                binding.todayLayout.tvAuthor.text = response.results[Random.nextInt(20)].author.toString()
            }
            Log.d("suzza" , response.toString())
            setOnClick(response)
        })
    }

    private fun setOnClick(response: Quotes?) {
        binding.todayLayout.apply {

            btnNext.setOnClickListener {
                tvQuoteContent.text = response!!.results[Random.nextInt(20)].content.toString()
                tvAuthor.text = response.results[Random.nextInt(20)].author.toString()
            }
            btnPrevious.setOnClickListener {
                tvQuoteContent.text = response!!.results[Random.nextInt(20)].content.toString()
                tvAuthor.text = response.results[Random.nextInt(20)].author.toString()
            }

        }
    }

    private fun onClickToSaveFavorite() {
        binding.todayLayout.apply {
            btnFavorite.setOnClickListener {
                toggle = if (!toggle) {
                    btnFavorite.setImageResource(R.drawable.ic_favorite_border)
                    !toggle
                } else {
                    btnFavorite.setImageResource(R.drawable.ic_favorite)
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.newQuote(
                            SavedQuoteLocalDataModel(
                                quote = tvQuoteContent.text.toString(),
                                author = tvAuthor.text.toString()
                            )
                        )
                    }
                    editor.putBoolean("favorite" , toggle)
                    editor.apply()
                    !toggle
                }
            }
        }
    }

}