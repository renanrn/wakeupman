# Deep Research Prompt: WakeUpMan - Android Sleep Detection

## Research Objective
Identify the most reliable technological stack for real-time drowsiness detection on Android and map strategies to ensure uninterrupted background service execution, considering energy-saving restrictions in modern APIs (30+).

## Context
Project "WakeUpMan" aims to prevent accidents and increase safety for drivers and night workers by detecting early signs of sleep and triggering an immediate alarm.

## Research Questions

### 1. Detection Technologies (Primary)
- **Computer Vision:** Which Android libraries are most efficient for real-time eye fatigue (PERCLOS) and yawn detection? (e.g., Google ML Kit, MediaPipe, OpenCV).
- **Inertial Sensors:** How to use the accelerometer and gyroscope to detect "micro-sleeps" (sudden head drops) with low power consumption?
- **Wearables:** What is the feasibility of integrating with heart rate APIs (Health Connect) to detect the transition from wakefulness to light sleep?

### 2. Android System Constraints (Critical)
- **Foreground Services:** What are the current requirements to keep the camera or sensors active in a `Foreground Service` without the system killing the process?
- **Battery Optimization:** How to navigate "Doze Mode" restrictions and manufacturer-specific customizations (Samsung, Xiaomi) that kill background apps?
- **Latency and Consumption:** What is the comparative battery impact between continuous image processing vs. motion sensor sampling?

## Methodology
- **Technical Benchmarking:** Compare latency and accuracy between on-device processing (Edge AI) vs. cloud (if applicable, though local is preferred for speed).
- **Documentation Review:** Consult Google's Power Management guidelines for Android 13/14.

## Expected Deliverables
1. **Decision Matrix:** Camera vs. Sensors vs. Wearables (Pros/Cons).
2. **Suggested Architecture:** Recommended Android service model (Service/WorkManager) for this use case.
3. **Risk Log:** Identification of scenarios where the app might fail (e.g., low light, aggressive battery saving).
