package com.iti.android_4.ui.search

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android_4.models.search.SearchQuotes
import com.iti.android_4.ui.BaseRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchQuoteViewModel(
    private val searchRepository: BaseRepository
) : ViewModel() {

    private val _searchQuotes: MutableLiveData<SearchQuotes> = MutableLiveData()
    val searchQuotes: MutableLiveData<SearchQuotes> = _searchQuotes

    fun getSearchQuotes(text: String) = viewModelScope.launch {
        safeSearchQuotesCall(text = text)
    }

    private fun safeSearchQuotesCall(text: String) {
        val call = searchRepository.getRetrofitSearchQuotes(text = text)
        call.enqueue(object : Callback<SearchQuotes> {
            override fun onResponse(
                call: Call<SearchQuotes>,
                response: Response<SearchQuotes>
            ) {
                searchQuotes.postValue(response.body())
                searchQuotes.value = response.body()
            }

            override fun onFailure(call: Call<SearchQuotes>, t: Throwable) {
                Log.d("suz", "onFailure: ${t.message}")
            }
        })
    }

}