package com.iti.android_4.data.lacal.db.dao

import androidx.room.*
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel


@Dao
interface SavedQuoteDao {

    @Query("SELECT * FROM Chats")
    suspend fun getSavedQuoted(): List<SavedQuoteLocalDataModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewQuote(saveNewQuote: SavedQuoteLocalDataModel)

    @Query("DELETE FROM Chats WHERE quote = :quote AND author = :author")
    suspend fun deleteQuote(quote: String, author: String)

}