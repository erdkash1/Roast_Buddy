/**
 * AppDatabase.kt
 *
 * Room database definition for the Roast Buddy app.
 * Manages local persistence for tasting journal entries and wishlist items.
 * Uses a singleton pattern to ensure only one database instance exists.
 *
 * Database version: 2 (added WishlistItem table in v2)
 */


package com.example.rooastbuddy

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [JournalEntry::class, WishlistItem::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun journalDao(): JournalDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "roastbuddy_db"
                )
                    .fallbackToDestructiveMigration()
                    .build().also { INSTANCE = it }
            }
        }
    }
}