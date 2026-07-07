package com.smylo.feature.scan.presentation

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
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.runtime.*
import com.google.accompanist.permissions.*
import coil.compose.AsyncImage
import com.smylo.core.common.GradientTipCard
import com.smylo.core.common.ScanGuidelineItem
import com.smylo.core.common.SmyloBottomNavBar
import com.smylo.core.common.SmyloTab

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun WeeklyScanScreen(
    profileImageUrl: String? = null,
    onBack: () -> Unit,
    onStartScan: () -> Unit,
    onNavigateToProgress: () -> Unit = {},
    onNavigateToPlan: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    var showCamera by remember { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    if (showCamera) {
        CameraScanExperience(
            onClose = { showCamera = false },
            onComplete = {
                showCamera = false
                // Logic for scan completion
            }
        )
    } else {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
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
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
                    windowInsets = WindowInsets.statusBars
                )
            },
            bottomBar = {
                SmyloBottomNavBar(
                    selectedTab = SmyloTab.SCAN,
                    onTabSelected = { tab ->
                        when (tab) {
                            SmyloTab.PROGRESS -> onNavigateToProgress()
                            SmyloTab.SCHEDULE -> onNavigateToPlan()
                            SmyloTab.SCAN -> { /* Already here */ }
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(64.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    "Time for your scan!",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    "Weekly scans help our AI track your progress and ensure your teeth are moving according to plan.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                
                Spacer(modifier = Modifier.height(32.dp))
                
                GradientTipCard(tip = "Rinse your mouth with water before scanning for better clarity of tooth surfaces.")
                
                Spacer(modifier = Modifier.weight(1f))
                
                Button(
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            showCamera = true
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Start AI Scan", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

@Composable
fun CameraScanExperience(
    onClose: () -> Unit,
    onComplete: () -> Unit
) {
    var currentStep by remember { mutableStateOf(0) }
    val steps = listOf("Front View", "Left View", "Right View")

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        CameraPreview(modifier = Modifier.fillMaxSize())
        
        // Overlay
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onClose) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Close", tint = Color.White)
                }
                Text(
                    text = "Step ${currentStep + 1}/${steps.size}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(48.dp))
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Text(
                text = steps[currentStep],
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Mask/Guide (simplified)
            Box(
                modifier = Modifier
                    .size(width = 280.dp, height = 200.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Button(
                onClick = {
                    if (currentStep < steps.size - 1) {
                        currentStep++
                    } else {
                        onComplete()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (currentStep < steps.size - 1) "Capture ${steps[currentStep]}" else "Finish Scan")
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun CameraPreview(modifier: Modifier) {
    // Placeholder for CameraX PreviewView
    Box(modifier = modifier.background(Color.DarkGray))
}

