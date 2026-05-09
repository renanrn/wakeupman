package com.wakeupman.ui.onboarding

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.ui.res.stringResource
import com.wakeupman.R
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryAlert
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BatteryOptimizationGuideScreen(
    onContinue: () -> Unit
) {
    val context = LocalContext.current
    val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
    
    // State to track if app is ignored
    var isIgnoringBatteryOptimizations by remember {
        mutableStateOf(powerManager.isIgnoringBatteryOptimizations(context.packageName))
    }

    Scaffold(
        containerColor = Color.Black
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
                    imageVector = Icons.Default.BatteryAlert,
                    contentDescription = null,
                    tint = Color.Yellow,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    stringResource(R.string.battery_guide_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    stringResource(R.string.battery_guide_desc),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(stringResource(R.string.instructions_title), fontWeight = FontWeight.Bold, color = Color.Yellow)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(stringResource(R.string.instruction_1), color = Color.White)
                    Text(stringResource(R.string.instruction_2), color = Color.White)
                    Text(stringResource(R.string.instruction_3), color = Color.White)
                }
            }

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        try {
                            val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
                                data = Uri.parse("package:${context.packageName}")
                            }
                            context.startActivity(intent)
                        } catch (e: Exception) {
                            try {
                                val fallbackIntent = Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS)
                                context.startActivity(fallbackIntent)
                            } catch (e2: Exception) {
                                val appDetailsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                    data = Uri.parse("package:${context.packageName}")
                                }
                                context.startActivity(appDetailsIntent)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Color.Black)
                ) {
                    Text(stringResource(R.string.btn_open_battery_settings), fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.ChevronRight, contentDescription = null)
                }

                Button(
                    onClick = onContinue,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Yellow, contentColor = Color.Black)
                ) {
                    Text(stringResource(R.string.btn_continue), fontWeight = FontWeight.Black)
                }
            }
        }
    }
}
