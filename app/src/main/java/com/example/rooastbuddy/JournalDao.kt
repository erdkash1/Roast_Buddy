package com.example.rooastbuddy


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    @Query("SELECT * FROM journal_entries ORDER BY id DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Insert
    suspend fun insertEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)
}