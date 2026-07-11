package com.smylo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.smylo.feature.auth.presentation.LoginScreen
import com.smylo.feature.auth.presentation.RegisterScreen
import com.smylo.feature.auth.presentation.AuthViewModel
import com.smylo.feature.auth.presentation.AuthFlow
import com.smylo.feature.auth.presentation.SplashScreen
import com.smylo.feature.auth.presentation.SplashViewModel
import com.smylo.feature.dashboard.presentation.DashboardScreen
import com.smylo.feature.dashboard.presentation.DashboardViewModel
import com.smylo.feature.plan.presentation.PlanSetupScreen
import com.smylo.feature.plan.presentation.PlanViewModel
import com.smylo.feature.plan.presentation.ScheduleScreen
import com.smylo.feature.profile.presentation.AccountSettingsScreen
import com.smylo.feature.profile.presentation.AccountSettingsViewModel
import com.smylo.feature.profile.presentation.ContactSupportScreen
import com.smylo.feature.profile.presentation.EditProfileScreen
import com.smylo.feature.profile.presentation.HelpSupportScreen
import com.smylo.feature.profile.presentation.ProfileScreen
import com.smylo.feature.profile.presentation.ProfileViewModel
import com.smylo.feature.scan.presentation.WeeklyScanScreen
import com.smylo.feature.timer.presentation.DailyWearDetailScreen
import com.smylo.feature.timer.presentation.TimerDetailScreen
import com.smylo.feature.timer.presentation.TimerViewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            val viewModel: SplashViewModel = hiltViewModel()
            val destination by viewModel.destination.collectAsStateWithLifecycle()
            LaunchedEffect(destination) {
                val target = destination ?: return@LaunchedEffect
                navController.navigate(target) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
            SplashScreen()
        }
        composable(Routes.AUTH_LOGIN) {
            val viewModel: AuthViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            LaunchedEffect(state.loggedIn) {
                if (state.loggedIn) {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.AUTH_LOGIN) { inclusive = true }
                    }
                }
            }
            LoginScreen(
                state = state,
                onEmailChange = viewModel::onEmailChange,
                onPhoneNumberChange = viewModel::onPhoneNumberChange,
                onOtpChange = viewModel::onOtpChange,
                onRequestOtp = { viewModel.requestOtp(AuthFlow.LOGIN) },
                onResendOtp = { viewModel.resendOtp(AuthFlow.LOGIN) },
                onVerifyOtp = { viewModel.verifyOtp(AuthFlow.LOGIN) },
                onNavigateToRegister = {
                    navController.navigate(Routes.AUTH_REGISTER)
                },
                onDismissSuccessMessage = viewModel::dismissSuccessMessage
            )
        }
        composable(Routes.AUTH_REGISTER) {
            val viewModel: AuthViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            LaunchedEffect(state.loggedIn) {
                if (state.loggedIn) {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.AUTH_REGISTER) { inclusive = true }
                    }
                }
            }
            RegisterScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onFirstNameChange = viewModel::onFirstNameChange,
                onLastNameChange = viewModel::onLastNameChange,
                onEmailChange = viewModel::onEmailChange,
                onPhoneNumberChange = viewModel::onPhoneNumberChange,
                onDateOfBirthChange = viewModel::onDateOfBirthChange,
                onOtpChange = viewModel::onOtpChange,
                onRequestOtp = { viewModel.requestOtp(AuthFlow.REGISTER) },
                onResendOtp = { viewModel.resendOtp(AuthFlow.REGISTER) },
                onVerifyOtp = { viewModel.verifyOtp(AuthFlow.REGISTER) },
                onNavigateToLogin = {
                    navController.navigate(Routes.AUTH_LOGIN) {
                        popUpTo(Routes.AUTH_REGISTER) { inclusive = true }
                    }
                },
                onDismissSuccessMessage = viewModel::dismissSuccessMessage
            )
        }
        composable(Routes.PLAN_SETUP) {
            val viewModel: PlanViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            LaunchedEffect(state.saved) {
                if (state.saved) {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.PLAN_SETUP) { inclusive = true }
                    }
                }
            }
            PlanSetupScreen(
                state = state,
                onAlignerCountChange = viewModel::updateAlignerCount,
                onDaysChange = viewModel::updateDaysPerAligner,
                onStartDateChange = viewModel::updateStartDate,
                onSave = viewModel::createPlan,
                onBack = { navController.popBackStack() },
                onNavigateToProgress = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToScan = { navController.navigate(Routes.SCAN) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.DASHBOARD) {
            val viewModel: DashboardViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            
            LaunchedEffect(state.isLoggedIn) {
                if (!state.isLoggedIn) {
                    navController.navigate(Routes.AUTH_LOGIN) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                }
            }

            DashboardScreen(
                state = state,
                onStartTimer = viewModel::startTimer,
                onStopTimer = viewModel::stopTimer,
                onOpenPlan = {
                    if (!state.planAvailable || state.isPlanExpired) {
                        navController.navigate(Routes.PLAN_SETUP)
                    } else {
                        navController.navigate(Routes.SCHEDULE)
                    }
                },
                onOpenTimerDetails = { navController.navigate(Routes.TIMER_DETAIL) },
                onOpenDailyWearDetails = { navController.navigate(Routes.DAILY_WEAR_DETAIL) },
                onOpenScan = { navController.navigate(Routes.SCAN) },
                onOpenProfile = { navController.navigate(Routes.PROFILE) },
                onLogout = viewModel::logout,
                onRefresh = viewModel::refresh
            )
        }
        composable(Routes.PROFILE) {
            val viewModel: ProfileViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            ProfileScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onLogout = { /* Handle logout via a dedicated action or shared VM */ },
                onNavigateToProfileDetails = { navController.navigate(Routes.EDIT_PROFILE) },
                onNavigateToProgress = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToPlan = {
                    if (!state.planAvailable || state.isPlanExpired) {
                        navController.navigate(Routes.PLAN_SETUP)
                    } else {
                        navController.navigate(Routes.SCHEDULE)
                    }
                },
                onNavigateToScan = { navController.navigate(Routes.SCAN) },
                onNavigateToSchedule = { navController.navigate(Routes.SCHEDULE) },
                onNavigateToAccountSettings = { navController.navigate(Routes.ACCOUNT_SETTINGS) },
                onNavigateToHelpSupport = { navController.navigate(Routes.HELP_SUPPORT) }
            )
        }
        composable(Routes.ACCOUNT_SETTINGS) {
            val viewModel: AccountSettingsViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value

            LaunchedEffect(state.loggedOut) {
                if (state.loggedOut) {
                    navController.navigate(Routes.AUTH_LOGIN) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                }
            }

            AccountSettingsScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onUpdateSetting = viewModel::updateSettingValue,
                onSaveSettings = viewModel::saveSettings,
                onLogout = viewModel::logout,
                onClearMessages = viewModel::clearMessages
            )
        }
        composable(Routes.HELP_SUPPORT) {
            val viewModel: ProfileViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            HelpSupportScreen(
                profileImageUrl = state.profileImage,
                onBack = { navController.popBackStack() },
                onNavigateToContactSupport = { navController.navigate(Routes.CONTACT_SUPPORT) }
            )
        }
        composable(Routes.CONTACT_SUPPORT) {
            ContactSupportScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.EDIT_PROFILE) {
            val viewModel: ProfileViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value

            LaunchedEffect(Unit) {
                viewModel.loadProfile()
            }

            EditProfileScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onFirstNameChange = viewModel::updateFirstName,
                onLastNameChange = viewModel::updateLastName,
                onDobChange = viewModel::updateDob,
                onImageSelected = viewModel::updateProfileImage,
                onSave = viewModel::saveProfile,
                onClearMessages = viewModel::clearMessages,
                onNavigateToProgress = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToPlan = {
                    if (!state.planAvailable || state.isPlanExpired) {
                        navController.navigate(Routes.PLAN_SETUP)
                    } else {
                        navController.navigate(Routes.SCHEDULE)
                    }
                },
                onNavigateToScan = { navController.navigate(Routes.SCAN) }
            )
        }
        composable(Routes.SCHEDULE) {
            val viewModel: PlanViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            
            ScheduleScreen(
                state = state,
                onBack = { navController.popBackStack() },
                onIncrementDays = viewModel::incrementDaysForAligner,
                onDecrementDays = viewModel::decrementDaysForAligner,
                onUpdateSchedule = viewModel::updateSchedule,
                onNavigateToProgress = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToScan = { navController.navigate(Routes.SCAN) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.SCAN) {
            val viewModel: DashboardViewModel = hiltViewModel()
            val state = viewModel.uiState.collectAsStateWithLifecycle().value
            
            WeeklyScanScreen(
                profileImageUrl = state.profileImageUrl,
                onBack = { navController.popBackStack() },
                onStartScan = { /* Handled internally in WeeklyScanScreen now */ },
                onNavigateToProgress = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToPlan = {
                    if (!state.planAvailable || state.isPlanExpired) {
                        navController.navigate(Routes.PLAN_SETUP)
                    } else {
                        navController.navigate(Routes.SCHEDULE)
                    }
                },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.TIMER_DETAIL) {
            val viewModel: TimerViewModel = hiltViewModel()
            val state = viewModel.timerState.collectAsStateWithLifecycle().value
            val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
            val weekly = viewModel.weeklySummary.collectAsStateWithLifecycle().value
            val todaySessions = viewModel.todaySessions.collectAsStateWithLifecycle().value
            val profileImageUrl = viewModel.profileImageUrl.collectAsStateWithLifecycle().value
            TimerDetailScreen(
                state = state,
                profileImageUrl = profileImageUrl,
                weeklySummary = weekly,
                todaySessions = todaySessions,
                onStart = viewModel::startTimer,
                onStop = viewModel::stopTimer,
                onBack = { navController.popBackStack() },
                onOpenDailyWearDetails = { navController.navigate(Routes.DAILY_WEAR_DETAIL) },
                onNavigateToProgress = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.DASHBOARD) { inclusive = true }
                    }
                },
                onNavigateToPlan = {
                    if (!uiState.planAvailable || uiState.isPlanExpired) {
                        navController.navigate(Routes.PLAN_SETUP)
                    } else {
                        navController.navigate(Routes.SCHEDULE)
                    }
                },
                onNavigateToScan = { navController.navigate(Routes.SCAN) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
            )
        }
        composable(Routes.DAILY_WEAR_DETAIL) {
            val viewModel: TimerViewModel = hiltViewModel()
            val selectedDate = viewModel.selectedDate.collectAsStateWithLifecycle().value
            val availableDays = viewModel.availableDays.collectAsStateWithLifecycle().value
            val sessions = viewModel.selectedDateSessions.collectAsStateWithLifecycle().value

            DailyWearDetailScreen(
                selectedDate = selectedDate,
                availableDays = availableDays,
                sessions = sessions,
                onDateSelected = viewModel::selectDate,
                onBack = { navController.popBackStack() }
            )
        }
    }
}

