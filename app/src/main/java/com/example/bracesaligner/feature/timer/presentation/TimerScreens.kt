package com.example.bracesaligner.feature.timer.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bracesaligner.core.common.TimerState
import com.example.bracesaligner.core.database.entity.DailyNonWearSummaryEntity

@Composable
fun TimerDetailScreen(
    state: TimerState,
    weeklySummary: List<DailyNonWearSummaryEntity>,
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Non-Wear Timer")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Today's total: ${state.todayTotalMillis / 60000} min")
        Text("Warning at ${state.warningMinutes} min, limit ${state.limitMinutes} min")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = if (state.isRunning) onStop else onStart, modifier = Modifier.fillMaxWidth()) {
            Text(if (state.isRunning) "Stop Timer" else "Start Timer")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Last 7 Days")
        weeklySummary.forEach { summary ->
            Text("Day ${summary.dateEpochDay}: ${summary.totalMinutes} min")
        }
    }
}
