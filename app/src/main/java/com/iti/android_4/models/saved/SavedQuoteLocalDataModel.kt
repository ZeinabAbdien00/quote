package com.iti.android_4.models.saved

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Chats")
data class SavedQuoteLocalDataModel(

    @ColumnInfo(name = "quote")
    @PrimaryKey(autoGenerate = false)
    val quote: String,

    @ColumnInfo(name = "author")
    val author: String,

    @ColumnInfo(name = "date")
    val date: String

)