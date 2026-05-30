package com.example.bracesaligner.feature.profile.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bracesaligner.R
import com.example.bracesaligner.ui.theme.AlignerGreen
import com.example.bracesaligner.ui.theme.AlignerTextGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onNavigateToProfileDetails: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToPlan: () -> Unit,
    onNavigateToScan: () -> Unit,
    onNavigateToSchedule: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.titleLarge,
                        color = AlignerGreen,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = AlignerGreen
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                        // Placeholder for top-right small profile image
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            ProfileBottomNavBar(
                onNavigateToProgress = onNavigateToProgress,
                onNavigateToPlan = onNavigateToPlan,
                onNavigateToScan = onNavigateToScan
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Profile Picture with Verification Badge
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(AlignerGreen.copy(alpha = 0.1f))
                        .padding(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    ) {
                         // Actual Image would go here
                    }
                }
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(AlignerGreen)
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Account Overview Section
            Text(
                text = "ACCOUNT OVERVIEW",
                modifier = Modifier.align(Alignment.Start),
                style = MaterialTheme.typography.labelLarge,
                color = AlignerTextGrey,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileMenuItem(
                icon = Icons.Default.DateRange,
                title = "Aligner Schedule",
                onClick = onNavigateToSchedule
            )
            ProfileMenuItem(
                icon = Icons.Default.Person,
                title = "Profile",
                onClick = onNavigateToProfileDetails
            )
            ProfileMenuItem(
                icon = Icons.Default.Settings,
                title = "Account Settings",
                onClick = {}
            )
            ProfileMenuItem(
                icon = Icons.Default.Info,
                title = "Help & Support",
                onClick = {}
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(onClick = onLogout) {
                Text(
                    "Log Out",
                    color = Color(0xFFB03939),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "VERSION 2.4.0 • CLINICAL SANCTUARY OS",
                style = MaterialTheme.typography.labelSmall,
                color = AlignerTextGrey.copy(alpha = 0.6f)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F7F7))
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = AlignerGreen)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Color(0xFF1A1C1E)
            )
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = AlignerTextGrey
            )
        }
    }
}

@Composable
fun ProfileBottomNavBar(
    onNavigateToProgress: () -> Unit,
    onNavigateToPlan: () -> Unit,
    onNavigateToScan: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToProgress,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("PROGRESS") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = AlignerTextGrey,
                unselectedTextColor = AlignerTextGrey
            )
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToPlan,
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("PLAN") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = AlignerTextGrey, unselectedTextColor = AlignerTextGrey)
        )
        NavigationBarItem(
            selected = false,
            onClick = onNavigateToScan,
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("SCAN") },
            colors = NavigationBarItemDefaults.colors(unselectedIconColor = AlignerTextGrey, unselectedTextColor = AlignerTextGrey)
        )
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("PROFILE") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = AlignerGreen,
                indicatorColor = AlignerGreen,
                unselectedIconColor = AlignerTextGrey,
                unselectedTextColor = AlignerTextGrey
            )
        )
    }
}
