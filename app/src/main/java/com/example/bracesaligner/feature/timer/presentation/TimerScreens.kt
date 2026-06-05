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
import com.example.bracesaligner.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerDetailScreen(
    state: TimerState,
    profileImageUrl: String? = null,
    weeklySummary: List<DailyNonWearSummaryEntity>,
    onStart: () -> Unit,
    onStop: () -> Unit,
    onBack: () -> Unit,
    onNavigateToProgress: () -> Unit = {},
    onNavigateToPlan: () -> Unit = {},
    onNavigateToScan: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Clinical Sanctuary",
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
                .background(AlignerOffWhite)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                "Non-Wear Timer",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = AlignerBlack
            )
            Text(
                "Keep track of the time you spend without your aligners.",
                fontSize = 15.sp,
                color = AlignerTextGrey,
                modifier = Modifier.padding(top = 4.dp, bottom = 32.dp)
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AlignerWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "TODAY'S TOTAL",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlignerTextGrey,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${state.todayTotalMillis / 60000} min",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlignerBlack
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Warning at ${state.warningMinutes} min • Limit ${state.limitMinutes} min",
                        color = AlignerTextGrey,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = if (state.isRunning) onStop else onStart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (state.isRunning) Color(0xFFE57373) else AlignerGreen
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            if (state.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (state.isRunning) "Stop Timer" else "Start Timer",
                            color = AlignerWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "History (Last 7 Days)",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = AlignerBlack
            )
            Spacer(modifier = Modifier.height(16.dp))

            weeklySummary.forEach { summary ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = AlignerWhite)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Day ${summary.dateEpochDay}",
                            fontWeight = FontWeight.Medium,
                            color = AlignerBlack
                        )
                        Text(
                            "${summary.totalMinutes} min",
                            fontWeight = FontWeight.Bold,
                            color = AlignerGreen
                        )
                    }
                }
            }
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
            label = { Text("PLAN") },
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
