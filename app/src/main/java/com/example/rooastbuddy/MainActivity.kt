/**
 * MainActivity.kt
 *
 * The single entry point of the Roast Buddy Android application.
 * Sets up the Compose UI, applies the custom theme, and hosts the
 * NavHost which manages navigation between all app screens.
 *
 * Architecture: Single-Activity with Jetpack Compose + Navigation Compose
 */

package com.example.rooastbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rooastbuddy.ui.theme.ROoastBuddyTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ROoastBuddyTheme {
                RoastBuddyApp()
            }
        }
    }
}


@Composable
fun RoastBuddyApp() {
    val navController = rememberNavController()
    val viewModel: CoffeeViewModel = viewModel()
    val profileViewModel: ProfileViewModel = viewModel()
    val userName by profileViewModel.userName.collectAsState()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Roast Buddy", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    if (userName.isNotEmpty()) {
                        TextButton(onClick = { navController.navigate("profile") }) {
                            Text("👤 ${userName.split(" ").first()}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (userName.isEmpty()) "setup" else "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("setup")     { ProfileSetupScreen(navController) }
            composable("home")      { HomeScreen(navController, profileViewModel) }
            composable("quiz")      { QuizScreen(navController, viewModel) }
            composable("result")    { ResultScreen(navController, viewModel) }
            composable("catalog")   { CatalogScreen(navController, viewModel) }
            composable("journal")   { JournalScreen(navController) }
            composable("education") { EducationScreen(navController) }
            composable("wishlist")  { WishlistScreen(navController) }
            composable("stats")     { StatsScreen(navController) }
            composable("profile")   { ProfileScreen(navController) }
        }
    }
}







