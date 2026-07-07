package com.smylo.feature.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smylo.R

@Composable
internal fun AuthBackgroundImage(
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.login_register_bgimage),
            contentDescription = null,
            modifier = Modifier.matchParentSize(),
            contentScale = contentScale
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            AuthBrandTeal.copy(alpha = 0.55f),
                            AuthBrandTeal.copy(alpha = 0.25f),
                            AuthPageBackground
                        )
                    )
                )
        )
    }
}

@Composable
internal fun AuthBackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(44.dp)
            .shadow(4.dp, CircleShape)
            .background(Color.White.copy(alpha = 0.92f), CircleShape)
    ) {
        Icon(
            Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "Back",
            tint = AuthDarkTeal
        )
    }
}

@Composable
internal fun AuthFieldLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = AuthLabelText,
        letterSpacing = 0.8.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
internal fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: ImageVector,
    keyboardType: KeyboardType,
    readOnly: Boolean = false,
    trailingIcon: ImageVector? = null
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        readOnly = readOnly,
        placeholder = {
            Text(placeholder, color = AuthPlaceholderText, style = MaterialTheme.typography.bodyMedium)
        },
        leadingIcon = {
            Icon(leadingIcon, contentDescription = null, tint = AuthIconTint, modifier = Modifier.size(20.dp))
        },
        trailingIcon = trailingIcon?.let { icon ->
            {
                Icon(icon, contentDescription = null, tint = AuthIconTint, modifier = Modifier.size(18.dp))
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, AuthInputBorder, RoundedCornerShape(12.dp)),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = AuthDarkTeal,
            unfocusedTextColor = AuthDarkTeal
        ),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true
    )
}

@Composable
internal fun OtpSuccessBanner() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(AuthLightTealPanel)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = AuthBrandTeal,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "OTP sent successfully",
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = AuthDarkTeal
        )
    }
}

@Composable
internal fun OtpInputSection(
    otpCode: String,
    onOtpChange: (String) -> Unit,
    otpRequested: Boolean,
    resendCooldownSeconds: Int,
    canResendOtp: Boolean,
    onResendOtp: () -> Unit,
    label: String = "SECURITY CODE (OTP)"
) {
    val resendLabel = when {
        !otpRequested -> "Resend Code"
        resendCooldownSeconds > 0 -> {
            val minutes = resendCooldownSeconds / 60
            val seconds = resendCooldownSeconds % 60
            "Resend in %d:%02d".format(minutes, seconds)
        }
        else -> "Resend Code"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AuthFieldLabel(label)
        if (otpRequested) {
            Text(
                text = resendLabel,
                color = if (canResendOtp) AuthBrandTeal else AuthFooterText,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(enabled = canResendOtp) { onResendOtp() }
            )
        }
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
                val char = otpCode.getOrNull(index)?.toString() ?: ""
                val isFocused = otpCode.length == index
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, AuthInputBorder, RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .then(
                            if (isFocused) Modifier.border(1.5.dp, AuthBrandTeal, RoundedCornerShape(12.dp))
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
            value = otpCode,
            onValueChange = { if (it.length <= 6) onOtpChange(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .focusRequester(focusRequester),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            cursorBrush = SolidColor(Color.Transparent),
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Color.Transparent),
            decorationBox = { it() }
        )
    }
}

@Composable
internal fun OutlinedAuthButton(
    text: String,
    enabled: Boolean,
    loading: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !loading,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthLightTealPanel,
            contentColor = AuthDarkTeal,
            disabledContainerColor = AuthLightTealPanel.copy(alpha = 0.5f),
            disabledContentColor = AuthDarkTeal.copy(alpha = 0.45f)
        ),
        elevation = null
    ) {
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp,
                color = AuthDarkTeal
            )
        } else {
            Text(text, fontWeight = FontWeight.Bold, letterSpacing = 0.6.sp)
        }
    }
}
