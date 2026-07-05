package com.smylo.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class SmyloTab(val label: String) {
    PROGRESS("PROGRESS"), 
    SCHEDULE("SCHEDULE"), 
    SCAN("SCAN"), 
    PROFILE("PROFILE")
}

@Composable
fun SmyloBottomNavBar(
    selectedTab: SmyloTab,
    onTabSelected: (SmyloTab) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 8.dp
    ) {
        val primaryColor = MaterialTheme.colorScheme.primary
        val inactiveColor = MaterialTheme.colorScheme.onSurfaceVariant

        val tabs = listOf(
            SmyloTab.PROGRESS to Icons.Default.Home,
            SmyloTab.SCHEDULE to Icons.Default.DateRange,
            SmyloTab.SCAN to Icons.Default.PhotoCamera,
            SmyloTab.PROFILE to Icons.Default.Person
        )

        tabs.forEach { (tab, icon) ->
            val isSelected = selectedTab == tab
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(tab) },
                icon = {
                    Box(
                        modifier = Modifier
                            .size(if (tab == SmyloTab.SCAN) 48.dp else 40.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) primaryColor else Color.Transparent),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else inactiveColor
                        )
                    }
                },
                label = { 
                    Text(
                        tab.label,
                        fontSize = 10.sp, 
                        fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold,
                        color = if (isSelected) primaryColor else inactiveColor
                    ) 
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun GradientTipCard(
    tip: String,
    modifier: Modifier = Modifier,
    title: String = "PRO TIP",
    icon: ImageVector = Icons.Default.Lightbulb
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primaryContainer
                    )
                )
            )
    ) {
        // Watermark Icon
        Icon(
            imageVector = Icons.Default.PhotoCamera,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 20.dp, y = 20.dp)
                .size(120.dp)
                .alpha(0.1f),
            tint = Color.White
        )

        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f),
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = tip,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 22.sp
                )
            }
        }
    }
}

@Composable
fun SmyloInfoCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    isGradient: Boolean = false
) {
    val boxModifier = if (isGradient) {
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )
    } else {
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    }

    Box(modifier = boxModifier) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                color = if (isGradient) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                description,
                style = MaterialTheme.typography.bodySmall,
                color = if (isGradient) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ScanGuidelineItem(number: String, title: String, description: String) {
    Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 8.dp)) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(description, color = MaterialTheme.colorScheme.onSurfaceVariant, style = MaterialTheme.typography.bodySmall)
        }
    }
}

