package com.example.rooastbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AddEntryForm(onSave: (String, String, Int, String) -> Unit, onCancel: () -> Unit) {
    var coffeeName by remember { mutableStateOf("") }
    var roastLevel by remember { mutableStateOf("Medium") }
    var rating by remember { mutableIntStateOf(3) }
    var notes by remember { mutableStateOf("") }

    Card(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Log a Coffee", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = coffeeName,
                onValueChange = { coffeeName = it },
                label = { Text("Coffee Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            Text("Roast Level")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("Light", "Medium", "Dark").forEach { roast ->
                    FilterChip(
                        selected = roastLevel == roast,
                        onClick = { roastLevel = roast },
                        label = { Text(roast) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))

            Text("Rating: ${"⭐".repeat(rating)}")
            Slider(
                value = rating.toFloat(),
                onValueChange = { rating = it.toInt() },
                valueRange = 1f..5f,
                steps = 3
            )

            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Tasting Notes") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = {
                        if (coffeeName.isNotEmpty()) {
                            onSave(coffeeName, roastLevel, rating, notes)
                        }
                    },
                    modifier = Modifier.weight(1f)
                ) { Text("Save") }

                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) { Text("Cancel") }
            }
        }
    }
}