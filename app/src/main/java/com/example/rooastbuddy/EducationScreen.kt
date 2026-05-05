/**
 * EducationScreen.kt
 *
 * Displays a curated collection of coffee education resources including
 * articles and videos. Users can filter by category and tap any card
 * to open the linked resource directly in the device's browser.
 *
 * Categories: Roast Guide, Brew Methods, Tasting, Origins, Equipment, Video
 */

package com.example.rooastbuddy

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.navigation.NavController



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

