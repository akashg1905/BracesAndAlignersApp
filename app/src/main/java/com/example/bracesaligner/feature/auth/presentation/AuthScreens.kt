package com.example.bracesaligner.feature.auth.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bracesaligner.ui.theme.AlignerBlack
import com.example.bracesaligner.ui.theme.AlignerGreen
import com.example.bracesaligner.ui.theme.AlignerLightBg
import com.example.bracesaligner.ui.theme.AlignerOffWhite
import com.example.bracesaligner.ui.theme.AlignerRed
import com.example.bracesaligner.ui.theme.AlignerTextGrey
import com.example.bracesaligner.ui.theme.AlignerWhite

@Composable
fun SplashScreen() {
    var target by remember { mutableFloatStateOf(0.6f) }
    val scale by animateFloatAsState(targetValue = target, animationSpec = tween(1200), label = "scale")
    LaunchedEffect(Unit) {
        target = 1f
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(AlignerOffWhite, AlignerWhite)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer(alpha = scale, scaleX = scale, scaleY = scale),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Icon Container
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(AlignerWhite, RoundedCornerShape(24.dp))
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                // Simplified Sparkle Icon as per your image
                Icon(
                    imageVector = Icons.Default.Info, // Placeholder for the sparkle logo
                    contentDescription = null,
                    tint = AlignerGreen,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "AlignerCare",
                color = AlignerGreen,
                fontSize = 42.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Your smile, reimagined.",
                color = AlignerTextGrey,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.2.sp
            )

            Spacer(modifier = Modifier.height(48.dp))
            
            // Modern Dot Loader
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(if (index == 0) AlignerGreen else AlignerGreen.copy(alpha = 0.2f))
                    )
                }
            }
        }

        // Bottom Branding
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CLINICAL EXCELLENCE",
                color = AlignerTextGrey.copy(alpha = 0.5f),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "V 2.4.8",
                color = AlignerTextGrey.copy(alpha = 0.3f),
                fontSize = 10.sp,
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
    onVerifyOtp: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AlignerWhite)
            .verticalScroll(rememberScrollState())
    ) {
        // Hero Section with Image Placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(AlignerLightBg, AlignerWhite)
                    )
                )
        ) {
            // This is where the image from your screenshot would go.
            // For now, I'm using a gradient to simulate the "light/airy" feel.
            
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = "AlignerCare",
                    color = AlignerGreen,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
                Box(
                    modifier = Modifier
                        .width(44.dp)
                        .height(4.dp)
                        .background(AlignerGreen)
                )
                
                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = buildAnnotatedString {
                        append("Your smile,\n")
                        withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                            append("reimagined.")
                        }
                    },
                    fontSize = 44.sp,
                    lineHeight = 48.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = AlignerBlack
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Step into our digital sanctuary. Your journey to professional dental alignment begins with precision and clarity.",
                    fontSize = 16.sp,
                    color = AlignerTextGrey,
                    lineHeight = 24.sp,
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Text(
                text = "WELCOME BACK",
                color = AlignerGreen,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )

            Text(
                text = "Clinical Login",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp),
                color = AlignerBlack
            )

            // Phone Number Input
            LoginLabel("PHONE NUMBER")
            CustomTextField(
                value = state.phoneNumber,
                onValueChange = onPhoneNumberChange,
                placeholder = "+1 (555) 000-0000",
                leadingIcon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Email Input
            LoginLabel("EMAIL ADDRESS")
            CustomTextField(
                value = state.email,
                onValueChange = onEmailChange,
                placeholder = "doctor@alignercare.com",
                leadingIcon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(24.dp))

            // OTP Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoginLabel("SECURITY CODE (OTP)")
                Text(
                    text = "Resend Code",
                    color = AlignerGreen,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onRequestOtp() }
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
                                .clip(RoundedCornerShape(8.dp))
                                .background(AlignerLightBg)
                                .then(
                                    if (isFocused) Modifier.background(AlignerGreen.copy(alpha = 0.05f))
                                    else Modifier
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = char,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlignerBlack
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
                fontSize = 12.sp,
                color = AlignerTextGrey,
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (!state.otpRequested) onRequestOtp() else onVerifyOtp()
                },
                enabled = !state.loading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AlignerGreen,
                    disabledContainerColor = AlignerGreen.copy(alpha = 0.6f)
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                if (state.loading) {
                    CircularProgressIndicator(color = AlignerWhite, modifier = Modifier.size(24.dp))
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (!state.otpRequested) "Request OTP" else "Enter Sanctuary",
                            fontSize = 16.sp,
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
                    withStyle(style = SpanStyle(color = AlignerGreen, fontWeight = FontWeight.Bold)) {
                        append("Start Consultation")
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(64.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(14.dp), tint = AlignerTextGrey)
                Text(" Privacy", fontSize = 12.sp, color = AlignerTextGrey)
                Spacer(modifier = Modifier.width(32.dp))
                Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp), tint = AlignerTextGrey)
                Text(" Support", fontSize = 12.sp, color = AlignerTextGrey)
            }

            state.error?.let {
                Text(
                    text = it,
                    color = AlignerRed,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun LoginLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        color = AlignerTextGrey,
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
        placeholder = { Text(placeholder, color = AlignerTextGrey.copy(alpha = 0.5f)) },
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = AlignerTextGrey) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = AlignerLightBg,
            unfocusedContainerColor = AlignerLightBg,
            disabledContainerColor = AlignerLightBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = AlignerBlack,
            unfocusedTextColor = AlignerBlack
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
}
