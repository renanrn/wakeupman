package com.wakeupman.data

import android.content.Context
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    
    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private var cameraId: String? = null

    private var mediaPlayer: MediaPlayer? = null
    
    private var flashJob: Job? = null
    private var isAlerting = false

    init {
        try {
            cameraId = cameraManager.cameraIdList.firstOrNull { id ->
                cameraManager.getCameraCharacteristics(id)
                    .get(android.hardware.camera2.CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            }
        } catch (e: Exception) {
            Log.e("AlertManager", "Failed to access camera for flash", e)
        }
    }

    fun startEmergencyAlert() {
        if (isAlerting) return
        isAlerting = true

        startAudioAlert()
        startVibrationAlert()
        startFlashAlert()
    }

    fun stopEmergencyAlert() {
        if (!isAlerting) return
        isAlerting = false

        stopAudioAlert()
        stopVibrationAlert()
        stopFlashAlert()
    }

    private fun startAudioAlert() {
        try {
            // Bypass DND if permission is granted, otherwise try our best
            val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
            audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0)

            val alertUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM) 
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE)

            mediaPlayer = MediaPlayer().apply {
                setDataSource(context, alertUri)
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                isLooping = true
                prepare()
                start()
            }
        } catch (e: Exception) {
            Log.e("AlertManager", "Failed to start audio alert", e)
        }
    }

    private fun stopAudioAlert() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
        mediaPlayer = null
    }

    private fun startVibrationAlert() {
        val pattern = longArrayOf(0, 500, 200, 500, 200)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(pattern, 0))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(pattern, 0)
        }
    }

    private fun stopVibrationAlert() {
        vibrator.cancel()
    }

    private fun startFlashAlert() {
        if (cameraId == null) return
        
        flashJob = CoroutineScope(Dispatchers.Default).launch {
            var flashOn = false
            while (isActive) {
                try {
                    cameraManager.setTorchMode(cameraId!!, flashOn)
                    flashOn = !flashOn
                    delay(300) // Strobe effect speed
                } catch (e: CameraAccessException) {
                    Log.e("AlertManager", "Torch mode error", e)
                    break
                }
            }
            // Ensure flash is off when cancelled
            try {
                cameraManager.setTorchMode(cameraId!!, false)
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    private fun stopFlashAlert() {
        flashJob?.cancel()
        flashJob = null
        try {
            cameraId?.let { cameraManager.setTorchMode(it, false) }
        } catch (e: Exception) {
            // Ignore
        }
    }
}
