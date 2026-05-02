package com.wakeupman.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import javax.inject.Inject

class MLKitFaceAnalyzer @Inject constructor() : ImageAnalysis.Analyzer {

    // Configure FaceDetector for speed (performance mode, classification on)
    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    private val detector = FaceDetection.getClient(options)

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            detector.process(image)
                .addOnSuccessListener { faces ->
                    for (face in faces) {
                        val leftEyeOpenProb = face.leftEyeOpenProbability
                        val rightEyeOpenProb = face.rightEyeOpenProbability
                        
                        if (leftEyeOpenProb != null && rightEyeOpenProb != null) {
                            Log.d("MLKitFaceAnalyzer", "Left Eye: $leftEyeOpenProb, Right Eye: $rightEyeOpenProb")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("MLKitFaceAnalyzer", "Face detection failed", e)
                }
                .addOnCompleteListener {
                    // CRITICAL: Must close the image to avoid memory leak and receive next frames
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }
}
