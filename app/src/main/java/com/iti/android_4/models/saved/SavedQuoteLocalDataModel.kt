package com.iti.android_4.models.saved

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Chats")
data class SavedQuoteLocalDataModel(

    @PrimaryKey(autoGenerate = true)
    val quoteId: Int = 0,

    @ColumnInfo(name = "quote")
    val quote: String,

    @ColumnInfo(name = "author")
    val author: String

)