package com.smylo.feature.timer.presentation

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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.smylo.core.common.GradientTipCard
import com.smylo.core.common.SmyloBottomNavBar
import com.smylo.core.common.SmyloTab
import com.smylo.core.common.TimerState
import com.smylo.core.database.entity.DailyNonWearSummaryEntity
import com.smylo.core.database.entity.NonWearSessionEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerDetailScreen(
    state: TimerState,
    profileImageUrl: String? = null,
    weeklySummary: List<DailyNonWearSummaryEntity>,
    todaySessions: List<NonWearSessionEntity> = emptyList(),
    onStart: () -> Unit,
    onStop: () -> Unit,
    onBack: () -> Unit,
    onOpenDailyWearDetails: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onNavigateToPlan: () -> Unit = {},
    onNavigateToScan: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val timeFormatter = SimpleDateFormat("hh:mm:ss aa", Locale.getDefault())
    val dateFormatter = SimpleDateFormat("EEEE, MMM dd", Locale.getDefault())
    val todayDateStr = dateFormatter.format(Date())

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        "Smylo",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {
                    IconButton(onClick = onOpenDailyWearDetails) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "History",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape)
                            .clickable { onNavigateToProfile() },
                        contentAlignment = Alignment.Center
                    ) {
                        if (profileImageUrl != null) {
                            AsyncImage(
                                model = profileImageUrl,
                                contentDescription = "Profile",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        bottomBar = {
            SmyloBottomNavBar(
                selectedTab = SmyloTab.PROGRESS,
                onTabSelected = { tab ->
                    when (tab) {
                        SmyloTab.PROGRESS -> onNavigateToProgress()
                        SmyloTab.SCHEDULE -> onNavigateToPlan()
                        SmyloTab.SCAN -> onNavigateToScan()
                        SmyloTab.PROFILE -> onNavigateToProfile()
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = todayDateStr,
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Summary of periods when aligners were removed.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0F2F7)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.background(
                        brush = Brush.linearGradient(
                            colors = listOf(Color(0xFFF4F9FB), Color.White)
                        )
                    )
                ) {
                    // Header with solid clinical background
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFE0F7F9))
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "START TIME",
                                color = Color(0xFF005D66).copy(alpha = 0.6f),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1.1f),
                                letterSpacing = 1.sp,
                                fontSize = 10.sp
                            )
                            Text(
                                "END TIME",
                                color = Color(0xFF005D66).copy(alpha = 0.6f),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1.1f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                                letterSpacing = 1.sp,
                                fontSize = 10.sp
                            )
                            Text(
                                "DURATION",
                                color = Color(0xFF005D66).copy(alpha = 0.6f),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(0.8f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                letterSpacing = 1.sp,
                                fontSize = 10.sp
                            )
                        }
                    }

                    Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                        todaySessions.forEachIndexed { index, session ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    timeFormatter.format(Date(session.startEpochMillis)).uppercase(),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF171C1D),
                                    modifier = Modifier.weight(1.1f)
                                )
                                Text(
                                    (session.endEpochMillis?.let { timeFormatter.format(Date(it)) } ?: "Ongoing").uppercase(),
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Medium,
                                    color = if (session.endEpochMillis == null) MaterialTheme.colorScheme.primary else Color(0xFF171C1D),
                                    modifier = Modifier.weight(1.1f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Start
                                )
                                val durationMillis = (session.endEpochMillis ?: System.currentTimeMillis()) - session.startEpochMillis
                                val durationText = com.smylo.core.common.TimeUtils.formatDurationHMS(durationMillis)
                                Text(
                                    if (durationText.isEmpty()) "0s" else durationText,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF005D66),
                                    modifier = Modifier.weight(0.8f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                                )
                            }
                            if (index < todaySessions.size - 1) {
                                HorizontalDivider(color = Color(0xFFE0F2F7).copy(alpha = 0.5f), thickness = 1.dp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Total Non-Wear Card with Gradient
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0F2F7)),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFFB2EBF2), Color.White)
                            )
                        )
                        .padding(vertical = 32.dp, horizontal = 24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "TOTAL NON-WEAR TIME",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xFF005D66).copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val totalText = com.smylo.core.common.TimeUtils.formatDurationHMS(state.todayTotalMillis)

                    Text(
                        text = if (totalText.isEmpty()) "0s" else totalText,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF005D66),
                        letterSpacing = 1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            GradientTipCard(
                tip = "Consistency is key. 22 hours of daily wear ensures your roots move safely and effectively into their target positions."
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}


