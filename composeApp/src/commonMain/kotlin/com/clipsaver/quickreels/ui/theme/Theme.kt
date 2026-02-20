package com.clipsaver.quickreels.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = SurfaceWhite,
    primaryContainer = PrimaryDarkDeepPurple,
    onPrimaryContainer = SurfaceWhite,
    secondary = AccentPinkStart,
    onSecondary = SurfaceWhite,
    background = BackgroundOffWhite,
    onBackground = DarkTextCharcoal,
    surface = SurfaceWhite,
    onSurface = DarkTextCharcoal,
)

// Define DarkColorScheme if needed, for now mapping to same or adjusted
private val DarkColorScheme = darkColorScheme(
    primary = PrimaryPurple,
    onPrimary = SurfaceWhite,
    primaryContainer = PrimaryDarkDeepPurple,
    onPrimaryContainer = SurfaceWhite,
    secondary = AccentPinkStart,
    onSecondary = SurfaceWhite,
    background = DarkTextCharcoal, // Dark background
    onBackground = SurfaceWhite,
    surface = PrimaryDarkDeepPurple, // Dark surface
    onSurface = SurfaceWhite,
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
//    val colorScheme = if (darkTheme) {
//        DarkColorScheme
//    } else {
//        LightColorScheme
//    }

    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = AppTypography(),
        content = content,
    )
}
