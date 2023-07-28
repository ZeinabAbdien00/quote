package com.iti.android_4.data.remote.api

import com.iti.android_4.models.Quotes
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface QuoteApi {

    @GET("quotes")
    fun getQuotes(): Call<Quotes>

}