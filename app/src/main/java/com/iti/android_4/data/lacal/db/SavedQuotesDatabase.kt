package com.iti.android_4.data.lacal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.iti.android_4.data.lacal.db.dao.SavedQuoteDao
import com.iti.android_4.models.saved.SavedQuoteLocalDataModel

@Database(entities = [SavedQuoteLocalDataModel::class], version = 1)
abstract class SavedQuotesDatabase : RoomDatabase() {

    abstract fun savedDao(): SavedQuoteDao

    companion object {
        private var instance: SavedQuotesDatabase? = null

        private const val DB_NAME = "Rooms"

        fun getInstance(context: Context): SavedQuotesDatabase {

            return instance ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context,
                    SavedQuotesDatabase::class.java,
                    DB_NAME
                ).build()
                Companion.instance = instance
                instance
            }
        }

    }

}