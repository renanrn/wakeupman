---
clickup:
  task_id: ""
  epic_task_id: ""
  list: "Maintenance"
  url: ""
  last_sync: ""
executor: "@dev"
quality_gate: "@architect"
quality_gate_tools: [unit_testing, android_lifecycle_check]
---

# Story Bug-1.0.1: Battery Intent Fallback and Notification Stability

## Problem Statement
The application exhibited two critical reliability issues in the v1.0 release:
1. **Battery Settings Failure:** The "OPEN BATTERY SETTINGS" button failed silently on some devices because the specific Intent was not supported by all OEMs.
2. **Unstable Notification:** The Foreground Service notification would "flicker" or fail to wake the device reliably during an emergency, lacking a distinct visual state for alerts.

## Acceptance Criteria
- [x] Implement multi-level fallback for the Battery Optimization Intent (Request -> Settings -> App Details).
- [x] Create a dynamic notification system in `VigilanceService` that changes color and text during alerts.
- [x] Utilize `FullScreenIntent` with the proper manifest permissions to ensure the device wakes up consistently.
- [x] Implement a processing lock in `DrowsinessEngine` to prevent redundant alert triggers while an emergency is active.
- [x] All unit tests must pass, confirming no regressions in detection logic.

## Tasks
- [x] Add try-catch fallback logic in `BatteryOptimizationGuideScreen.kt`.
- [x] Refactor `VigilanceService.kt` to manage notification updates via `updateNotification(state)`.
- [x] Add `USE_FULL_SCREEN_INTENT` to `AndroidManifest.xml`.
- [x] Add `VigilanceState.EMERGENCY` guard in `DrowsinessEngine.processFaceData`.
- [x] Verify stability with existing unit tests.

## Change Log
| Date | Version | Description | Author |
|---|---|---|---|
| 2026-05-03 | 1.0 | Bugfix for battery intent and notification flickering | Gemini CLI |

## Dev Agent Record

### Agent Model Used
Gemini 2.0 Flash

### Debug Log References
- `ActivityNotFoundException` handled for battery settings.
- `DrowsinessEngine` redundant re-emissions fixed.

### Completion Notes List
- The notification now turns Red during alerts.
- Redundant IA processing is paused during emergency to save resources and avoid state noise.

### File List
- `app/src/main/java/com/wakeupman/service/VigilanceService.kt`
- `app/src/main/java/com/wakeupman/ui/onboarding/BatteryOptimizationGuideScreen.kt`
- `app/src/main/java/com/wakeupman/domain/DrowsinessEngine.kt`
- `app/src/main/AndroidManifest.xml`
- `app/src/test/java/com/wakeupman/domain/DrowsinessEngineTest.kt` (stability fix)
