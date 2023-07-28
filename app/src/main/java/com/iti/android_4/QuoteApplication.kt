package com.iti.android_4

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class QuoteApplication : Application() {


    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        @SuppressLint("StaticFieldLeak")
        lateinit var instance: QuoteApplication
            private set

    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        instance = this
    }


}