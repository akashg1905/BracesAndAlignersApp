package com.example.bracesaligner.feature.scan.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import com.example.bracesaligner.ui.theme.AlignerBlack
import com.example.bracesaligner.ui.theme.AlignerGreen
import com.example.bracesaligner.ui.theme.AlignerOffWhite
import com.example.bracesaligner.ui.theme.AlignerTextGrey
import com.example.bracesaligner.ui.theme.AlignerWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklyScanScreen(
    profileImageUrl: String? = null,
    onBack: () -> Unit,
    onStartScan: () -> Unit,
    onNavigateToProgress: () -> Unit = {},
    onNavigateToPlan: () -> Unit = {},
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
            ScanBottomNavBar(
                onNavigateToProgress = onNavigateToProgress,
                onNavigateToPlan = onNavigateToPlan,
                onNavigateToProfile = onNavigateToProfile
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(AlignerOffWhite)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(AlignerGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = AlignerGreen,
                    modifier = Modifier.size(64.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                "Time for your scan!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = AlignerBlack
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                "Weekly scans help our AI track your progress and ensure your teeth are moving according to plan.",
                fontSize = 15.sp,
                color = AlignerTextGrey,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            ScanGuidelineItem(
                number = "1",
                title = "Good Lighting",
                description = "Ensure you're in a well-lit room, preferably facing a window."
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ScanGuidelineItem(
                number = "2",
                title = "Clear View",
                description = "Remove your aligners and keep your lips away from your teeth."
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            ScanGuidelineItem(
                number = "3",
                title = "Steady Hands",
                description = "Hold your phone steady or use a mirror to guide yourself."
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = onStartScan,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AlignerGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Start AI Scan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun ScanGuidelineItem(number: String, title: String, description: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(AlignerGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = AlignerWhite, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = AlignerBlack)
            Text(description, color = AlignerTextGrey, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}

@Composable
fun ScanBottomNavBar(
    onNavigateToProgress: () -> Unit,
    onNavigateToPlan: () -> Unit,
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
            label = { Text("PROGRESS") },
            colors = NavigationBarItemDefaults.colors(
                unselectedIconColor = AlignerTextGrey,
                unselectedTextColor = AlignerTextGrey,
                selectedIconColor = AlignerGreen,
                selectedTextColor = AlignerGreen
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
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            label = { Text("SCAN") },
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
