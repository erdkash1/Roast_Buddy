/**
 * ProfileSetupScreen.kt
 *
 * Onboarding screen shown on the app's first launch when no profile exists.
 * Collects the user's name, preferred brew method, and coffee experience level.
 * Saves the data to DataStore via ProfileViewModel and navigates to HomeScreen.
 * The setup screen is never shown again after the profile is saved.
 */

package com.example.rooastbuddy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun ProfileSetupScreen(navController: NavController) {
    val profileViewModel: ProfileViewModel = viewModel()
    var name by remember { mutableStateOf("") }
    var selectedBrew by remember { mutableStateOf("Pour-Over") }
    var selectedLevel by remember { mutableStateOf("Beginner") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("☕", fontSize = 64.sp)
        Spacer(Modifier.height(16.dp))
        Text(
            "Welcome to Roast Buddy!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Let's set up your coffee profile",
            fontSize = 15.sp,
            color = MaterialTheme.colorScheme.secondary,
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
        )
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            leadingIcon = { Text("👤") }
        )

        Spacer(Modifier.height(20.dp))
        Text(
            "Preferred Brew Method",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        androidx.compose.foundation.lazy.LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("Pour-Over", "French Press", "Espresso", "Drip")) { brew ->
                FilterChip(
                    selected = selectedBrew == brew,
                    onClick = { selectedBrew = brew },
                    label = { Text(brew) }
                )
            }
        }

        Spacer(Modifier.height(20.dp))
        Text(
            "Experience Level",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Beginner", "Intermediate", "Expert").forEach { level ->
                FilterChip(
                    selected = selectedLevel == level,
                    onClick = { selectedLevel = level },
                    label = { Text(level) }
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Button(
            onClick = {
                if (name.isNotEmpty()) {
                    profileViewModel.saveProfile(name, selectedBrew, selectedLevel)
                    navController.navigate("home") {
                        popUpTo("setup") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Text("Let's Go! ☕", fontSize = 16.sp)
        }
    }
}