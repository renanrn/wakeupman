package com.wakeupman.ui.onboarding

import android.Manifest
import android.os.Build
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import androidx.compose.ui.res.stringResource
import com.wakeupman.R
import com.wakeupman.ui.theme.AlertYellow
import com.wakeupman.ui.theme.CarbonBlack
import com.wakeupman.ui.theme.IndustrialGray

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PermissionOnboardingScreen(
    viewModel: PermissionViewModel = hiltViewModel(),
    onAllPermissionsGranted: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    // Refresh permission states when user returns to the app
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.updatePermissionStates()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val permissionsToRequest = mutableListOf(Manifest.permission.CAMERA)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissionsToRequest.add(Manifest.permission.POST_NOTIFICATIONS)
    }

    val permissionState = rememberMultiplePermissionsState(permissionsToRequest)

    // Check if everything is granted to proceed
    if (viewModel.areAllCriticalPermissionsGranted()) {
        LaunchedEffect(Unit) {
            onAllPermissionsGranted()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.onboarding_title), 
                        style = MaterialTheme.typography.headlineMedium,
                        color = AlertYellow
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = CarbonBlack
                )
            )
        },
        containerColor = CarbonBlack
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null,
                    tint = AlertYellow,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.safety_access_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    stringResource(R.string.safety_access_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                PermissionItem(
                    icon = Icons.Default.CameraAlt,
                    title = stringResource(R.string.camera_access_title),
                    description = stringResource(R.string.camera_access_desc),
                    isGranted = uiState.isCameraGranted
                )
                
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    PermissionItem(
                        icon = Icons.Default.Notifications,
                        title = stringResource(R.string.notifications_title),
                        description = stringResource(R.string.notifications_desc),
                        isGranted = uiState.isNotificationsGranted
                    )
                }

                PermissionItem(
                    icon = Icons.Default.PowerSettingsNew,
                    title = stringResource(R.string.battery_optimization_title),
                    description = stringResource(R.string.battery_optimization_desc),
                    isGranted = uiState.isBatteryOptimizationDisabled
                )
            }

            val allPermissionsGranted = permissionState.allPermissionsGranted
            val shouldShowSettings = !allPermissionsGranted && !permissionState.shouldShowRationale

            Button(
                onClick = { 
                    if (!allPermissionsGranted) {
                        if (shouldShowSettings) {
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        } else {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    } else if (!uiState.isBatteryOptimizationDisabled) {
                        // If only battery is missing, we could navigate or open battery settings directly
                        onAllPermissionsGranted()
                    } else {
                        onAllPermissionsGranted()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AlertYellow,
                    contentColor = CarbonBlack
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = when {
                        shouldShowSettings -> stringResource(R.string.btn_open_app_settings)
                        !allPermissionsGranted -> stringResource(R.string.btn_grant_access)
                        else -> stringResource(R.string.btn_next_step)
                    },
                    style = MaterialTheme.typography.headlineMedium.copy(fontSize = 18.sp)
                )
            }
        }
    }
}

@Composable
fun PermissionItem(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isGranted) Color(0xFF1B5E20) else IndustrialGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (isGranted) Color.Green else Color.White,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                title,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                description,
                color = Color.Gray,
                fontSize = 12.sp
            )
        }
    }
}
