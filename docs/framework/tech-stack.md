# WakeUpMan Technology Stack

## 1. Core Platform
- **OS Target:** Android 14+ (API 34+)
- **Language:** Kotlin 1.9+
- **Architecture Pattern:** Clean Architecture + MVVM

## 2. Presentation Layer
- **UI Toolkit:** Jetpack Compose (Material 3)
- **Navigation:** Jetpack Compose Navigation
- **Asynchronous Data:** Kotlin Coroutines & StateFlow

## 3. Computer Vision & ML
- **Camera Abstraction:** CameraX (Camera2 under the hood)
- **Face Detection:** Google ML Kit Face Detection (On-device)
- **Frame Processing:** YUV_420_888 ImageAnalysis Use Case

## 4. Background Execution
- **Service:** Foreground Service (Type: camera | specialUse)
- **Wake Management:** PartialWakeLock

## 5. Hardware Interfacing
- **Audio:** AudioManager (STREAM_ALARM, DND Bypass)
- **Flashlight:** CameraManager (setTorchMode)
- **Haptics:** Vibrator / VibratorManager

## 6. Local Persistence
- **Incident Logs:** Room Database
- **Preferences/Calibration:** Jetpack DataStore (Preferences)

## 7. Dependency Injection
- **DI Framework:** Hilt / Dagger

## 8. Testing
- **Unit Tests:** JUnit 4 / JUnit 5, MockK
- **UI Tests:** Compose UI Testing
- **E2E Tests:** Espresso