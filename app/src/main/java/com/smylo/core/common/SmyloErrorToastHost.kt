package com.smylo.core.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

private const val ERROR_TOAST_DURATION_MS = 8_000L

@Composable
fun SmyloErrorToastHost(
    errorMessenger: ErrorMessenger,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var visibleMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(errorMessenger) {
        errorMessenger.events.collectLatest { event ->
            visibleMessage = event.message
            delay(ERROR_TOAST_DURATION_MS)
            if (visibleMessage == event.message) {
                visibleMessage = null
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        content()

        AnimatedVisibility(
            visible = visibleMessage != null,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            visibleMessage?.let { message ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(6.dp, RoundedCornerShape(12.dp))
                        .background(
                            color = MaterialTheme.colorScheme.error,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = 14.dp)
                ) {
                    androidx.compose.foundation.layout.Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onError,
                            modifier = Modifier.padding(end = 10.dp)
                        )
                        Text(
                            text = message,
                            color = MaterialTheme.colorScheme.onError,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
        }
    }
}
