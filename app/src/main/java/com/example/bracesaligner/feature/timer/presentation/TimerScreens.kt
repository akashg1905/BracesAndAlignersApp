package com.example.bracesaligner.feature.timer.presentation

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import com.example.bracesaligner.core.common.TimerState
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity
import com.example.bracesaligner.core.database.entity.NonWearSessionEntity
import com.example.bracesaligner.ui.theme.*
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
        topBar = {
            CenterAlignedTopAppBar(
                windowInsets = WindowInsets.statusBars,
                title = {
                    Text(
                        "Non-Wear Details",
                        color = AlignerGreen,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = AlignerGreen)
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(AlignerWhite)
                            .border(1.dp, AlignerGreen, CircleShape)
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
                                tint = AlignerGreen,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = AlignerWhite)
            )
        },
        bottomBar = {
            TimerBottomNavBar(
                onNavigateToProgress = onNavigateToProgress,
                onNavigateToPlan = onNavigateToPlan,
                onNavigateToScan = onNavigateToScan,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AlignerWhite)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = todayDateStr,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AlignerBlack
            )
            Text(
                "Summary of periods when aligners were removed.",
                fontSize = 15.sp,
                color = AlignerTextGrey,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FBFC)),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            "Start Time",
                            color = AlignerGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1.1f)
                        )
                        Text(
                            "End Time",
                            color = AlignerGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(1.1f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start
                        )
                        Text(
                            "Duration",
                            color = AlignerGreen,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            modifier = Modifier.weight(0.8f),
                            textAlign = androidx.compose.ui.text.style.TextAlign.End
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    todaySessions.forEach { session ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                timeFormatter.format(Date(session.startEpochMillis)).uppercase(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = AlignerBlack,
                                modifier = Modifier.weight(1.1f)
                            )
                            Text(
                                (session.endEpochMillis?.let { timeFormatter.format(Date(it)) } ?: "Ongoing").uppercase(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = AlignerBlack,
                                modifier = Modifier.weight(1.1f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Start
                            )
                            val durationMillis = (session.endEpochMillis ?: System.currentTimeMillis()) - session.startEpochMillis
                            val totalSeconds = durationMillis / 1000
                            val h = totalSeconds / 3600
                            val m = (totalSeconds % 3600) / 60
                            val s = totalSeconds % 60
                            
                            val durationText = buildString {
                                if (h > 0) append("${h}h ")
                                if (m > 0) append("${m}m ")
                                if (s > 0 || (h == 0L && m == 0L)) append("${s}s")
                            }.trim()
                            Text(
                                durationText,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlignerBlack,
                                modifier = Modifier.weight(0.8f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF1F8F9))
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Daily Total Non-Wear", color = AlignerTextGrey, fontWeight = FontWeight.Medium)
                            val totalSeconds = state.todayTotalMillis / 1000
                            val h = totalSeconds / 3600
                            val m = (totalSeconds % 3600) / 60
                            val s = totalSeconds % 60
                            
                            val totalText = buildString {
                                if (h > 0) append("${h}h ")
                                append("${m}m ")
                                append("${s}s")
                            }.trim()

                            Text(
                                totalText,
                                color = AlignerGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.End
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1).copy(alpha = 0.5f)),
                border = androidx.compose.foundation.BorderStroke(2.dp, AlignerGreen.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        "\"Consistency is key to a perfect smile. Your goal is 22 hours of wear time per day. Try to minimize non-wear sessions by planning meals and oral care.\"",
                        fontSize = 14.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = AlignerGreen,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun TimerBottomNavBar(
    onNavigateToProgress: () -> Unit,
    onNavigateToPlan: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    NavigationBar(
        containerColor = AlignerWhite,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { onNavigateToProgress() },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("PROGRESS") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = AlignerWhite,
                selectedTextColor = AlignerGreen,
                indicatorColor = AlignerGreen,
                unselectedIconColor = AlignerTextGrey,
                unselectedTextColor = AlignerTextGrey
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToPlan,
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("SCHEDULE") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = AlignerTextGrey,
                unselectedTextColor = AlignerTextGrey,
                selectedIconColor = AlignerGreen,
                selectedTextColor = AlignerGreen
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToScan,
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("SCAN") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = AlignerTextGrey,
                unselectedTextColor = AlignerTextGrey,
                selectedIconColor = AlignerGreen,
                selectedTextColor = AlignerGreen
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToProfile,
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("PROFILE") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = AlignerTextGrey,
                unselectedTextColor = AlignerTextGrey,
                selectedIconColor = AlignerGreen,
                selectedTextColor = AlignerGreen
            )
        )
    }
}
