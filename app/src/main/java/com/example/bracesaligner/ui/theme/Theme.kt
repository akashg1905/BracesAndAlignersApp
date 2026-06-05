package com.example.bracesaligner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = AlignerGreen,
    onPrimary = AlignerWhite,
    surface = AlignerWhite,
    onSurface = AlignerBlack,
    secondary = AlignerTextGrey,
    background = AlignerWhite,
    onBackground = AlignerBlack
)
private val DarkColors = darkColorScheme(
    primary = AlignerGreen,
    onPrimary = AlignerWhite,
    surface = AlignerBlack,
    onSurface = AlignerWhite,
    secondary = AlignerTextGrey,
    tertiary = AlignerGreen,
    background = AlignerBlack,
    onBackground = AlignerWhite
)

@Composable
fun BracesAndAlignerTheme(
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
