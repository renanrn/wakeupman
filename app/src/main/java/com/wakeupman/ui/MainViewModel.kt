package com.wakeupman.ui

import androidx.lifecycle.ViewModel
import com.wakeupman.domain.DrowsinessEngine
import com.wakeupman.domain.VigilanceState
import com.wakeupman.service.ServiceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val drowsinessEngine: DrowsinessEngine,
    private val serviceManager: ServiceManager
) : ViewModel() {

    val vigilanceState: StateFlow<VigilanceState> = drowsinessEngine.vigilanceState

    fun toggleVigilance(enable: Boolean) {
        if (enable) {
            serviceManager.startVigilance()
        } else {
            serviceManager.stopVigilance()
        }
    }

    fun triggerTestAlert() {
        drowsinessEngine.triggerSimulatedEmergency()
    }
}
