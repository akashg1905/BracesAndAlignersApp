package com.smylo.feature.profile.presentation

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smylo.core.common.AppInfo
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

private const val PRIVACY_POLICY_URL = "https://sites.google.com/view/smylo-app/privacy-policy"
private const val TERMS_OF_SERVICE_URL = "https://sites.google.com/view/smylo-app/terms-and-condition"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingsScreen(
    state: AccountSettingsUiState,
    onBack: () -> Unit,
    onUpdateSetting: (String, String) -> Unit,
    onSaveSettings: () -> Unit,
    onLogout: () -> Unit,
    onClearMessages: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.successMessage) {
        state.successMessage?.let {
            snackbarHostState.showSnackbar(it)
            onClearMessages()
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Log Out", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Account") },
            text = { Text("Account deletion is not available in this version. Please contact support for assistance.") },
            confirmButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("OK")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Account Settings",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SettingsSectionHeader("NOTIFICATIONS")

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                Column(modifier = Modifier.padding(vertical = 8.dp)) {
                    state.settings.forEachIndexed { index, item ->
                        DynamicSettingRow(
                            item = item,
                            onValueChange = { onUpdateSetting(item.settingName, it) }
                        )
                        if (index < state.settings.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onSaveSettings,
                        enabled = !state.isSaving && state.settings.isNotEmpty(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        if (state.isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Icon(
                                Icons.Default.Save,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Save Settings", fontWeight = FontWeight.SemiBold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionHeader("ACCOUNT")

            SettingsActionCard {
                AccountActionRow(
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    title = "Log Out",
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    iconTint = MaterialTheme.colorScheme.error,
                    onClick = { showLogoutDialog = true }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                AccountActionRow(
                    icon = Icons.Default.Delete,
                    title = "Delete Account",
                    titleColor = MaterialTheme.colorScheme.error,
                    iconTint = MaterialTheme.colorScheme.error,
                    onClick = { showDeleteDialog = true }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            SettingsSectionHeader("LEGAL")

            SettingsActionCard {
                AccountActionRow(
                    icon = Icons.Default.Shield,
                    title = "Privacy Policy",
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    iconTint = MaterialTheme.colorScheme.primary,
                    trailingIcon = Icons.Outlined.OpenInNew,
                    onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(PRIVACY_POLICY_URL))
                        )
                    }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                AccountActionRow(
                    icon = Icons.Default.Gavel,
                    title = "Terms of Service",
                    titleColor = MaterialTheme.colorScheme.onSurface,
                    iconTint = MaterialTheme.colorScheme.primary,
                    trailingIcon = Icons.Outlined.OpenInNew,
                    onClick = {
                        context.startActivity(
                            Intent(Intent.ACTION_VIEW, Uri.parse(TERMS_OF_SERVICE_URL))
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "SmyLo App ${AppInfo.versionName}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Text(
                text = "© ${Calendar.getInstance().get(Calendar.YEAR)} SmyLo Orthodontics",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
private fun SettingsActionCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(content = content)
    }
}

@Composable
private fun AccountActionRow(
    icon: ImageVector,
    title: String,
    titleColor: androidx.compose.ui.graphics.Color,
    iconTint: androidx.compose.ui.graphics.Color,
    trailingIcon: ImageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            color = titleColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            trailingIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DynamicSettingRow(
    item: SettingItemUi,
    onValueChange: (String) -> Unit
) {
    when (item.valueKind) {
        "boolean" -> BooleanSettingRow(item, onValueChange)
        "date" -> DateSettingRow(item, onValueChange)
        else -> HourSettingRow(item, onValueChange)
    }
}

@Composable
private fun SettingRowLayout(
    item: SettingItemUi,
    inputContent: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(
                if (item.isUnset) {
                    Modifier.background(
                        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.25f)
                    )
                } else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                iconForSetting(item.settingName),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.label,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (item.description.isNotBlank()) {
                Text(
                    text = item.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        inputContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HourSettingRow(
    item: SettingItemUi,
    onValueChange: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val (initialHour, initialMinute) = parseHourValue(item.value) ?: (8 to 0)
    val timePickerState = rememberTimePickerState(
        initialHour = initialHour,
        initialMinute = initialMinute,
        is24Hour = false
    )

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onValueChange(formatHourApi(timePickerState.hour, timePickerState.minute))
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }

    SettingRowLayout(item = item) {
        OutlinedButton(
            onClick = { showTimePicker = true },
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(
                width = if (item.isUnset) 1.5.dp else 1.dp,
                color = if (item.isUnset) {
                    MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = if (item.isUnset) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = formatHourDisplay(item.value),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                Icons.Default.Schedule,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSettingRow(
    item: SettingItemUi,
    onValueChange: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val initialDate = parseDateValue(item.value) ?: LocalDate.now()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val date = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onValueChange(date.format(DateTimeFormatter.ISO_LOCAL_DATE))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    SettingRowLayout(item = item) {
        OutlinedButton(
            onClick = { showDatePicker = true },
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(
                width = if (item.isUnset) 1.5.dp else 1.dp,
                color = if (item.isUnset) {
                    MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.outlineVariant
                }
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = if (item.isUnset) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                }
            ),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = formatDateDisplay(item.value),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun BooleanSettingRow(
    item: SettingItemUi,
    onValueChange: (String) -> Unit
) {
    val checked = item.value.equals("true", ignoreCase = true)

    SettingRowLayout(item = item) {
        Switch(
            checked = checked,
            onCheckedChange = { onValueChange(it.toString()) },
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = if (item.isUnset) {
                    MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                } else {
                    MaterialTheme.colorScheme.outline
                }
            )
        )
    }
}

private fun iconForSetting(settingName: String): ImageVector = when {
    settingName.contains("breakfast", ignoreCase = true) -> Icons.Default.FreeBreakfast
    settingName.contains("lunch", ignoreCase = true) -> Icons.Default.LunchDining
    settingName.contains("dinner", ignoreCase = true) -> Icons.Default.DinnerDining
    settingName.contains("aligner", ignoreCase = true) ||
        settingName.contains("reminder", ignoreCase = true) -> Icons.Default.Notifications
    else -> Icons.Default.Settings
}

private fun parseHourValue(value: String): Pair<Int, Int>? {
    if (value.isBlank()) return null
    val parts = value.split(":")
    if (parts.size < 2) return null
    val hour = parts[0].trim().toIntOrNull() ?: return null
    val minute = parts[1].take(2).trim().toIntOrNull() ?: return null
    return hour to minute
}

private fun formatHourDisplay(value: String): String {
    val (hour, minute) = parseHourValue(value) ?: return "Set time"
    val hour12 = when {
        hour == 0 -> 12
        hour > 12 -> hour - 12
        else -> hour
    }
    val amPm = if (hour < 12) "AM" else "PM"
    return String.format(Locale.getDefault(), "%02d:%02d %s", hour12, minute, amPm)
}

private fun formatHourApi(hour: Int, minute: Int): String =
    String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

private fun parseDateValue(value: String): LocalDate? =
    runCatching { LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE) }.getOrNull()

private fun formatDateDisplay(value: String): String {
    if (value.isBlank()) return "Set date"
    return parseDateValue(value)?.format(DateTimeFormatter.ofPattern("MMM d, yyyy"))
        ?: value
}
