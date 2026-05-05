/**
 * CoffeeViewModel.kt
 *
 * ViewModel responsible for managing the coffee catalog and quiz recommendation logic.
 * Handles fetching the artisanal coffee catalog from Firebase Firestore and
 * generating personalized coffee recommendations based on quiz answers.
 *
 * Uses StateFlow for reactive UI updates and coroutines for async operations.
 */

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
            delay(1500)

            val brewMethod = quizAnswers.getOrElse(0) { "Pour-Over" }
            val flavor = quizAnswers.getOrElse(1) { "Bright & Fruity" }
            val strength = quizAnswers.getOrElse(2) { "Balanced" }

            val recommendation = when {
                flavor == "Bright & Fruity" && strength == "Light & Delicate" ->
                    "We recommend Ethiopian Yirgacheffe (Light Roast)! Your love of $brewMethod and bright fruity flavors makes this the perfect match — expect vibrant citrus and floral notes that shine best at a lighter roast level."
                flavor == "Chocolate & Nutty" && strength == "Balanced" ->
                    "We recommend Colombian Supremo (Medium Roast)! Your $brewMethod preference pairs beautifully with this balanced coffee — rich chocolate and caramel notes with a smooth, clean finish every time."
                flavor == "Earthy & Bold" || strength == "Extra Bold" ->
                    "We recommend Sumatra Mandheling (Dark Roast)! Bold and intense is clearly your style — this dark roast delivers deep earthy flavors and a full body that stands up perfectly to $brewMethod brewing."
                flavor == "Sweet & Floral" ->
                    "We recommend Kenya AA (Light Roast)! Your preference for sweet floral notes and $brewMethod brewing makes this East African gem ideal — expect a bright berry sweetness with a clean winey finish."
                strength == "Strong & Intense" ->
                    "We recommend Brazil Santos (Dark Roast)! Your $brewMethod setup and love of strong coffee makes this smooth dark roast perfect — walnut and dark chocolate notes with low acidity for an intense but never bitter cup."
                else ->
                    "We recommend Guatemala Antigua (Medium Roast)! A versatile choice for $brewMethod lovers who enjoy $flavor flavors — rich cocoa and brown sugar with a hint of cinnamon spice make this a crowd favorite."
            }

            _recommendation.value = recommendation
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