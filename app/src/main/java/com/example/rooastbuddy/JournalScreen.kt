/**
 * JournalScreen.kt
 *
 * Displays the user's tasting journal — a log of coffees they have tried.
 * Each entry shows the coffee name, roast level, star rating, tasting notes, and date.
 * Users can add new entries via the AddEntryForm which appears inline at the top.
 * All entries are persisted locally using the Room database.
 */

package com.example.rooastbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun JournalScreen(navController: NavController) {
    val viewModel: JournalViewModel = viewModel()
    val entries by viewModel.entries.collectAsState()
    var showForm by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextButton(onClick = { navController.popBackStack() }) { Text("< Back") }
            Text("Tasting Journal", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            TextButton(onClick = { showForm = true }) { Text("+ Add") }
        }

        if (showForm) {
            AddEntryForm(
                onSave = { name, roast, rating, notes ->
                    viewModel.addEntry(name, roast, rating, notes)
                    showForm = false
                },
                onCancel = { showForm = false }
            )
        }

        if (entries.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No entries yet. Tap + Add to log a coffee!")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(entries) { entry ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(entry.coffeeName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text(entry.date, fontSize = 12.sp, color = MaterialTheme.colorScheme.secondary)
                            }
                            Text("${entry.roastLevel} Roast", color = MaterialTheme.colorScheme.primary)
                            Text("⭐".repeat(entry.rating), fontSize = 18.sp)
                            if (entry.notes.isNotEmpty()) {
                                Spacer(Modifier.height(4.dp))
                                Text(entry.notes, fontSize = 14.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}
