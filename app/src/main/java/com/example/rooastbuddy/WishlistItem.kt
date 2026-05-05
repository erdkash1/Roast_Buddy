/**
 * WishlistItem.kt
 *
 * Room database entity representing a coffee saved to the user's wishlist.
 * The coffee name is used as the primary key to prevent duplicate entries.
 * Wishlist items mirror the Coffee data from the Firestore catalog.
 */

package com.example.rooastbuddy


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wishlist")
data class WishlistItem(
    @PrimaryKey val name: String,
    val roast: String,
    val notes: String,
    val description: String
)