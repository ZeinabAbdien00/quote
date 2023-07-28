package com.iti.android_4.ui.saved

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel
import kotlinx.coroutines.launch

class SavedQuoteViewModel(
    private val quotesRepository: SavedQuotesRepository
) : ViewModel() {


    private val _savedQuotes: MutableLiveData<List<SavedQuoteLocalDataModel>> = MutableLiveData()
    val savedQuotes: MutableLiveData<List<SavedQuoteLocalDataModel>> = _savedQuotes


    fun getQuotess() {
        viewModelScope.launch {
            savedQuotes()
        }
    }


    private suspend fun savedQuotes() {

        _savedQuotes.postValue(quotesRepository.getSavedQuotes())
        _savedQuotes.value = quotesRepository.getSavedQuotes()
        Log.d("suz", savedQuotes.value.toString() + " savedQuotes")
        Log.d("suz", quotesRepository.getSavedQuotes().toString() + " getSavedQuotes")

    }


}
