package com.example.bracesaligner.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

import androidx.compose.ui.graphics.Color

val AlignerGreen = Color(0xFF005B4B)
val AlignerLightBg = Color(0xFFE1E8ED)
val AlignerTextGrey = Color(0xFF637378)

private val LightColors = lightColorScheme(
    primary = AlignerGreen,
    onPrimary = Color.White,
    surface = Color.White,
    onSurface = Color.Black,
    secondary = AlignerTextGrey
)
private val DarkColors = darkColorScheme()

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
