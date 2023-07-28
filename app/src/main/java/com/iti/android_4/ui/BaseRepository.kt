package com.iti.android_4.ui

import com.iti.android_4.QuoteApplication
import com.iti.android_4.data.lacal.db.SavedQuotesDatabase
import com.iti.android_4.data.remote.api.RetrofitClient
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel

open class BaseRepository {

    companion object {
        private val daoObject = SavedQuotesDatabase.getInstance(QuoteApplication.context).savedDao()
    }

    fun getRetrofitQuotes() =
        getQuotes()

    private fun getQuotes() =
        RetrofitClient.apiServiceInstance().getQuotes()

    suspend fun insertNewQuotes(newQuote: SavedQuoteLocalDataModel) =
        daoObject.insertNewQuote(saveNewQuote = newQuote)

    suspend fun deleteQuotes(quote: String, author: String) =
        daoObject.deleteQuote(quote = quote, author = author)

    suspend fun getSavedQuotes() = daoObject.getSavedQuoted()

}