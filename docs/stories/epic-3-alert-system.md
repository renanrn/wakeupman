# Epic 3: Sistema de Alerta Multi-Sensorial

## Epic Goal
Implement an intrusive, multi-sensory emergency alert system to immediately wake a drowsy user, fulfilling G3.

## Epic Description

**Existing System Context:**
- Current relevant functionality: Detection Logic Engine triggering alert events.
- Technology stack: Kotlin, Android AudioManager, CameraManager (Flash), Vibrator.
- Integration points: Listens to the Detection Logic Engine state.

**Enhancement Details:**
- What's being added/changed: Maximum volume audio playback overriding DND, LED strobe flash, and continuous haptic feedback.
- How it integrates: Triggered globally when a drowsiness event is confirmed.
- Success criteria: Multi-sensory alarm activates within 200ms of detection.

## Stories

1. **Story 3.1: Variable Frequency Audio Alert (DND bypass)**
   - Description: Play high-intensity audio and implement Do Not Disturb bypass.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[audio_validation, permission_review]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: Audio focus requests, max volume enforcement.

2. **Story 3.2: Strobe Flash LED integration**
   - Description: Toggle the device flashlight rapidly during an alert.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[hardware_compatibility_check]`
   - **Quality Gates**: Pre-Commit
   - **Focus**: Safe usage of CameraManager.

3. **Story 3.3: Emergency Haptic Feedback patterns**
   - Description: Continuous vibration patterns to complement the audio.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[haptic_validation]`
   - **Quality Gates**: Pre-Commit
   - **Focus**: Aggressive vibration profiles.

4. **Story 3.4: Alert Stop UI (Big Red Button)**
   - Description: The fullscreen "I'm Awake" critical alert interface to dismiss the alarm.
   - **Executor Assignment**: `executor: @ux-design-expert`, `quality_gate: @dev`
   - **Quality Gate Tools**: `[ui_accessibility_review]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: Vibrant red interface, massive touch targets.

## Handoff to Story Manager
"Please develop detailed user stories for the Alert System. Key considerations: The system must bypass Do Not Disturb mode and operate even if the screen is off. Focus on hardware compatibility for the flashlight and vibrator."