package com.iti.android_4.data.remote.api

import com.iti.android_4.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private fun instance(): Retrofit =
        Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create()
        ).build()
    fun apiServiceInstance(): QuoteApi = instance().create(QuoteApi::class.java)

}