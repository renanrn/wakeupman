package com.wakeupman.ui.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.stringResource
import com.wakeupman.R
import com.wakeupman.data.local.IncidentEntity
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: HistoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val incidents by viewModel.incidents.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.incident_log_title), fontWeight = FontWeight.Bold, color = Color.Yellow) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back_desc), tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.clearHistory() }) {
                        Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.clear_desc), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        containerColor = Color.Black
    ) { padding ->
        if (incidents.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.no_incidents), color = Color.Gray)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(incidents) { incident ->
                    IncidentItem(incident)
                }
            }
        }
    }
}

@Composable
fun IncidentItem(incident: IncidentEntity) {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
    val dateString = sdf.format(Date(incident.timestamp))

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (incident.triggerType == "EYES_CLOSED") stringResource(R.string.alert_eyes_closed) else stringResource(R.string.alert_head_nod),
                    color = Color.Red,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text(text = dateString, color = Color.Gray, fontSize = 12.sp)
            }
            Text(
                text = stringResource(R.string.baseline_label, "%.2f".format(incident.baselineUsed)),
                color = Color.Yellow,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
