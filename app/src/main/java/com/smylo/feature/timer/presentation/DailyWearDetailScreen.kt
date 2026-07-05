package com.smylo.feature.timer.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smylo.core.common.GradientTipCard
import com.smylo.core.common.TimeUtils
import com.smylo.core.database.entity.NonWearSessionEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyWearDetailScreen(
    selectedDate: Long,
    availableDays: List<Long>,
    sessions: List<NonWearSessionEntity>,
    onDateSelected: (Long) -> Unit,
    onBack: () -> Unit
) {
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMMM yyyy", Locale.getDefault()) }
    val dayNameFormatter = remember { DateTimeFormatter.ofPattern("EEE", Locale.getDefault()) }
    val dayNumberFormatter = remember { DateTimeFormatter.ofPattern("d", Locale.getDefault()) }
    val timeFormatter = remember { DateTimeFormatter.ofPattern("hh:mm:ss a", Locale.getDefault()) }

    val selectedLocalDate = remember(selectedDate) { LocalDate.ofEpochDay(selectedDate) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "DAILY WEAR DETAIL",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            letterSpacing = 1.sp
                        )
                        Text(
                            selectedLocalDate.format(monthFormatter),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onPrimaryContainer)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color(0xFFF4F9FB))
            )
        },
        containerColor = Color(0xFFF4F9FB)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Horizontal Calendar
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(availableDays.sorted()) { epochDay ->
                    val date = LocalDate.ofEpochDay(epochDay)
                    val isSelected = epochDay == selectedDate
                    
                    Box(
                        modifier = Modifier
                            .width(64.dp)
                            .height(84.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else Color.White)
                            .border(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else Color(0xFFE0F2F7),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onDateSelected(epochDay) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                date.format(dayNameFormatter).uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) Color.White.copy(alpha = 0.7f) else MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                date.format(dayNumberFormatter),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                            )
                            if (isSelected) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(modifier = Modifier.size(4.dp).clip(CircleShape).background(Color.White))
                            }
                        }
                    }
                }
            }

            // Total Time Off Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF5F7)),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        "TOTAL TIME OFF",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        letterSpacing = 0.5.sp
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val totalMillis = sessions.sumOf { (it.endEpochMillis ?: System.currentTimeMillis()) - it.startEpochMillis }
                        val h = totalMillis / 3600000
                        val m = (totalMillis % 3600000) / 60000
                        val s = (totalMillis % 60000) / 1000
                        
                        Text(
                            text = buildAnnotatedString {
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                                    append("${h}h ")
                                }
                                withStyle(SpanStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) {
                                    append("${m}m ")
                                    append("${s}s")
                                }
                            },
                            style = MaterialTheme.typography.headlineLarge.copy(
                                platformStyle = PlatformTextStyle(includeFontPadding = false),
                                fontSize = 28.sp
                            ),
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            softWrap = false,
                            modifier = Modifier.weight(1f)
                        )
                        
                        Column(
                            horizontalAlignment = Alignment.End,
                            modifier = Modifier.padding(bottom = 2.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    "On Track",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                "Under daily limit of 2h",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Time Off Log Table
            Text(
                "Time Off Log",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(0.dp),
                border = BorderStroke(1.dp, Color(0xFFEAF5F7))
            ) {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(scrollState)
                ) {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .widthIn(min = 400.dp) // Ensure a minimum width for the table content
                            .fillMaxWidth()
                            .background(Color(0xFFEAF5F7))
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "START TIME",
                            modifier = Modifier.weight(1.2f),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            "END TIME",
                            modifier = Modifier.weight(1.2f),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            "DURATION",
                            modifier = Modifier.weight(0.8f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.End,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }

                    if (sessions.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "No sessions recorded",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color(0xFF6D797B)
                            )
                        }
                    } else {
                        sessions.forEachIndexed { index, session ->
                            Row(
                                modifier = Modifier
                                    .widthIn(min = 400.dp)
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val start = Instant.ofEpochMilli(session.startEpochMillis)
                                    .atZone(ZoneId.systemDefault())
                                    .format(timeFormatter)
                                val end = session.endEpochMillis?.let { 
                                    Instant.ofEpochMilli(it)
                                        .atZone(ZoneId.systemDefault())
                                        .format(timeFormatter) 
                                } ?: "--"
                                val durationMillis = (session.endEpochMillis ?: System.currentTimeMillis()) - session.startEpochMillis

                                Text(
                                    text = start,
                                    modifier = Modifier.weight(1.2f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = end,
                                    modifier = Modifier.weight(1.2f),
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = TimeUtils.formatDurationHMS(durationMillis),
                                    modifier = Modifier.weight(0.8f),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                            if (index < sessions.lastIndex) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = Color(0xFFEAF5F7)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Daily Compliance Summary
            ComplianceSummaryCard(sessions)

            Spacer(modifier = Modifier.height(24.dp))

            GradientTipCard(
                tip = "Consistency is key. Aim for 22 hours of wear time daily for optimal results.",
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ComplianceSummaryCard(sessions: List<NonWearSessionEntity>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8EEF0)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "DAILY COMPLIANCE SUMMARY",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFF6D797B),
                letterSpacing = 1.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val totalNonWearMillis = sessions.sumOf { (it.endEpochMillis ?: System.currentTimeMillis()) - it.startEpochMillis }
                val totalDayMillis = 24 * 60 * 60 * 1000L
                val wearMillis = (totalDayMillis - totalNonWearMillis).coerceAtLeast(0)
                
                val wearPercentage = (wearMillis.toFloat() / totalDayMillis.toFloat()) * 100
                val progressColor = MaterialTheme.colorScheme.onPrimaryContainer
                val strokeWidth = with(androidx.compose.ui.platform.LocalDensity.current) { 10.dp.toPx() }
                
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                    Canvas(modifier = Modifier.size(100.dp)) {
                        drawArc(
                            color = Color.White,
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = progressColor,
                            startAngle = -90f,
                            sweepAngle = 3.6f * wearPercentage,
                            useCenter = false,
                            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "😊",
                            fontSize = 20.sp
                        )
                        Text(
                            text = "${wearPercentage.toInt()}%",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = progressColor
                        )
                    }
                }
                
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        ComplianceRow(
                            label = "Wear Time",
                            value = TimeUtils.formatDurationHMS(wearMillis),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            isBullet = true
                        )
                        ComplianceRow(
                            label = "Time Off",
                            value = TimeUtils.formatDurationHMS(totalNonWearMillis),
                            color = MaterialTheme.colorScheme.outline,
                            isBullet = false
                        )
                }
            }
        }
    }
}

@Composable
fun ComplianceRow(label: String, value: String, color: Color, isBullet: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.width(160.dp)
    ) {
        if (isBullet) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        } else {
            Box(modifier = Modifier.size(8.dp).border(1.dp, color, CircleShape))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF6D797B),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = if (isBullet) color else MaterialTheme.colorScheme.onSurface
        )
    }
}

