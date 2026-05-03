package com.wakeupman.domain

import android.util.Log
import com.wakeupman.data.PreferencesRepository
import com.wakeupman.data.local.IncidentDao
import com.wakeupman.data.local.IncidentEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

enum class VigilanceState {
    STANDBY, ACTIVE, WARNING, EMERGENCY
}

@Singleton
class DrowsinessEngine @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val incidentDao: IncidentDao
) {

    private val _vigilanceState = MutableStateFlow(VigilanceState.STANDBY)
    val vigilanceState: StateFlow<VigilanceState> = _vigilanceState.asStateFlow()

    private val pitchHistory = mutableListOf<PitchData>()
    private val eyeHistory = mutableListOf<EyeData>()
    
    private var baselineEyeOpenness: Float = 0.8f // Default fallback
    
    private val engineScope = CoroutineScope(Dispatchers.Default)

    init {
        engineScope.launch {
            preferencesRepository.eyeOpenBaselineFlow.collect { baseline ->
                if (baseline != null) {
                    baselineEyeOpenness = baseline
                    Log.d("DrowsinessEngine", "Baseline updated: $baselineEyeOpenness")
                }
            }
        }
    }

    data class PitchData(val timestamp: Long, val pitch: Float)
    data class EyeData(val timestamp: Long, val eyeOpenness: Float)

    fun start() {
        if (_vigilanceState.value == VigilanceState.STANDBY) {
            _vigilanceState.value = VigilanceState.ACTIVE
            pitchHistory.clear()
            eyeHistory.clear()
        }
    }

    fun stop() {
        _vigilanceState.value = VigilanceState.STANDBY
        pitchHistory.clear()
        eyeHistory.clear()
    }

    fun processFaceData(
        pitch: Float, 
        leftEyeOpenProb: Float? = null, 
        rightEyeOpenProb: Float? = null, 
        timestamp: Long = System.currentTimeMillis()
    ) {
        if (_vigilanceState.value == VigilanceState.STANDBY) return

        // 1. Process Head Pitch (Nod detection)
        pitchHistory.add(PitchData(timestamp, pitch))
        pitchHistory.removeAll { timestamp - it.timestamp > 1000 } // 1 second window

        if (pitchHistory.isNotEmpty()) {
            val maxPitchInWindow = pitchHistory.maxByOrNull { it.pitch }?.pitch ?: pitch
            val pitchDrop = maxPitchInWindow - pitch

            if (pitchDrop > 30f) {
                Log.w("DrowsinessEngine", "🚨 EMERGENCY: Head nod detected! Pitch drop: $pitchDrop")
                triggerEmergency("HEAD_NOD")
                return // Stop further processing for this frame
            }
        }

        // 2. Process Eye Openness
        if (leftEyeOpenProb != null && rightEyeOpenProb != null) {
            val avgEyeOpenness = (leftEyeOpenProb + rightEyeOpenProb) / 2f
            eyeHistory.add(EyeData(timestamp, avgEyeOpenness))
        }

        eyeHistory.removeAll { timestamp - it.timestamp > 2500 } // Slightly larger than 2s to allow > 2s calculation

        if (eyeHistory.isNotEmpty()) {
            val threshold = baselineEyeOpenness * 0.4f
            
            // Check if all frames in the last 2 seconds are below the threshold
            val twoSecondsAgo = timestamp - 2000
            
            // We need at least one frame older than 2 seconds to confirm duration
            val hasOlderFrame = eyeHistory.any { it.timestamp <= twoSecondsAgo }
            
            if (hasOlderFrame) {
                // Check if ALL frames in the entire history are below threshold
                // Wait, we only care if they have been closed continuously for 2 seconds.
                // So all frames in the last 2 seconds MUST be below threshold.
                val framesInLast2s = eyeHistory.filter { it.timestamp > twoSecondsAgo }
                val allClosedRecently = framesInLast2s.isNotEmpty() && framesInLast2s.all { it.eyeOpenness < threshold }
                
                // Also check the frame exactly around 2 seconds ago
                val olderFramesClosed = eyeHistory.filter { it.timestamp <= twoSecondsAgo }.all { it.eyeOpenness < threshold }

                if (allClosedRecently && olderFramesClosed) {
                    Log.w("DrowsinessEngine", "🚨 EMERGENCY: Eyes closed for > 2s!")
                    triggerEmergency("EYES_CLOSED")
                    return
                } else if (allClosedRecently) {
                    // Not yet 2 seconds, but closing... maybe warning?
                    if (_vigilanceState.value == VigilanceState.ACTIVE) {
                        _vigilanceState.value = VigilanceState.WARNING
                    }
                } else {
                    // Eyes are open
                    if (_vigilanceState.value == VigilanceState.WARNING) {
                        _vigilanceState.value = VigilanceState.ACTIVE
                    }
                }
            } else {
                // Not enough history, but if all frames are closed we can set warning
                val allClosed = eyeHistory.all { it.eyeOpenness < threshold }
                if (allClosed && eyeHistory.isNotEmpty()) {
                    if (_vigilanceState.value == VigilanceState.ACTIVE) {
                        _vigilanceState.value = VigilanceState.WARNING
                    }
                } else {
                    if (_vigilanceState.value == VigilanceState.WARNING) {
                        _vigilanceState.value = VigilanceState.ACTIVE
                    }
                }
            }
        }
    }

    fun triggerSimulatedEmergency() {
        triggerEmergency("SIMULATED_TEST")
    }

    private fun triggerEmergency(type: String) {
        _vigilanceState.value = VigilanceState.EMERGENCY
        pitchHistory.clear()
        eyeHistory.clear()
        
        // Log to Room Database
        engineScope.launch {
            incidentDao.insertIncident(
                IncidentEntity(
                    timestamp = System.currentTimeMillis(),
                    triggerType = type,
                    baselineUsed = baselineEyeOpenness
                )
            )
        }
    }
    
    // For testing purposes
    fun getPitchHistorySize(): Int = pitchHistory.size
    fun getEyeHistorySize(): Int = eyeHistory.size
    fun setState(state: VigilanceState) { _vigilanceState.value = state }
}
