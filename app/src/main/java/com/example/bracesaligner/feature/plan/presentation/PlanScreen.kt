package com.example.bracesaligner.feature.plan.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlanSetupScreen(
    state: PlanUiState,
    onAlignerCountChange: (Int) -> Unit,
    onDaysChange: (Int) -> Unit,
    onSave: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Create Aligner Plan")
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.alignerCount.toString(),
            onValueChange = { onAlignerCountChange(it.toIntOrNull() ?: 1) },
            label = { Text("Number of Aligners (1-30)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = state.daysPerAligner.toString(),
            onValueChange = { onDaysChange(it.toIntOrNull() ?: 7) },
            label = { Text("Days per Aligner (7-10)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) { Text("Save Plan") }
        state.error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(it)
        }
    }
}
