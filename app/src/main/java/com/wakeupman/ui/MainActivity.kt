package com.wakeupman.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.wakeupman.ui.calibration.CalibrationScreen
import com.wakeupman.ui.dashboard.DashboardScreen
import com.wakeupman.ui.history.HistoryScreen
import com.wakeupman.ui.onboarding.BatteryOptimizationGuideScreen
import com.wakeupman.ui.onboarding.PermissionOnboardingScreen
import com.wakeupman.ui.theme.WakeUpManTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WakeUpManTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WakeUpManAppContent()
                }
            }
        }
    }
}

@Composable
fun WakeUpManAppContent(viewModel: MainViewModel = hiltViewModel()) {
    var appStep by remember { mutableStateOf(AppStep.PERMISSIONS) }
    val vigilanceState by viewModel.vigilanceState.collectAsState()

    when (appStep) {
        AppStep.PERMISSIONS -> {
            PermissionOnboardingScreen(
                onAllPermissionsGranted = {
                    appStep = AppStep.BATTERY_GUIDE
                }
            )
        }
        AppStep.BATTERY_GUIDE -> {
            BatteryOptimizationGuideScreen(
                onContinue = {
                    appStep = AppStep.CALIBRATION
                }
            )
        }
        AppStep.CALIBRATION -> {
            CalibrationScreen(
                onCalibrationFinished = {
                    appStep = AppStep.DASHBOARD
                }
            )
        }
        AppStep.DASHBOARD -> {
            DashboardScreen(
                vigilanceState = vigilanceState,
                onToggleVigilance = { enable ->
                    viewModel.toggleVigilance(enable)
                },
                onNavigateToHistory = {
                    appStep = AppStep.HISTORY
                }
            )
        }
        AppStep.HISTORY -> {
            HistoryScreen(
                onNavigateBack = {
                    appStep = AppStep.DASHBOARD
                }
            )
        }
    }
}

enum class AppStep {
    PERMISSIONS, BATTERY_GUIDE, CALIBRATION, DASHBOARD, HISTORY
}
