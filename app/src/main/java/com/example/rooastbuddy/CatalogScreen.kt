package com.example.rooastbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController

@Composable
fun CatalogScreen(navController: NavController, viewModel: CoffeeViewModel) {
    val catalog by viewModel.catalog.collectAsState()
    val isLoading by viewModel.isLoadingCatalog.collectAsState()
    var selectedRoast by remember { mutableStateOf("All") }
    var selectedCoffee by remember { mutableStateOf<Coffee?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchCatalog()
    }

    // Show detail screen if a coffee is selected
    if (selectedCoffee != null) {
        CoffeeDetailScreen(
            coffee = selectedCoffee!!,
            onBack = { selectedCoffee = null }
        )
        return
    }

    val filtered = if (selectedRoast == "All") catalog
    else catalog.filter { it.roast == selectedRoast }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.popBackStack() }) { Text("< Back") }
                Text(
                    "Curated Roasts",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Filter chips
        item {
            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(listOf("All", "Light", "Medium", "Dark")) { roast ->
                    FilterChip(
                        selected = selectedRoast == roast,
                        onClick = { selectedRoast = roast },
                        label = { Text(roast) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // Loading or empty state
        if (isLoading) {
            item {
                Box(
                    Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            }
        } else if (filtered.isEmpty()) {
            item {
                Box(
                    Modifier.fillMaxWidth().height(300.dp),
                    contentAlignment = Alignment.Center
                ) { Text("No coffees found.") }
            }
        } else {
            items(filtered) { coffee ->
                CoffeeCard(coffee = coffee, onClick = { selectedCoffee = coffee })
            }
        }
    }
}