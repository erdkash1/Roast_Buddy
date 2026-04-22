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