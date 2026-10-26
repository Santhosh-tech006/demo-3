package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GrandmasterColorScheme = darkColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = CardBgElevated,
    onPrimaryContainer = LightBlue,
    secondary = ElectricBlue,
    onSecondary = BgPrimary,
    secondaryContainer = CardBg,
    onSecondaryContainer = LightBlue,
    tertiary = AccentGold,
    onTertiary = BgPrimary,
    background = BgPrimary,
    onBackground = TextMain,
    surface = BgSecondary,
    onSurface = TextMain,
    surfaceVariant = CardBg,
    onSurfaceVariant = TextSecondary,
    outline = BorderColor,
    outlineVariant = BorderLight,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun GrandmasterAcademyTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GrandmasterColorScheme,
        typography = Typography,
        content = content
    )
}
