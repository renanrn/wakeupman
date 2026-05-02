package com.wakeupman.domain

import android.util.Log
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DrowsinessEngine @Inject constructor() {

    private val pitchHistory = mutableListOf<PitchData>()
    
    data class PitchData(val timestamp: Long, val pitch: Float)

    fun processFaceData(pitch: Float, timestamp: Long = System.currentTimeMillis()) {
        pitchHistory.add(PitchData(timestamp, pitch))

        // Keep only history within the last 1 second (1000 ms)
        pitchHistory.removeAll { timestamp - it.timestamp > 1000 }

        if (pitchHistory.isNotEmpty()) {
            val maxPitchInWindow = pitchHistory.maxByOrNull { it.pitch }?.pitch ?: pitch
            val pitchDrop = maxPitchInWindow - pitch

            // ML Kit headEulerAngleX: Positive value is when the face is turned upward.
            // A sudden drop indicates a head nod (microsleep).
            if (pitchDrop > 30f) {
                Log.w("DrowsinessEngine", "🚨 EMERGENCY: Head nod detected! Pitch drop: $pitchDrop")
                // Trigger alert logic will be added here
                
                // Clear history to avoid multiple triggers for the same nod
                pitchHistory.clear()
            }
        }
    }
    
    // For testing purposes
    fun getHistorySize(): Int = pitchHistory.size
}
