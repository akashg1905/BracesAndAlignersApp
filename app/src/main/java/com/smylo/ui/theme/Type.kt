package com.smylo.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.smylo.R

// Manrope FontFamily using the resources from res/font
val Manrope = FontFamily(
    Font(R.font.manrope_regular, FontWeight.Normal),
    Font(R.font.manrope_medium, FontWeight.Medium),
    Font(R.font.manrope_semibold, FontWeight.SemiBold),
    Font(R.font.manrope_bold, FontWeight.Bold),
    Font(R.font.manrope_extrabold, FontWeight.ExtraBold),
    Font(R.font.manrope_light, FontWeight.Light),
    Font(R.font.manrope_extralight, FontWeight.ExtraLight)
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 40.sp,
        letterSpacing = (-0.02).sp
    ),
    headlineMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.01).sp
    ),
    headlineSmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 28.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.02.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.05.sp
    )
)
