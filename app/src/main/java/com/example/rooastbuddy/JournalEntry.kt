/**
 * JournalEntry.kt
 *
 * Room database entity representing a single tasting journal entry.
 * Each entry captures the user's experience with a specific coffee,
 * including a star rating, personal notes, and the date logged.
 */
package com.example.rooastbuddy

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coffeeName: String,
    val roastLevel: String,
    val rating: Int,
    val notes: String,
    val date: String
)