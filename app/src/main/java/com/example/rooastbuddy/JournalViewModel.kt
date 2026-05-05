/**
 * JournalViewModel.kt
 *
 * ViewModel responsible for managing the Tasting Journal and Wishlist features.
 * Provides reactive StateFlow streams for journal entries and wishlist items,
 * both stored locally using the Room database via JournalDao.
 *
 * Extends AndroidViewModel to access application context for Room database initialization.
 */

package com.example.rooastbuddy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JournalViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).journalDao()

    val entries: StateFlow<List<JournalEntry>> = dao.getAllEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val wishlist: StateFlow<List<WishlistItem>> = dao.getWishlist()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addEntry(coffeeName: String, roastLevel: String, rating: Int, notes: String) {
        viewModelScope.launch {
            dao.insertEntry(
                JournalEntry(
                    coffeeName = coffeeName,
                    roastLevel = roastLevel,
                    rating = rating,
                    notes = notes,
                    date = java.time.LocalDate.now().toString()
                )
            )
        }
    }

    fun deleteEntry(entry: JournalEntry) {
        viewModelScope.launch { dao.deleteEntry(entry) }
    }

    fun addToWishlist(coffee: Coffee) {
        viewModelScope.launch {
            dao.addToWishlist(
                WishlistItem(
                    name = coffee.name,
                    roast = coffee.roast,
                    notes = coffee.notes,
                    description = coffee.description
                )
            )
        }
    }

    fun removeFromWishlist(item: WishlistItem) {
        viewModelScope.launch { dao.removeFromWishlist(item) }
    }

    fun isInWishlist(name: String): Flow<Boolean> = dao.isInWishlist(name)
}