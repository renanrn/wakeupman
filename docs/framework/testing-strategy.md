# WakeUpMan Testing Strategy

## 1. Overview
The testing strategy for WakeUpMan focuses on high reliability of the core Drowsiness Engine and the Alert System. Due to the critical safety nature of the application, tests must ensure that detection logic triggers under the specified 200ms latency requirement and that background services do not fail silently.

## 2. Unit Testing (Domain & Core Logic)
- **Target:** 80%+ code coverage for the `domain` module.
- **Tools:** JUnit 4/5, MockK, Coroutines Test library.
- **Focus Areas:**
  - `DrowsinessEngine`: Ensure state machines correctly transition from "Awake" -> "Warning" -> "Emergency" based on mock sequences of `eyeOpenProbability` and head `pitch`.
  - Ensure calibration logic correctly sets baselines.

## 3. Integration Testing (Data & Service)
- **Tools:** AndroidX Test, Room Testing.
- **Focus Areas:**
  - Room Database: Verify that incident logs are correctly saved and retrieved.
  - AlertManager: Mock the `AudioManager` and `Vibrator` to ensure that triggers from the engine result in correct hardware API calls (including max volume and DND bypass logic).

## 4. UI Testing (Jetpack Compose)
- **Tools:** Compose UI Testing framework.
- **Focus Areas:**
  - Verify that the "Start Vigilance" button correctly changes state.
  - Verify the Critical Alert screen (Massive Red Button) appears and intercepts clicks properly.
  - Test Permission Onboarding flows (mocking granted/denied states).

## 5. End-to-End (E2E) & Performance Testing
- **Tools:** Espresso, Macrobenchmark.
- **Focus Areas:**
  - Ensure the `VigilanceService` stays alive under memory pressure.
  - Test the full loop: Mock CameraX frame input -> ML Kit -> Engine -> Alert Trigger.
  - **Latency SLA:** Verify the time from frame ingestion to alert trigger is < 200ms.