package com.wakeupman.ui.dashboard

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Vibration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wakeupman.domain.VigilanceState
import com.wakeupman.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    vigilanceState: VigilanceState,
    onToggleVigilance: (Boolean) -> Unit,
    onNavigateToHistory: () -> Unit,
    onTriggerTestAlert: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "WAKEUPMAN", 
                        style = MaterialTheme.typography.headlineMedium,
                        color = AlertYellow
                    ) 
                },
                actions = {
                    IconButton(onClick = onTriggerTestAlert) {
                        Icon(Icons.Default.Vibration, contentDescription = "Test Alert", tint = AlertYellow)
                    }
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "History", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CarbonBlack)
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
            verticalArrangement = Arrangement.SpaceAround
        ) {
            // Status Indicator with Pulse
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(CircleShape)
                        .background(
                            color = when (vigilanceState) {
                                VigilanceState.STANDBY -> DimGray
                                VigilanceState.ACTIVE -> SafetyGreen.copy(alpha = alpha)
                                VigilanceState.WARNING -> AlertYellow.copy(alpha = alpha)
                                VigilanceState.EMERGENCY -> EmergencyRed.copy(alpha = alpha)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = vigilanceState.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (vigilanceState == VigilanceState.STANDBY) Color.White else CarbonBlack
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = when (vigilanceState) {
                        VigilanceState.STANDBY -> "Vigilance is OFFLINE"
                        VigilanceState.ACTIVE -> "System Monitoring..."
                        VigilanceState.WARNING -> "FATIGUE WARNING!"
                        VigilanceState.EMERGENCY -> "CRITICAL ALERT!"
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = when (vigilanceState) {
                        VigilanceState.STANDBY -> Color.Gray
                        VigilanceState.ACTIVE -> SafetyGreen
                        VigilanceState.WARNING -> AlertYellow
                        VigilanceState.EMERGENCY -> EmergencyRed
                    }
                )
            }

            // Industrial Start/Stop Toggle
            Button(
                onClick = { onToggleVigilance(vigilanceState == VigilanceState.STANDBY) },
                modifier = Modifier
                    .size(220.dp)
                    .padding(8.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (vigilanceState == VigilanceState.STANDBY) AlertYellow else EmergencyRed,
                    contentColor = CarbonBlack
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 12.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (vigilanceState == VigilanceState.STANDBY) Icons.Default.PlayArrow else Icons.Default.Stop,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        text = if (vigilanceState == VigilanceState.STANDBY) "ENGAGE" else "DISENGAGE",
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 20.sp)
                    )
                }
            }

            Surface(
                color = IndustrialGray,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Ensure device is mounted with a clear view of your face.",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.LightGray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}
