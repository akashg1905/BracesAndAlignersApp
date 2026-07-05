package com.smylo.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = PrimaryCyanTeal,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    inversePrimary = InversePrimary,
    secondary = SecondarySlateTeal,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = TertiaryAmber,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    error = ErrorRed,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    background = SurfaceLight,
    onBackground = OnSurface,
    surface = SurfaceLight,
    onSurface = OnSurface,
    surfaceDim = SurfaceDim,
    surfaceBright = SurfaceBright,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest,
    surfaceVariant = SurfaceContainerHighest,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    inverseSurface = Color(0xFF2C3132),
    inverseOnSurface = Color(0xFFEDF1F2)
)

// High-Tech Clinical dark palette — dark surfaces with readable text
private val DarkColors = darkColorScheme(
    primary = InversePrimary,
    onPrimary = Color(0xFF003544),
    primaryContainer = Color(0xFF004D61),
    onPrimaryContainer = Color(0xFF9EEFFF),
    inversePrimary = PrimaryCyanTeal,
    secondary = Color(0xFF89D0E0),
    onSecondary = Color(0xFF003544),
    secondaryContainer = Color(0xFF1B4A52),
    onSecondaryContainer = Color(0xFFB8EAEF),
    tertiary = TertiaryContainer,
    onTertiary = OnTertiaryContainer,
    tertiaryContainer = Color(0xFF5D4200),
    onTertiaryContainer = Color(0xFFFFBC66),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF0E1415),
    onBackground = Color(0xFFEDF1F2),
    surface = Color(0xFF0E1415),
    onSurface = Color(0xFFEDF1F2),
    surfaceDim = Color(0xFF0E1415),
    surfaceBright = Color(0xFF343A3B),
    surfaceContainerLowest = Color(0xFF090E0F),
    surfaceContainerLow = Color(0xFF171C1D),
    surfaceContainer = Color(0xFF1B2122),
    surfaceContainerHigh = Color(0xFF252B2C),
    surfaceContainerHighest = Color(0xFF303637),
    surfaceVariant = Color(0xFF303637),
    onSurfaceVariant = Color(0xFFBDC9CB),
    outline = Color(0xFF899294),
    outlineVariant = Color(0xFF3D494A),
    inverseSurface = Color(0xFFEDF1F2),
    inverseOnSurface = Color(0xFF2C3132)
)

@Composable
fun SmyloTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

