package com.smylo.feature.auth.presentation

import android.util.Patterns
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smylo.R
import com.smylo.core.common.AppInfo
import com.smylo.core.common.GradientTipCard

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

    val brandingColor = Color(0xFF23535C)

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
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(brandingColor.copy(alpha = 0.6f))
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(brandingColor.copy(alpha = 0.35f))
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(brandingColor.copy(alpha = 0.2f))
                )
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

@Composable
fun AuthScreen(
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onRequestOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onDismissError: () -> Unit
) {
    if (state.error != null) {
        AlertDialog(
            onDismissRequest = onDismissError,
            title = { Text("Error") },
            text = { Text(state.error) },
            confirmButton = {
                TextButton(onClick = onDismissError) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(MaterialTheme.colorScheme.surfaceContainerLow, MaterialTheme.colorScheme.surface)
                    )
                )
                .padding(horizontal = 24.dp)
                .padding(top = 64.dp, bottom = 40.dp)
        ) {
            Text(
                text = "Smylo",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge.copy(
                    lineHeight = 40.sp
                ),
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
            Box(
                modifier = Modifier
                    .width(44.dp)
                    .height(4.dp)
                    .background(MaterialTheme.colorScheme.primary)
            )
            
            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = buildAnnotatedString {
                    append("Your smile,\n")
                    withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append("reimagined.")
                    }
                },
                style = MaterialTheme.typography.displayMedium.copy(
                    lineHeight = 60.sp
                ),
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Step into our digital sanctuary. Your journey to professional dental alignment begins with precision and clarity.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "WELCOME BACK",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )

            Text(
                text = "Clinical Login",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Phone Number Input
            LoginLabel("PHONE NUMBER")
            CustomTextField(
                value = state.phoneNumber,
                onValueChange = { 
                    if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                        onPhoneNumberChange(it)
                    }
                },
                placeholder = "9876543210",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Input
            LoginLabel("EMAIL ADDRESS")
            CustomTextField(
                value = state.email,
                onValueChange = { onEmailChange(it.trim()) },
                placeholder = "user@email.com",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(24.dp))

            // OTP Section
            val isEmailValid = remember(state.email) {
                Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
            }
            val isPhoneValid = remember(state.phoneNumber) {
                state.phoneNumber.length == 10 && state.phoneNumber.all { it.isDigit() }
            }
            val isOtpValid = remember(state.otpCode) {
                state.otpCode.length == 6
            }
            
            val canRequestOtp = isEmailValid && isPhoneValid

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoginLabel("SECURITY CODE (OTP)")
                Text(
                    text = "Resend Code",
                    color = if (canRequestOtp) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(enabled = canRequestOtp) { onRequestOtp() }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            val focusRequester = remember { FocusRequester() }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { focusRequester.requestFocus() }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(6) { index ->
                        val char = state.otpCode.getOrNull(index)?.toString() ?: ""
                        val isFocused = state.otpCode.length == index
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                                .then(
                                    if (isFocused) Modifier.background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f))
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                BasicTextField(
                    value = state.otpCode,
                    onValueChange = { if (it.length <= 6) onOtpChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    cursorBrush = SolidColor(Color.Transparent),
                    textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Transparent),
                    decorationBox = { it() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Enter your credentials and verification code sent via SMS.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (!state.otpRequested) onRequestOtp() else onVerifyOtp()
                },
                enabled = !state.loading && if (!state.otpRequested) canRequestOtp else isOtpValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                if (state.loading) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (!state.otpRequested) "Request OTP" else "Login",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = buildAnnotatedString {
                    append("New patient? ")
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
                        append("Start Consultation")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(48.dp))

            GradientTipCard(
                tip = "Always remember to keep your previous set of aligners. They serve as a backup in case of loss or damage.",
                title = "PRO TIP"
            )

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(" Privacy", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(32.dp))
                Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(" Support", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            state.error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun LoginLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    keyboardType: KeyboardType
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
}

