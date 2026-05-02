package com.wakeupman.ui.calibration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wakeupman.data.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CalibrationViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _calibrationState = MutableStateFlow<CalibrationState>(CalibrationState.Idle)
    val calibrationState: StateFlow<CalibrationState> = _calibrationState.asStateFlow()

    private val accumulatedProbabilities = mutableListOf<Float>()
    private var isCalibrating = false
    private var calibrationStartTime = 0L

    fun startCalibration() {
        isCalibrating = true
        accumulatedProbabilities.clear()
        calibrationStartTime = System.currentTimeMillis()
        _calibrationState.value = CalibrationState.Calibrating(0f)
    }

    fun processEyeProbabilities(leftEyeOpenProb: Float, rightEyeOpenProb: Float) {
        if (!isCalibrating) return

        val currentTime = System.currentTimeMillis()
        val elapsed = currentTime - calibrationStartTime

        // Calculate average of both eyes
        val avgProb = (leftEyeOpenProb + rightEyeOpenProb) / 2f
        accumulatedProbabilities.add(avgProb)

        if (elapsed >= 3000L) { // 3 seconds calibration
            isCalibrating = false
            finishCalibration()
        } else {
            val progress = elapsed / 3000f
            _calibrationState.value = CalibrationState.Calibrating(progress)
        }
    }

    private fun finishCalibration() {
        val finalBaseline = if (accumulatedProbabilities.isNotEmpty()) {
            accumulatedProbabilities.average().toFloat()
        } else {
            0.8f // Fallback
        }

        viewModelScope.launch {
            preferencesRepository.saveEyeOpenBaseline(finalBaseline)
            _calibrationState.value = CalibrationState.Completed(finalBaseline)
        }
    }
}

sealed class CalibrationState {
    object Idle : CalibrationState()
    data class Calibrating(val progress: Float) : CalibrationState()
    data class Completed(val baseline: Float) : CalibrationState()
}
