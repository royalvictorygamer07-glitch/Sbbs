package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = BloodCrimson,
    onPrimary = TextPrimary,
    primaryContainer = CrimsonDeep,
    onPrimaryContainer = CrimsonGlow,
    secondary = NeonCyan,
    onSecondary = DarkBackground,
    secondaryContainer = DarkSurfaceElevated,
    onSecondaryContainer = NeonCyan,
    tertiary = ElectricIndigo,
    onTertiary = TextPrimary,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder,
    outlineVariant = CrimsonBorder,
    error = BloodCrimson,
    onError = TextPrimary
)

@Composable
fun CodeMindTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
