package com.example.bracesaligner.feature.plan.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.bracesaligner.core.common.AlignerScheduleItem
import com.example.bracesaligner.core.common.TimeUtils
import com.example.bracesaligner.ui.theme.AlignerBlack
import com.example.bracesaligner.ui.theme.AlignerGreen
import com.example.bracesaligner.ui.theme.AlignerOffWhite
import com.example.bracesaligner.ui.theme.AlignerTextGrey
import com.example.bracesaligner.ui.theme.AlignerWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    state: PlanUiState,
    onBack: () -> Unit,
    onIncrementDays: (String) -> Unit = {},
    onDecrementDays: (String) -> Unit = {},
    onUpdateSchedule: () -> Unit = {},
    onNavigateToProgress: () -> Unit = {},
    onNavigateToScan: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val items = state.scheduleItems
    val profileImageUrl = state.profileImageUrl
    
    val hasChanges by remember(state.scheduleItems, state.originalScheduleItems) {
        derivedStateOf {
            state.scheduleItems.any { item ->
                val original = state.originalScheduleItems.find { it.id == item.id }
                original != null && item.daysForAligner != original.daysForAligner
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Aligner Schedule",
                        color = Color(0xFF006064), // Darker teal from SS
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AlignerWhite)
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = AlignerOffWhite)
            )
        },
        bottomBar = {
            ScheduleBottomNavBar(
                onNavigateToProgress = onNavigateToProgress,
                onNavigateToScan = onNavigateToScan,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(AlignerOffWhite)) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
                    Text(
                        "Active Treatment",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlignerBlack
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "Customize your journey. Adjusting durations will automatically recalibrate your entire schedule.",
                        fontSize = 13.sp,
                        color = AlignerTextGrey,
                        lineHeight = 18.sp
                    )
                }

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(items) { item ->
                        AlignerScheduleCard(
                            item = item,
                            onIncrement = { onIncrementDays(item.id) },
                            onDecrement = { onDecrementDays(item.id) }
                        )
                    }
                    
                    item {
                        DynamicRecalibrationCard()
                    }
                    
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // Update Button at bottom
            if (hasChanges) {
                Button(
                    onClick = onUpdateSchedule,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(20.dp)
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00695C)),
                    shape = RoundedCornerShape(28.dp),
                    enabled = !state.isUpdating
                ) {
                    if (state.isUpdating) {
                        CircularProgressIndicator(color = AlignerWhite, modifier = Modifier.size(24.dp))
                    } else {
                        Icon(Icons.Default.Update, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Update Schedule", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun AlignerScheduleCard(
    item: AlignerScheduleItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    val today = TimeUtils.todayEpochDay()
    val isCompleted = !item.isCurrent && item.endEpochDay < today

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AlignerWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "PHASE ${item.alignerNumber.toString().padStart(2, '0')}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlignerTextGrey.copy(alpha = 0.6f),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Aligner ${item.alignerNumber}",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlignerBlack
                    )
                }

                StatusBadge(isCompleted = isCompleted, isCurrent = item.isCurrent)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Date Range Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(AlignerOffWhite.copy(alpha = 0.5f))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = null,
                    tint = AlignerTextGrey.copy(alpha = 0.6f),
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${item.startDate} — ${item.endDate}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = AlignerBlack
                )
                
                if (isCompleted) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Default.Update,
                        contentDescription = null,
                        tint = AlignerTextGrey.copy(alpha = 0.4f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${item.daysForAligner} Days",
                        fontSize = 13.sp,
                        color = AlignerTextGrey
                    )
                }
            }

            if (!isCompleted) {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Treatment Duration",
                        fontSize = 14.sp,
                        color = AlignerTextGrey
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(AlignerOffWhite)
                            .padding(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        IconButton(
                            onClick = onDecrement,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Remove, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                        
                        Text(
                            text = "${item.daysForAligner} Days",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp)
                        )

                        IconButton(
                            onClick = onIncrement,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatusBadge(isCompleted: Boolean, isCurrent: Boolean) {
    val backgroundColor = when {
        isCompleted -> Color(0xFFF5F5F5)
        isCurrent -> Color(0xFFB2EBF2)
        else -> Color(0xFFEEEEEE)
    }
    val contentColor = when {
        isCompleted -> AlignerTextGrey
        isCurrent -> Color(0xFF00ACC1)
        else -> AlignerTextGrey
    }
    val text = when {
        isCompleted -> "COMPLETED"
        isCurrent -> "CURRENT"
        else -> "UPCOMING"
    }
    val icon = when {
        isCompleted -> Icons.Default.CheckCircle
        isCurrent -> Icons.Default.Layers 
        else -> Icons.Default.Update 
    }

    Surface(
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = text,
                color = contentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun DynamicRecalibrationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = AlignerWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, AlignerOffWhite)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0F7FA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFF00838F),
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    "Dynamic Recalibration",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlignerBlack
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Adjusting your current or future aligner duration will automatically shift all subsequent start and end dates to maintain orthodontic precision.",
                    fontSize = 13.sp,
                    color = AlignerTextGrey,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun ScheduleBottomNavBar(
    onNavigateToProgress: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    NavigationBar(
        containerColor = AlignerWhite,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToProgress,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("DASHBOARD") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = AlignerTextGrey,
                unselectedTextColor = AlignerTextGrey,
                selectedIconColor = AlignerGreen,
                selectedTextColor = AlignerGreen
            )
        )
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("SCHEDULE") },
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
            onClick = onNavigateToScan,
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("INSIGHTS") },
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
