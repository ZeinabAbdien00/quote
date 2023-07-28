package com.iti.android_4.ui.saved

import com.iti.android_4.QuoteApplication
import com.iti.android_4.data.lacal.db.SavedQuotesDatabase

class SavedQuotesRepository {

    private val daoObject = SavedQuotesDatabase.getInstance(QuoteApplication.context).savedDao()

    suspend fun getSavedQuotes() = daoObject.getSavedQuoted()



}