package com.example.rooastbuddy.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val RoastBuddyColorScheme = lightColorScheme(
    primary = PineGreen,
    onPrimary = Cream,
    primaryContainer = LightGreen,
    onPrimaryContainer = Bistre,

    secondary = CoffeeBrown,
    onSecondary = Cream,
    secondaryContainer = LightBrown,
    onSecondaryContainer = DarkBrown,

    tertiary = JuniperGreen,
    onTertiary = Cream,

    background = LightCream,
    onBackground = DarkBrown,

    surface = Cream,
    onSurface = DarkBrown,
    onSurfaceVariant = CoffeeBrown,

    error = LightBrown,
    onError = Cream
)

@Composable
fun ROoastBuddyTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = RoastBuddyColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = PineGreen.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}