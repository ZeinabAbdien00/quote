package com.iti.android_4.ui.today

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android_4.models.quotes.Quotes
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel
import com.iti.android_4.ui.BaseRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TodayQuotesViewModel(
    private val todayRepository: BaseRepository
) : ViewModel() {

    val quotes: MutableLiveData<Quotes> = MutableLiveData()

    fun getQuotes() = viewModelScope.launch {
        safeQuotesCall()
    }

    private fun safeQuotesCall() {

        val call = todayRepository.getRetrofitQuotes()
        call.enqueue(object : Callback<Quotes> {
            override fun onResponse(
                call: Call<Quotes>,
                response: Response<Quotes>
            ) {
                quotes.postValue(response.body())
            }

            override fun onFailure(call: Call<Quotes>, t: Throwable) {
                Log.d("suz", "onFailure: ${t.message}")
            }
        })
    }

    suspend fun newQuote(newQuotes: SavedQuoteLocalDataModel) {
        todayRepository.insertNewQuotes(newQuotes)
    }

    suspend fun deleteQuote(quote: String, author: String) {
        todayRepository.deleteQuotes(quote = quote, author = author)
    }

    private val _savedQuotes: MutableLiveData<List<SavedQuoteLocalDataModel>> = MutableLiveData()
    val savedQuotes: MutableLiveData<List<SavedQuoteLocalDataModel>> = _savedQuotes

    fun getSavedQuotes() {
        viewModelScope.launch {
            savedQuotes()
        }
    }

    private suspend fun savedQuotes() {
        _savedQuotes.value = todayRepository.getSavedQuotes()
    }

}