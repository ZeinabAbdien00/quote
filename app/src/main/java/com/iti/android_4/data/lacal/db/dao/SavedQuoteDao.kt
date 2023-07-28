package com.iti.android_4.data.lacal.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel


@Dao
interface SavedQuoteDao {

    @Query("SELECT * FROM Chats")
    suspend fun getSavedQuoted(): List<SavedQuoteLocalDataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewQuote(saveNewQuote: SavedQuoteLocalDataModel)

}