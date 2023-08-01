package com.iti.android_4.ui

import android.util.Log
import com.iti.android_4.QuoteApplication
import com.iti.android_4.data.lacal.db.SavedQuotesDatabase
import com.iti.android_4.data.remote.api.RetrofitClient
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel
import com.iti.android_4.models.search.SearchQuotes
import retrofit2.Call

open class BaseRepository {

    companion object {
        private val daoObject = SavedQuotesDatabase.getInstance(QuoteApplication.context).savedDao()
        private val retrofitObject = RetrofitClient.apiServiceInstance()
    }

    fun getRetrofitQuotes() =
        getQuotes()
    fun getRetrofitSearchQuotes(text : String) =
        getSearchQuotes(text)

    private fun getQuotes() =
        retrofitObject.getQuotes()

    private fun getSearchQuotes(text : String) : Call<SearchQuotes> {
        Log.d("nop", retrofitObject.getSearchQuotes(text).toString())
       return retrofitObject.getSearchQuotes(text)

    }


    suspend fun insertNewQuotes(newQuote: SavedQuoteLocalDataModel) =
        daoObject.insertNewQuote(saveNewQuote = newQuote)

    suspend fun deleteQuotes(quote: String, author: String) =
        daoObject.deleteQuote(quote = quote, author = author)

    suspend fun getSavedQuotes() = daoObject.getSavedQuoted()

}