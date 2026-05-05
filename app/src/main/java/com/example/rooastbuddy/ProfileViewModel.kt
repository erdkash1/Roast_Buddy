/**
 * ProfileViewModel.kt
 *
 * ViewModel responsible for managing the user's profile preferences.
 * Reads and writes user data (name, brew method, experience level) using
 * Jetpack DataStore for persistent key-value storage across app sessions.
 */


package com.example.rooastbuddy

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = UserPreferences(application)

    val userName: StateFlow<String> = prefs.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val brewMethod: StateFlow<String> = prefs.brewMethod
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val experienceLevel: StateFlow<String> = prefs.experienceLevel
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    val isProfileSet: StateFlow<Boolean> = prefs.userName
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")
        .let { flow ->
            kotlinx.coroutines.flow.combine(flow) { it[0].isNotEmpty() }
                .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
        }

    fun saveProfile(name: String, brew: String, level: String) {
        viewModelScope.launch {
            prefs.saveProfile(name, brew, level)
        }
    }

    fun clearProfile() {
        viewModelScope.launch {
            prefs.clearProfile()
        }
    }
}