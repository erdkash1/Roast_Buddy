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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun StatsScreen(navController: NavController) {
    val viewModel: JournalViewModel = viewModel()
    val entries by viewModel.entries.collectAsState()

    val totalCoffees = entries.size
    val avgRating = if (entries.isEmpty()) 0f else entries.map { it.rating }.average().toFloat()
    val favRoast = entries.groupBy { it.roastLevel }
        .maxByOrNull { it.value.size }?.key ?: "None yet"
    val topRated = entries.maxByOrNull { it.rating }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) { Text("< Back") }
            Text(
                "My Coffee Stats",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (entries.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No stats yet. Start logging coffees in your journal!")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {

                // Summary cards
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Total coffees
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("☕", fontSize = 32.sp)
                                Text(
                                    "$totalCoffees",
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Coffees Tried", fontSize = 12.sp)
                            }
                        }

                        // Average rating
                        Card(
                            modifier = Modifier.weight(1f),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("⭐", fontSize = 32.sp)
                                Text(
                                    "%.1f".format(avgRating),
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text("Avg Rating", fontSize = 12.sp)
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // Favorite roast
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Favorite Roast Level", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(favRoast, fontSize = 22.sp, color = MaterialTheme.colorScheme.primary)
                            Text(
                                "Based on ${entries.count { it.roastLevel == favRoast }} entries",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                }

                // Top rated coffee
                item {
                    if (topRated != null) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Highest Rated Coffee", fontWeight = FontWeight.Bold)
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    topRated.coffeeName,
                                    fontSize = 20.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text("⭐".repeat(topRated.rating), fontSize = 18.sp)
                                Text(
                                    topRated.notes,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                            }
                        }
                        Spacer(Modifier.height(12.dp))
                    }
                }

                // Roast breakdown
                item {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Roast Breakdown", fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            listOf("Light", "Medium", "Dark").forEach { roast ->
                                val count = entries.count { it.roastLevel == roast }
                                val percent = if (totalCoffees > 0) (count * 100 / totalCoffees) else 0
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        roast,
                                        modifier = Modifier.width(60.dp),
                                        fontSize = 14.sp
                                    )
                                    LinearProgressIndicator(
                                        progress = { percent / 100f },
                                        modifier = Modifier.weight(1f).height(8.dp),
                                        color = when (roast) {
                                            "Light" -> MaterialTheme.colorScheme.tertiary
                                            "Medium" -> MaterialTheme.colorScheme.secondary
                                            else -> MaterialTheme.colorScheme.error
                                        }
                                    )
                                    Text(
                                        " $count",
                                        fontSize = 14.sp,
                                        modifier = Modifier.padding(start = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}