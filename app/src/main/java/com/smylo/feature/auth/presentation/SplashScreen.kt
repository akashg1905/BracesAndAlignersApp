package com.smylo.feature.auth.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smylo.R
import com.smylo.core.common.AppInfo

@Composable
fun SplashScreen() {
    var startAnimation by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(1000),
        label = "alpha"
    )
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    val brandingColor = AuthDarkTeal

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE0F7F9),
                        Color.White
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(alpha = alpha),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_screen_2),
                contentDescription = "Smylo Logo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Welcome to",
                color = brandingColor,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-0.5).sp
                )
            )
            Text(
                text = "Smylo",
                color = brandingColor,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-1).sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Precision tracking for your perfect\nsmile.",
                style = MaterialTheme.typography.bodyLarge,
                color = brandingColor.copy(alpha = 0.5f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(72.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                listOf(0.6f, 0.35f, 0.2f).forEach { alpha ->
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(brandingColor.copy(alpha = alpha))
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(1.dp)
                    .background(brandingColor.copy(alpha = 0.15f))
            )
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(letterSpacing = 2.sp)) {
                        append("CLINICAL EXCELLENCE")
                    }
                    append("  *  ${AppInfo.versionLabel()}")
                },
                style = MaterialTheme.typography.labelSmall,
                color = brandingColor.copy(alpha = 0.4f),
                fontWeight = FontWeight.Medium
            )
        }
    }
}
