package com.iti.android_4.ui.today

import com.iti.android_4.QuoteApplication
import com.iti.android_4.data.lacal.db.SavedQuotesDatabase
import com.iti.android_4.data.remote.api.RetrofitClient
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel

class TodayQuotesRepository {

    private val daoObject = SavedQuotesDatabase.getInstance(QuoteApplication.context).savedDao()


    fun getQuotes() =
        RetrofitClient.apiServiceInstance().getQuotes()

    suspend fun insertNewQuotes(newQuote: SavedQuoteLocalDataModel) =
        daoObject.insertNewQuote(saveNewQuote = newQuote)

    suspend fun deleteQuotes(quote: String, author: String) =
        daoObject.deleteQuote(quote = quote, author = author)

}