package com.smylo.feature.auth.presentation

import android.util.Patterns
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    state: AuthUiState,
    onBack: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    onDateOfBirthChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onRequestOtp: () -> Unit,
    onResendOtp: () -> Unit,
    onVerifyOtp: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onDismissSuccessMessage: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by remember { mutableStateOf(false) }

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
    val canResendOtp = state.otpRequested &&
        state.resendCooldownSeconds == 0 &&
        canRequestOtp &&
        !state.loading

    if (showDatePicker) {
        val initialMillis = remember(state.dateOfBirth) {
            runCatching {
                if (state.dateOfBirth.isNotBlank()) {
                    LocalDate.parse(state.dateOfBirth, DateTimeFormatter.ISO_LOCAL_DATE)
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                } else null
            }.getOrNull()
        }
        val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        onDateOfBirthChange(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    }
                    showDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = AuthPageBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {
                AuthBackgroundImage(
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                AuthBackButton(
                    onClick = onBack,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 16.dp, top = 16.dp)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-28).dp)
                    .padding(horizontal = 16.dp)
                    .shadow(10.dp, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Text(
                    text = "ALIGNERCARE PROFESSIONAL",
                    style = MaterialTheme.typography.labelSmall,
                    color = AuthBrandTeal,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.4.sp
                )
                Text(
                    text = "Clinical Registration",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = AuthDarkTeal,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Initialize your professional care profile to coordinate your treatment plan with our clinical team.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuthMutedText,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        AuthFieldLabel("FIRST NAME")
                        AuthTextField(
                            value = state.firstName,
                            onValueChange = onFirstNameChange,
                            placeholder = "Legal first name",
                            leadingIcon = Icons.Default.Person,
                            keyboardType = KeyboardType.Text
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        AuthFieldLabel("LAST NAME")
                        AuthTextField(
                            value = state.lastName,
                            onValueChange = onLastNameChange,
                            placeholder = "Legal last name",
                            leadingIcon = Icons.Default.Person,
                            keyboardType = KeyboardType.Text
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                AuthFieldLabel("EMAIL ADDRESS *")
                AuthTextField(
                    value = state.email,
                    onValueChange = { onEmailChange(it.trim()) },
                    placeholder = "primary@example.com",
                    leadingIcon = Icons.Default.Email,
                    keyboardType = KeyboardType.Email
                )

                Spacer(modifier = Modifier.height(16.dp))

                AuthFieldLabel("DATE OF BIRTH")
                val dobDisplay = remember(state.dateOfBirth) {
                    runCatching {
                        if (state.dateOfBirth.isNotBlank()) {
                            LocalDate.parse(state.dateOfBirth, DateTimeFormatter.ISO_LOCAL_DATE)
                                .format(DateTimeFormatter.ofPattern("MM/dd/yyyy"))
                        } else ""
                    }.getOrDefault(state.dateOfBirth)
                }
                Box(modifier = Modifier.clickable { showDatePicker = true }) {
                    AuthTextField(
                        value = dobDisplay,
                        onValueChange = {},
                        placeholder = "mm/dd/yyyy",
                        leadingIcon = Icons.Default.CalendarToday,
                        keyboardType = KeyboardType.Text,
                        readOnly = true
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AuthFieldLabel("PHONE NUMBER *")
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

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedAuthButton(
                    text = "REQUEST VERIFICATION CODE",
                    enabled = !state.loading && canRequestOtp && !state.otpRequested,
                    loading = state.loading && !state.otpRequested,
                    onClick = onRequestOtp
                )

                Spacer(modifier = Modifier.height(20.dp))

                OtpInputSection(
                    otpCode = state.otpCode,
                    onOtpChange = onOtpChange,
                    otpRequested = state.otpRequested,
                    resendCooldownSeconds = state.resendCooldownSeconds,
                    canResendOtp = canResendOtp,
                    onResendOtp = onResendOtp,
                    label = "Security Code"
                )

                if (state.otpRequested) {
                    Spacer(modifier = Modifier.height(12.dp))
                    OtpSuccessBanner()
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onVerifyOtp,
                    enabled = !state.loading && isOtpValid && state.otpRequested,
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
                    if (state.loading && state.otpRequested) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Establish Patient Profile", fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(AuthPageBackground)
                        .padding(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Shield,
                            contentDescription = null,
                            tint = AuthBrandTeal,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Secure Clinical Data",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = AuthDarkTeal
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Your records are managed under strict healthcare privacy protocols, ensuring direct and secure communication with your orthodontic specialist.",
                        style = MaterialTheme.typography.bodySmall,
                        color = AuthMutedText,
                        lineHeight = 18.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-12).dp)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("Already registered? ")
                        withStyle(style = SpanStyle(color = AuthDarkTeal, fontWeight = FontWeight.Bold)) {
                            append("Sign In")
                        }
                    },
                    modifier = Modifier.clickable(onClick = onNavigateToLogin),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AuthMutedText
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(horizontalArrangement = Arrangement.Center) {
                    Text(
                        "PRIVACY POLICY",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuthFooterText,
                        letterSpacing = 0.8.sp
                    )
                    Spacer(modifier = Modifier.width(28.dp))
                    Text(
                        "TERMS OF SERVICE",
                        style = MaterialTheme.typography.labelSmall,
                        color = AuthFooterText,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
