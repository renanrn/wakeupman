package com.wakeupman.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupman.data.local.IncidentDao
import com.wakeupman.data.local.IncidentEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val incidentDao: IncidentDao
) : ViewModel() {

    val incidents: Flow<List<IncidentEntity>> = incidentDao.getAllIncidents()

    fun clearHistory() {
        viewModelScope.launch {
            incidentDao.clearAll()
        }
    }
}
