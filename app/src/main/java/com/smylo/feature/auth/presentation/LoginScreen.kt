package com.smylo.feature.auth.presentation

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    state: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onRequestOtp: () -> Unit,
    onResendOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onDismissSuccessMessage: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onDismissSuccessMessage()
        }
    }

    val isEmailValid = remember(state.email) {
        Patterns.EMAIL_ADDRESS.matcher(state.email).matches()
    }
    val isPhoneValid = remember(state.phoneNumber) {
        state.phoneNumber.length == 10 && state.phoneNumber.all { it.isDigit() }
    }
    val isOtpValid = remember(state.otpCode) { state.otpCode.length == 6 }
    val canRequestOtp = isEmailValid && isPhoneValid

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AuthPageBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AuthPageBackground)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AuthBackgroundImage(modifier = Modifier.fillMaxSize())

                Text(
                    text = "SmyLo",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(12.dp, RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 28.dp)
                ) {
                    Text(
                        text = "Welcome Back",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = AuthDarkTeal
                    )
                    Text(
                        text = "Sign in to continue your smile journey.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AuthMutedText,
                        modifier = Modifier.padding(top = 8.dp, bottom = 24.dp)
                    )

                    AuthFieldLabel("PHONE NUMBER")
                    AuthTextField(
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

                    Spacer(modifier = Modifier.height(18.dp))

                    AuthFieldLabel("EMAIL ADDRESS")
                    AuthTextField(
                        value = state.email,
                        onValueChange = { onEmailChange(it.trim()) },
                        placeholder = "your@email.com",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email
                    )

                    if (state.otpRequested) {
                        Spacer(modifier = Modifier.height(22.dp))
                        OtpInputSection(
                            otpCode = state.otpCode,
                            onOtpChange = onOtpChange,
                            otpRequested = state.otpRequested,
                            resendCooldownSeconds = state.resendCooldownSeconds,
                            canResendOtp = state.otpRequested &&
                                state.resendCooldownSeconds == 0 &&
                                canRequestOtp &&
                                !state.loading,
                            onResendOtp = onResendOtp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OtpSuccessBanner()
                    }

                    Spacer(modifier = Modifier.height(26.dp))

                    Button(
                        onClick = {
                            if (!state.otpRequested) onRequestOtp() else onVerifyOtp()
                        },
                        enabled = !state.loading && if (!state.otpRequested) canRequestOtp else isOtpValid,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AuthDarkTeal,
                            contentColor = Color.White,
                            disabledContainerColor = AuthDarkTeal.copy(alpha = 0.45f)
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        if (state.loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = if (!state.otpRequested) "Request OTP" else "Sign In",
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 0.5.sp
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = buildAnnotatedString {
                        append("New patient? ")
                        withStyle(style = SpanStyle(color = AuthDarkTeal, fontWeight = FontWeight.Bold)) {
                            append("Start Consultation")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = onNavigateToRegister),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuthMutedText
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
