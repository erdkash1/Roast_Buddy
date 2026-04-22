package com.example.rooastbuddy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RoastBuddyApp() }
    }
}

@Composable
fun RoastBuddyApp() {
    val navController = rememberNavController()
    // Single shared ViewModel across all screens
    val viewModel: CoffeeViewModel = viewModel()

    Scaffold(
        topBar = {
            @OptIn(ExperimentalMaterial3Api::class)
            TopAppBar(
                title = { Text("Roast Buddy", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("home")    { HomeScreen(navController) }
            composable("quiz")    { QuizScreen(navController, viewModel) }
            composable("result")  { ResultScreen(navController, viewModel) }
            composable("catalog") { CatalogScreen(navController, viewModel) }
        }
    }
}


@Composable
fun HomeScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Find Your Perfect Cup", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("Discover the 'why' behind the coffee.", fontSize = 16.sp)
        Spacer(Modifier.height(32.dp))

        Button(
            onClick = { navController.navigate("quiz") },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Take the Roast Finder Quiz") }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { navController.navigate("catalog") },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Browse Artisanal Catalog") }
    }
}


private val questions = listOf(
    "How do you brew your coffee?" to
            listOf("Pour-Over", "French Press", "Espresso Machine", "Drip Maker"),
    "What flavors do you enjoy?" to
            listOf("Bright & Fruity", "Chocolate & Nutty", "Earthy & Bold", "Sweet & Floral"),
    "How strong do you like it?" to
            listOf("Light & Delicate", "Balanced", "Strong & Intense", "Extra Bold")
)

@Composable
fun QuizScreen(navController: NavController, viewModel: CoffeeViewModel) {
    var currentQ by remember { mutableIntStateOf(0) }

    val (questionText, options) = questions[currentQ]

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "Question ${currentQ + 1} of ${questions.size}",
            color = MaterialTheme.colorScheme.secondary
        )
        Spacer(Modifier.height(8.dp))
        Text(questionText, fontSize = 22.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(24.dp))

        options.forEach { option ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable {
                        viewModel.quizAnswers.add(option)
                        if (currentQ < questions.size - 1) {
                            currentQ++
                        } else {
                            viewModel.fetchRecommendation()
                            navController.navigate("result")
                        }
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(text = option, modifier = Modifier.padding(16.dp), fontSize = 18.sp)
            }
        }
    }
}


@Composable
fun ResultScreen(navController: NavController, viewModel: CoffeeViewModel) {
    val recommendation by viewModel.recommendation.collectAsState()
    val isLoading by viewModel.isLoadingRec.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Your AI-Powered Match", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator()
            Spacer(Modifier.height(16.dp))
            Text("Brewing your recommendation...", color = MaterialTheme.colorScheme.secondary)
        } else {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Why we picked this for you:", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(recommendation ?: "No recommendation received.")
                }
            }
            Spacer(Modifier.height(32.dp))
            Button(onClick = { navController.navigate("catalog") }) {
                Text("See Full Catalog")
            }
        }
    }
}


@Composable
fun CatalogScreen(navController: NavController, viewModel: CoffeeViewModel) {
    val catalog by viewModel.catalog.collectAsState()
    val isLoading by viewModel.isLoadingCatalog.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCatalog()
    }

    Column(modifier = Modifier.fillMaxSize()) {
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

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (catalog.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No coffees found. Check Firestore setup.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(catalog) { coffee ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                "${coffee.name} (${coffee.roast})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                coffee.notes,
                                color = MaterialTheme.colorScheme.secondary,
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(coffee.description, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}