package com.example.rooastbuddy


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface JournalDao {
    // existing journal methods
    @Query("SELECT * FROM journal_entries ORDER BY id DESC")
    fun getAllEntries(): Flow<List<JournalEntry>>

    @Insert
    suspend fun insertEntry(entry: JournalEntry)

    @Delete
    suspend fun deleteEntry(entry: JournalEntry)

    // new wishlist methods
    @Query("SELECT * FROM wishlist")
    fun getWishlist(): Flow<List<WishlistItem>>

    @Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun addToWishlist(item: WishlistItem)

    @Delete
    suspend fun removeFromWishlist(item: WishlistItem)

    @Query("SELECT EXISTS(SELECT 1 FROM wishlist WHERE name = :name)")
    fun isInWishlist(name: String): Flow<Boolean>
}