# WakeUpMan Product Requirements Document (PRD)

## 1. Goals and Background Context

### Goals
- **G1:** Develop an Android app capable of monitoring drowsiness signs (closed eyes) in real-time.
- **G2:** Ensure the app runs uninterrupted in the background, even with the screen off or other apps open.
- **G3:** Implement an intrusive multi-sensory alert system (variable high-intensity noise, strobe flash, and vibration).
- **G4:** Minimize false positives and excessive battery impact using efficient on-device AI (Google ML Kit).
- **G5:** Provide a simple setup experience, guiding users through critical system permissions (Battery & Foreground Service).

### Background Context
WakeUpMan is a critical safety tool for drivers, guards, and night workers. Drowsiness is a leading cause of fatal accidents. This solution focuses on the **aggressiveness of the alert** and **background resilience**. By using Google ML Kit for on-device computer vision, it ensures instant, private, and reliable monitoring.

### Change Log
| Date       | Version | Description                                              | Author     |
|------------|---------|----------------------------------------------------------|------------|
| 2026-04-27 | 1.0     | Initial PRD Creation                                     | Morgan     |
| 2026-05-03 | 1.1     | Added Dynamic Notification and Intent Fallback reqs      | Gemini CLI |

---

## 2. Requirements

### Functional Requirements
- **FR1: Multi-Signal Detection.** Process front camera stream via ML Kit to calculate `eyeOpenProbability` and 3D face orientation (Pitch, Yaw, Roll).
- **FR2: Critical Alert Trigger.** Trigger max volume alarm if:
    - Eye open probability < 40% for > 2 seconds.
    - OR head pitch exceeds 30 degrees abruptly (head nod).
- **FR3: Multi-Sensory Alert.** Combine variable frequency audio, strobe LED flash, and continuous vibration.
- **FR4: Resilient Background Mode.** Maintain active monitoring via a `camera` type Foreground Service.
- **FR5: Dynamic Calibration.** Initial calibration step to define the user's "awake" baseline.
- **FR6: Permission Management.** Onboarding flow for Camera, Notifications, and Battery Optimization bypass.
- **FR7: Dynamic Emergency Notification.** The FGS notification must visually transition to a critical state (Red color, urgent text) during alerts and utilize `FullScreenIntent` to reliably wake the device and bypass lock screen barriers.
- **FR8: Intent Compatibility Fallbacks.** Navigation to system settings (like Battery Optimization) must implement multi-level fallbacks to ensure compatibility across different Android OEMs (Samsung, Xiaomi, etc.).

### Non-Functional Requirements
- **NFR1: On-Device Processing.** 100% local analysis (Privacy first).
- **NFR2: Max Latency.** Alert must start within 200ms of detection.
- **NFR3: DND Bypass.** Capability to override "Do Not Disturb" mode for emergency alerts.
- **NFR4: Battery Target.** < 15% battery drain per hour on modern devices.
- **NFR5: Pose Robustness.** Detection must work with prescription glasses.

---

## 3. User Interface Design Goals

### UX Vision
"Set and Forget" experience. High-contrast, utility-first design for night use. Massive touch targets for fatigued users.

### Core Screens
1. **Permission Onboarding:** Step-by-step visual guide for Android 14+ permissions.
2. **Monitoring Dashboard (Home):** Small camera preview for calibration and "Start Vigilance" button.
3. **Active Vigilance Mode:** Dimmed screen with pulsating status indicator to avoid distracting drivers.
4. **Critical Alert Screen:** Vibrant red interface with a giant "I'm Awake" button.

### Branding
- **Colors:** Carbon Black, Alert Yellow, Emergency Red.
- **Style:** Industrial Safety / Aviation aesthetics.

---

## 4. Technical Assumptions

- **Repository:** Monorepo.
- **Stack:** Native Android (Kotlin & Jetpack Compose).
- **AI Engine:** Google ML Kit (Face Detection).
- **Minimum SDK:** Android 14 (API 34) to handle new Foreground Service rules.
- **Background Strategy:** `Foreground Service` + `PartialWakeLock` + Battery Optimization Whitelist.
- **Architecture:** MVVM with Clean Architecture principles.

---

## 5. Epic List

- **Epic 1: Foundation, Background & Permissões** - Basic app structure and resilient background service.
- **Epic 2: Motor de Detecção (IA) & Calibração** - ML Kit integration and drowsiness logic.
- **Epic 3: Sistema de Alerta Multi-Sensorial** - Audio, Flash, and Haptic alert implementation.
- **Epic 4: UI/UX Final & Onboarding de Segurança** - Final design polish and battery education flow.

---

## 6. Epic Details

### Epic 1: Foundation & Background
- **Story 1.1:** Setup Project (Kotlin/Compose) and Manifest Permissions.
- **Story 1.2:** Implement resilient Foreground Service with persistent notification.
- **Story 1.3:** CameraX integration within the background service.
- **Story 1.4:** Permission status dashboard.

### Epic 2: AI Engine & Calibration
- **Story 2.1:** ML Kit Eye Opening detection integration.
- **Story 2.2:** Head Pose (Pitch) detection implementation.
- **Story 2.3:** Calibration UI for user baseline.
- **Story 2.4:** Detection Logic Engine (Threshold monitor).

### Epic 3: Alert System
- **Story 3.1:** Variable Frequency Audio Alert (DND bypass).
- **Story 3.2:** Strobe Flash LED integration.
- **Story 3.3:** Emergency Haptic Feedback patterns.
- **Story 3.4:** Alert Stop UI (Big Red Button).

### Epic 4: Final UI & Onboarding
- **Story 4.1:** High-Contrast Dark Theme implementation.
- **Story 4.2:** Educational Battery Optimization Guide (Samsung/Xiaomi specific).
- **Story 4.3:** Local Incident Log (Room database).
- **Story 4.4:** Safety polish and final acceptance testing.

---

## 7. Next Steps

### UX Expert Prompt
"Review the WakeUpMan PRD, specifically the high-contrast night-use requirements and the critical alert interaction. Design the wireframes and component library in Compose, focusing on extreme usability for fatigued users."

### Architect Prompt
"Analyze the WakeUpMan PRD. Design the service architecture to ensure camera access in background on Android 14+, specifying the Foreground Service implementation and the ML Kit processing pipeline to meet the 200ms latency requirement."
