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
import com.example.rooastbuddy.ui.theme.ROoastBuddyTheme
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size

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

// ── Quiz ─────────────────────────────────────────────────────────────────────

private val questions = listOf(
    "How do you brew your coffee?" to
            listOf("Pour-Over", "French Press", "Espresso Machine", "Drip Maker"),
    "What flavors do you enjoy?" to
            listOf("Bright & Fruity", "Chocolate & Nutty", "Earthy & Bold", "Sweet & Floral"),
    "How strong do you like it?" to
            listOf("Light & Delicate", "Balanced", "Strong & Intense", "Extra Bold")
)








data class EducationItem(
    val title: String,
    val description: String,
    val url: String,
    val category: String
)

@Composable
fun EducationScreen(navController: NavController) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val articles = listOf(
        EducationItem(
            title = "Light vs Dark Roast: What's the Difference?",
            description = "Learn how roast level affects flavor, caffeine, and acidity.",
            url = "https://www.ncausa.org/About-Coffee/Coffee-Roasts-Guide",
            category = "Roast Guide"
        ),
        EducationItem(
            title = "The Science of Pour-Over Coffee",
            description = "Why pour-over brings out the best in light roasts.",
            url = "https://www.homegrounds.co/pour-over-coffee-guide/",
            category = "Brew Methods"
        ),
        EducationItem(
            title = "How to Taste Coffee Like a Pro",
            description = "A beginner's guide to coffee cupping and tasting notes.",
            url = "https://www.seriouseats.com/how-to-taste-coffee",
            category = "Tasting"
        ),
        EducationItem(
            title = "Ethiopian Coffee: Origins & Flavor",
            description = "Discover why Ethiopia is considered the birthplace of coffee.",
            url = "https://www.homegrounds.co/ethiopian-coffee/",
            category = "Origins"
        ),
        EducationItem(
            title = "Beginner's Guide to Coffee Grinders",
            description = "How grind size affects your brew and which grinder to buy.",
            url = "https://www.homegrounds.co/best-coffee-grinder/",
            category = "Equipment"
        ),
        EducationItem(
            title = "Video: How Coffee is Made (Seed to Cup)",
            description = "Watch the full journey of coffee from farm to your cup.",
            url = "https://www.youtube.com/watch?v=R3NeHAFkBxA",
            category = "Video"
        )
    )

    val categories = articles.map { it.category }.distinct()
    var selectedCategory by remember { mutableStateOf("All") }
    val filtered = if (selectedCategory == "All") articles
    else articles.filter { it.category == selectedCategory }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { navController.popBackStack() }) { Text("< Back") }
                Text(
                    "Education Hub",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        // Scrollable filter chips
        item {
            androidx.compose.foundation.lazy.LazyRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = selectedCategory == "All",
                        onClick = { selectedCategory = "All" },
                        label = { Text("All") }
                    )
                }
                items(categories) { cat ->
                    FilterChip(
                        selected = selectedCategory == cat,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat) }
                    )
                }
            }
            Spacer(Modifier.height(8.dp))
        }

        // Articles
        items(filtered) { item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clickable {
                        val intent = android.content.Intent(
                            android.content.Intent.ACTION_VIEW,
                            android.net.Uri.parse(item.url)
                        )
                        context.startActivity(intent)
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            item.category,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (item.category == "Video") "▶ Watch" else "→ Read",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    Text(item.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(Modifier.height(4.dp))
                    Text(item.description, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}
@Composable
fun WishlistScreen(navController: NavController) {
    val viewModel: JournalViewModel = viewModel()
    val wishlist by viewModel.wishlist.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { navController.popBackStack() }) { Text("< Back") }
            Text(
                "My Wishlist",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        if (wishlist.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No items yet. Browse the catalog and add coffees!")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
                items(wishlist) { item ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(item.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                Text("${item.roast} Roast", color = MaterialTheme.colorScheme.secondary)
                                Text(item.notes, fontSize = 13.sp)
                            }
                            IconButton(onClick = { viewModel.removeFromWishlist(item) }) {
                                Text("✕", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

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




