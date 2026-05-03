package com.wakeupman.data

import android.content.Context
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Vibrator
import android.util.Log
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.lang.reflect.Field

@OptIn(ExperimentalCoroutinesApi::class)
class AlertManagerTest {

    private val context = mockk<Context>(relaxed = true)
    private val audioManager = mockk<AudioManager>(relaxed = true)
    private val vibrator = mockk<Vibrator>(relaxed = true)
    private val cameraManager = mockk<CameraManager>(relaxed = true)
    private val mediaPlayer = mockk<MediaPlayer>(relaxed = true)
    
    private lateinit var alertManager: AlertManager

    @Before
    fun setup() {
        mockkStatic(Log::class)
        every { Log.e(any<String>(), any<String>(), any<Throwable>()) } returns 0
        every { Log.e(any<String>(), any<String>()) } returns 0
        every { Log.w(any<String>(), any<String>()) } returns 0
        every { Log.d(any<String>(), any<String>()) } returns 0

        mockkStatic(RingtoneManager::class)
        every { RingtoneManager.getDefaultUri(any()) } returns mockk<Uri>()

        mockkConstructor(MediaPlayer::class)
        every { anyConstructed<MediaPlayer>().setDataSource(any<Context>(), any<Uri>()) } just runs
        every { anyConstructed<MediaPlayer>().prepare() } just runs
        every { anyConstructed<MediaPlayer>().start() } just runs
        every { anyConstructed<MediaPlayer>().stop() } just runs
        every { anyConstructed<MediaPlayer>().release() } just runs

        every { context.getSystemService(Context.AUDIO_SERVICE) } returns audioManager
        every { context.getSystemService(Context.VIBRATOR_SERVICE) } returns vibrator
        every { context.getSystemService(Context.CAMERA_SERVICE) } returns cameraManager
        every { cameraManager.cameraIdList } returns arrayOf("0")
        
        alertManager = AlertManager(context)
    }

    @Test
    fun `startEmergencyAlert sets max volume and starts vibration`() {
        val maxVolume = 15
        every { audioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM) } returns maxVolume

        alertManager.startEmergencyAlert()

        verify { audioManager.setStreamVolume(AudioManager.STREAM_ALARM, maxVolume, 0) }
        verify { vibrator.vibrate(any<LongArray>(), 0) }
    }

    @Test
    fun `stopEmergencyAlert stops vibration`() {
        alertManager.startEmergencyAlert()
        alertManager.stopEmergencyAlert()

        verify { vibrator.cancel() }
    }

    @After
    fun tearDown() {
        unmockkAll()
    }
}
