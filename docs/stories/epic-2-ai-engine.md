# Epic 2: Motor de Detecção (IA) & Calibração

## Epic Goal
Integrate Google ML Kit to process camera frames in real-time, detecting drowsiness signals efficiently to meet G1 and G4 goals.

## Epic Description

**Existing System Context:**
- Current relevant functionality: CameraX frames available from Epic 1.
- Technology stack: Kotlin, Google ML Kit (Face Detection).
- Integration points: Integrates with the CameraX image analysis use case.

**Enhancement Details:**
- What's being added/changed: ML Kit on-device processing to compute eye open probabilities and head pose.
- How it integrates: Consumes frames, outputs detection state to the Alert Engine.
- Success criteria: Accurately detects closed eyes for >2s or sudden head nods (pitch > 30 deg).

## Stories

1. **Story 2.1: ML Kit Eye Opening detection integration**
   - Description: Analyze frames to calculate `eyeOpenProbability`.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[performance_profiling, accuracy_validation]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: On-device speed, minimal battery drain.

2. **Story 2.2: Head Pose (Pitch) detection implementation**
   - Description: Track 3D head orientation to detect sudden nods.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[logic_validation]`
   - **Quality Gates**: Pre-Commit
   - **Focus**: Euler angles (Pitch, Yaw, Roll).

3. **Story 2.3: Calibration UI for user baseline**
   - Description: Initial setup screen to learn the user's normal "awake" state (e.g., with glasses).
   - **Executor Assignment**: `executor: @ux-design-expert`, `quality_gate: @dev`
   - **Quality Gate Tools**: `[ui_validation, user_testing]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: Simple feedback loop for the user.

4. **Story 2.4: Detection Logic Engine (Threshold monitor)**
   - Description: The core engine that evaluates the AI outputs against thresholds to trigger alerts.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[unit_testing, edge_case_validation]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: 200ms latency requirement, reducing false positives.

## Handoff to Story Manager
"Please develop detailed user stories for the AI engine. Key considerations: Battery impact (< 15% drain per hour target) and robust detection even with prescription glasses. Ensure unit tests cover all edge cases in the detection logic."