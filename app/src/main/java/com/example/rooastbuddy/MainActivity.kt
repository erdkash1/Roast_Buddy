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

@Composable
fun CoffeeCard(coffee: Coffee, onClick: () -> Unit) {
    val roastColor = when (coffee.roast) {
        "Light" -> MaterialTheme.colorScheme.tertiary
        "Medium" -> MaterialTheme.colorScheme.secondary
        "Dark" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    coffee.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    color = roastColor.copy(alpha = 0.15f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        "${coffee.roast} Roast",
                        fontSize = 11.sp,
                        color = roastColor,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                coffee.notes,
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                coffee.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Tap to see details →",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CoffeeDetailScreen(coffee: Coffee, onBack: () -> Unit) {
    val viewModel: JournalViewModel = viewModel()
    val isInWishlist by viewModel.isInWishlist(coffee.name).collectAsState(initial = false)

    val roastColor = when (coffee.roast) {
        "Light" -> MaterialTheme.colorScheme.tertiary
        "Medium" -> MaterialTheme.colorScheme.secondary
        "Dark" -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.primary
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) { Text("< Back to Catalog") }
        Spacer(Modifier.height(16.dp))
        Text(coffee.name, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        Surface(
            color = roastColor.copy(alpha = 0.15f),
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                "${coffee.roast} Roast",
                fontSize = 14.sp,
                color = roastColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }

        Spacer(Modifier.height(24.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tasting Notes", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text(coffee.notes, fontSize = 15.sp, color = MaterialTheme.colorScheme.secondary)
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("About this Coffee", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text(coffee.description, fontSize = 15.sp)
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Brew Recommendation", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    when (coffee.roast) {
                        "Light" -> "Best with Pour-Over or Aeropress to highlight delicate floral and fruit notes."
                        "Medium" -> "Great with Drip or French Press for a balanced, smooth cup."
                        "Dark" -> "Perfect for Espresso or French Press to bring out bold, rich flavors."
                        else -> "Try different brew methods to find your favorite."
                    },
                    fontSize = 14.sp
                )
            }
        }

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = {
                if (isInWishlist) viewModel.removeFromWishlist(
                    WishlistItem(coffee.name, coffee.roast, coffee.notes, coffee.description)
                ) else viewModel.addToWishlist(coffee)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = if (isInWishlist) ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary
            ) else ButtonDefaults.buttonColors()
        ) {
            Text(if (isInWishlist) "✓ Remove from Wishlist" else "Add to Wishlist")
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = { },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) { Text("Mock Checkout (Coming Soon)") }
    }
}
// ── Journal ───────────────────────────────────────────────────────────────────

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

// ── Education Hub ─────────────────────────────────────────────────────────────

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

// ── Profile Setup ─────────────────────────────────────────────────────────────

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

// ── Profile Screen ────────────────────────────────────────────────────────────

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