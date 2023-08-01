package com.iti.android_4.data.remote.api

import com.iti.android_4.models.quotes.Quotes
import com.iti.android_4.models.search.SearchQuotes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface QuoteApi {

    @GET("quotes")
    fun getQuotes(): Call<Quotes>

    @GET("search/quotes")
    fun getSearchQuotes(@Query("query") searchText: String): Call<SearchQuotes>

}