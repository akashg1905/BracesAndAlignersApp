package com.example.bracesaligner

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.bracesaligner.navigation.AppNavHost
import com.example.bracesaligner.ui.theme.BracesAndAlignerTheme

@Composable
fun App() {
    BracesAndAlignerTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            AppNavHost(navController = rememberNavController())
        }
    }
}