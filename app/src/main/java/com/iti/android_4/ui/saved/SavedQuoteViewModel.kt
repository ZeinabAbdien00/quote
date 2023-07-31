package com.iti.android_4.ui.saved

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel
import com.iti.android_4.ui.BaseRepository
import kotlinx.coroutines.launch

class SavedQuoteViewModel(
    private val savedRepository: BaseRepository
) : ViewModel() {

    private val _savedQuotes: MutableLiveData<List<SavedQuoteLocalDataModel>> = MutableLiveData()
    val savedQuotes: MutableLiveData<List<SavedQuoteLocalDataModel>> = _savedQuotes

    fun getSavedQuotes() {
        viewModelScope.launch {
            savedQuotes()
        }
    }

    private suspend fun savedQuotes() {

        _savedQuotes.postValue(savedRepository.getSavedQuotes())
        _savedQuotes.value = savedRepository.getSavedQuotes()

    }

    suspend fun deleteQuote(quote: String, author: String) {
        savedRepository.deleteQuotes(quote = quote, author = author)
    }

}
