/**
 * CoffeeDetailScreen.kt
 *
 * Full detail view for a selected coffee from the catalog.
 * Shows tasting notes, description, brew recommendation based on roast level,
 * and an Add/Remove Wishlist button that persists to the Room database.
 * Also includes a mock checkout button as a placeholder for future payment integration.
 */

package com.example.rooastbuddy

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

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