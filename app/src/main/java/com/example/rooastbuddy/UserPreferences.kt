/**
 * UserPreferences.kt
 *
 * Manages persistent user profile data using Jetpack DataStore (Preferences).
 * DataStore provides an asynchronous, consistent key-value storage solution
 * that replaces SharedPreferences with better coroutine and Flow support.
 *
 * Stores: user name, preferred brew method, and coffee experience level.
 */

package com.example.rooastbuddy

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val NAME_KEY = stringPreferencesKey("user_name")
        val BREW_KEY = stringPreferencesKey("brew_method")
        val LEVEL_KEY = stringPreferencesKey("experience_level")
    }

    val userName: Flow<String> = context.dataStore.data.map { it[NAME_KEY] ?: "" }
    val brewMethod: Flow<String> = context.dataStore.data.map { it[BREW_KEY] ?: "" }
    val experienceLevel: Flow<String> = context.dataStore.data.map { it[LEVEL_KEY] ?: "" }

    suspend fun saveProfile(name: String, brew: String, level: String) {
        context.dataStore.edit { prefs ->
            prefs[NAME_KEY] = name
            prefs[BREW_KEY] = brew
            prefs[LEVEL_KEY] = level
        }
    }

    suspend fun clearProfile() {
        context.dataStore.edit { it.clear() }
    }
}