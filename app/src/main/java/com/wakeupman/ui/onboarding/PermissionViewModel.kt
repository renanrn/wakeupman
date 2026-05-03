package com.wakeupman.ui.onboarding

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.PowerManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PermissionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(PermissionUiState())
    val uiState: StateFlow<PermissionUiState> = _uiState.asStateFlow()

    init {
        updatePermissionStates()
    }

    fun updatePermissionStates() {
        val isCameraGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        val isNotificationsGranted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }

        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        val isBatteryOptimized = !powerManager.isIgnoringBatteryOptimizations(context.packageName)

        _uiState.update {
            it.copy(
                isCameraGranted = isCameraGranted,
                isNotificationsGranted = isNotificationsGranted,
                isBatteryOptimizationDisabled = !isBatteryOptimized
            )
        }
    }

    fun areAllCriticalPermissionsGranted(): Boolean {
        return _uiState.value.isCameraGranted && 
               _uiState.value.isNotificationsGranted && 
               _uiState.value.isBatteryOptimizationDisabled
    }
}

data class PermissionUiState(
    val isCameraGranted: Boolean = false,
    val isNotificationsGranted: Boolean = false,
    val isBatteryOptimizationDisabled: Boolean = false
)
