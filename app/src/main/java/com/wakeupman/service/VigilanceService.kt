package com.wakeupman.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleService
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Inject

@AndroidEntryPoint
class VigilanceService : LifecycleService() {

    @Inject lateinit var faceAnalyzer: com.wakeupman.data.MLKitFaceAnalyzer

    private var wakeLock: PowerManager.WakeLock? = null
    private val CHANNEL_ID = "vigilance_service_channel"
    private val NOTIFICATION_ID = 1

    private lateinit var cameraExecutor: ExecutorService
    private var imageAnalyzer: ImageAnalysis? = null

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        acquireWakeLock()
        startForegroundService()
        
        // Start CameraX
        startCamera()
        
        return START_STICKY
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, HeadlessAnalyzer())
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to this LifecycleService
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, imageAnalyzer
                )

                Log.d("VigilanceService", "CameraX successfully bound to lifecycle.")

            } catch (exc: Exception) {
                Log.e("VigilanceService", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun startForegroundService() {
        val notification = createNotification()
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val foregroundServiceType = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA or ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            } else {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_CAMERA
            }
            startForeground(NOTIFICATION_ID, notification, foregroundServiceType)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("WakeUpMan")
            .setContentText("Vigilance Active - Monitoring drowsiness")
            .setSmallIcon(android.R.drawable.ic_menu_camera)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setOngoing(true)
            .build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Vigilance Service Channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Used for WakeUpMan background drowsiness monitoring"
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    private fun acquireWakeLock() {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "WakeUpMan::VigilanceWakeLock").apply {
            acquire()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        wakeLock?.let {
            if (it.isHeld) {
                it.release()
            }
        }
    }
}
vate class HeadlessAnalyzer : ImageAnalysis.Analyzer {
        override fun analyze(imageProxy: ImageProxy) {
            // Placeholder: Log the image format and dimensions to verify delivery
            Log.d("HeadlessAnalyzer", "Frame received: ${imageProxy.width}x${imageProxy.height}, format: ${imageProxy.format}")
            
            // CRITICAL: Must close the image to avoid memory leak and receive next frames
            imageProxy.close()
        }
    }
}
