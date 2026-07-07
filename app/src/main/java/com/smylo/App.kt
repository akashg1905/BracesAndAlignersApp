package com.smylo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.smylo.core.common.ErrorMessenger
import com.smylo.core.common.SmyloErrorToastHost
import com.smylo.navigation.AppNavHost
import com.smylo.ui.theme.SmyloTheme

@Composable
fun App(errorMessenger: ErrorMessenger) {
    SmyloTheme {
        SmyloErrorToastHost(errorMessenger = errorMessenger) {
            Surface(modifier = Modifier.fillMaxSize()) {
                AppNavHost(navController = rememberNavController())
            }
        }
    }
}
