package com.example.rooastbuddy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class Coffee(
    val name: String = "",
    val roast: String = "",
    val notes: String = "",
    val description: String = ""
)

class CoffeeViewModel : ViewModel() {

    val quizAnswers = mutableListOf<String>()

    private val _recommendation = MutableStateFlow<String?>(null)
    val recommendation: StateFlow<String?> = _recommendation

    private val _isLoadingRec = MutableStateFlow(false)
    val isLoadingRec: StateFlow<Boolean> = _isLoadingRec

    private val _catalog = MutableStateFlow<List<Coffee>>(emptyList())
    val catalog: StateFlow<List<Coffee>> = _catalog

    private val _isLoadingCatalog = MutableStateFlow(false)
    val isLoadingCatalog: StateFlow<Boolean> = _isLoadingCatalog

    fun fetchRecommendation() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoadingRec.value = true
            _recommendation.value = null

            // TEMPORARY MOCK - simulates real API call
            delay(1500)
            _recommendation.value = "We recommend Ethiopian Yirgacheffe (Light Roast)! " +
                    "Because you prefer ${quizAnswers.getOrElse(0) { "Pour-Over" }} and enjoy " +
                    "${quizAnswers.getOrElse(1) { "bright fruity" }} flavors, this light roast " +
                    "perfectly highlights delicate citrus and floral notes without any overwhelming " +
                    "bitterness. It's your ideal cup!"
            _isLoadingRec.value = false
        }
    }

    fun fetchCatalog() {
        _isLoadingCatalog.value = true
        val db = Firebase.firestore

        db.collection("coffees")
            .get()
            .addOnSuccessListener { result ->
                _catalog.value = result.documents.map { doc ->
                    Coffee(
                        name = doc.getString("name") ?: "",
                        roast = doc.getString("roast") ?: "",
                        notes = doc.getString("notes") ?: "",
                        description = doc.getString("description") ?: ""
                    )
                }
                _isLoadingCatalog.value = false
            }
            .addOnFailureListener {
                _isLoadingCatalog.value = false
            }
    }
}