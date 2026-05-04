package com.example.rooastbuddy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
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
fun ProfileScreen(navController: NavController) {
    val profileViewModel: ProfileViewModel = viewModel()
    val userName by profileViewModel.userName.collectAsState()
    val brewMethod by profileViewModel.brewMethod.collectAsState()
    val experienceLevel by profileViewModel.experienceLevel.collectAsState()

    var editMode by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(userName) }
    var newBrew by remember { mutableStateOf(brewMethod) }
    var newLevel by remember { mutableStateOf(experienceLevel) }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) { Text("< Back") }
            Text(
                "My Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))

            // Avatar
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("☕", fontSize = 48.sp)
            }

            Spacer(Modifier.height(16.dp))

            if (!editMode) {
                Text(userName, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text(experienceLevel, color = MaterialTheme.colorScheme.primary, fontSize = 16.sp)
                Text("Prefers $brewMethod", color = MaterialTheme.colorScheme.secondary, fontSize = 14.sp)

                Spacer(Modifier.height(32.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Profile Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                        Spacer(Modifier.height(12.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Name", color = MaterialTheme.colorScheme.secondary)
                            Text(userName, fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Brew Method", color = MaterialTheme.colorScheme.secondary)
                            Text(brewMethod, fontWeight = FontWeight.Medium)
                        }
                        Spacer(Modifier.height(8.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Experience", color = MaterialTheme.colorScheme.secondary)
                            Text(experienceLevel, fontWeight = FontWeight.Medium)
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        newName = userName
                        newBrew = brewMethod
                        newLevel = experienceLevel
                        editMode = true
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text("Edit Profile") }

            } else {
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                Spacer(Modifier.height(16.dp))
                Text("Brew Method", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                androidx.compose.foundation.lazy.LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(listOf("Pour-Over", "French Press", "Espresso", "Drip")) { brew ->
                        FilterChip(
                            selected = newBrew == brew,
                            onClick = { newBrew = brew },
                            label = { Text(brew) }
                        )
                    }
                }
                Spacer(Modifier.height(16.dp))
                Text("Experience Level", fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("Beginner", "Intermediate", "Expert").forEach { level ->
                        FilterChip(
                            selected = newLevel == level,
                            onClick = { newLevel = level },
                            label = { Text(level) }
                        )
                    }
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (newName.isNotEmpty()) {
                            profileViewModel.saveProfile(newName, newBrew, newLevel)
                            editMode = false
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text("Save Changes") }
                Spacer(Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { editMode = false },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) { Text("Cancel") }
            }
        }
    }
}