package com.example.bracesaligner.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.bracesaligner.feature.auth.presentation.AuthScreen
import com.example.bracesaligner.feature.auth.presentation.AuthViewModel
import com.example.bracesaligner.feature.auth.presentation.SplashScreen
import com.example.bracesaligner.feature.auth.presentation.SplashViewModel
import com.example.bracesaligner.feature.dashboard.presentation.DashboardScreen
import com.example.bracesaligner.feature.dashboard.presentation.DashboardViewModel
import com.example.bracesaligner.feature.plan.presentation.PlanSetupScreen
import com.example.bracesaligner.feature.plan.presentation.PlanViewModel
import com.example.bracesaligner.feature.plan.presentation.ScheduleScreen
import com.example.bracesaligner.feature.profile.presentation.EditProfileScreen
import com.example.bracesaligner.feature.profile.presentation.ProfileScreen
import com.example.bracesaligner.feature.profile.presentation.ProfileViewModel
import com.example.bracesaligner.feature.scan.presentation.WeeklyScanScreen
import com.example.bracesaligner.feature.timer.presentation.TimerDetailScreen
import com.example.bracesaligner.feature.timer.presentation.TimerViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            val viewModel: SplashViewModel = hiltViewModel()
            val destination = viewModel.destination.collectAsStateWithLifecycle().value
            LaunchedEffect(destination) {
                val target = destination ?: return@LaunchedEffect
                navController.navigate(target) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
            SplashScreen()
        }
        composable(Routes.AUTH) {
            val viewModel: AuthViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            LaunchedEffect(state.loggedIn) {
                if (state.loggedIn) {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.AUTH) { inclusive = true }
                    }
                }
            }
            AuthScreen(
                state = state,
                onEmailChange = viewModel::onEmailChange,
                onPhoneNumberChange = viewModel::onPhoneNumberChange,
                onOtpChange = viewModel::onOtpChange,
                onRequestOtp = viewModel::requestOtp,
                onVerifyOtp = viewModel::verifyOtp
            )
        }
        composable(Routes.PLAN_SETUP) {
            val viewModel: PlanViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            LaunchedEffect(state.saved) {
                if (state.saved) navController.popBackStack()
            }
            PlanSetupScreen(
                state = state,
                onAlignerCountChange = viewModel::updateAlignerCount,
                onDaysChange = viewModel::updateDaysPerAligner,
                onSave = viewModel::createPlan
            )
        }
        composable(Routes.DASHBOARD) {
            val viewModel: DashboardViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            
            LaunchedEffect(state.isLoggedIn) {
                if (!state.isLoggedIn) {
                    navController.navigate(Routes.AUTH) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                }
            }

            DashboardScreen(
                state = state,
                onStartTimer = viewModel::startTimer,
                onStopTimer = viewModel::stopTimer,
                onOpenPlan = { navController.navigate(Routes.PLAN_SETUP) },
                onOpenTimerDetails = { navController.navigate(Routes.TIMER_DETAIL) },
                onOpenScan = { navController.navigate(Routes.SCAN) },
                onOpenProfile = { navController.navigate(Routes.PROFILE) },
                onLogout = viewModel::logout,
                onRefresh = viewModel::refresh
            )
        }
        composable(Routes.PROFILE) {
            val viewModel: DashboardViewModel = hiltViewModel()
            ProfileScreen(
                onBack = { navController.popBackStack() },
                onLogout = viewModel::logout,
                onNavigateToProfileDetails = { navController.navigate(Routes.EDIT_PROFILE) },
                onNavigateToProgress = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToPlan = { navController.navigate(Routes.PLAN_SETUP) },
                onNavigateToScan = { navController.navigate(Routes.SCAN) },
                onNavigateToSchedule = { navController.navigate(Routes.SCHEDULE) }
            )
        }
        composable(Routes.EDIT_PROFILE) {
            val viewModel: ProfileViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            EditProfileScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onFirstNameChange = viewModel::updateFirstName,
                onLastNameChange = viewModel::updateLastName,
                onDobChange = viewModel::updateDob,
                onSave = viewModel::saveProfile
            )
        }
        composable(Routes.SCHEDULE) {
            val viewModel: PlanViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            
            LaunchedEffect(Unit) {
                viewModel.fetchSchedule()
            }
            
            ScheduleScreen(
                items = state.scheduleItems,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.SCAN) {
            WeeklyScanScreen(
                onBack = { navController.popBackStack() },
                onStartScan = { /* TODO: Launch AI scanning experience */ }
            )
        }
        composable(Routes.TIMER_DETAIL) {
            val viewModel: TimerViewModel = hiltViewModel()
            val state = viewModel.timerState.collectAsStateWithLifecycle().value
            val weekly = viewModel.weeklySummary.collectAsStateWithLifecycle().value
            TimerDetailScreen(
                state = state,
                weeklySummary = weekly,
                onStart = viewModel::startTimer,
                onStop = viewModel::stopTimer
            )
        }
    }
}
