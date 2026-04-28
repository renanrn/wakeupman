# Epic 1: Foundation, Background & Permissões

## Epic Goal
Establish the foundational Android app structure and implement a resilient background service capable of continuous monitoring to meet G2 and G5 goals.

## Epic Description

**Existing System Context:**
- Current relevant functionality: Greenfield project.
- Technology stack: Native Android (Kotlin & Jetpack Compose).
- Integration points: N/A.

**Enhancement Details:**
- What's being added/changed: Base application setup, Android 14+ permission management, and the core Foreground Service.
- How it integrates: Provides the infrastructure for CameraX and ML Kit.
- Success criteria: The background service runs uninterrupted and permissions are correctly handled.

## Stories

1. **Story 1.1: Setup Project (Kotlin/Compose) and Manifest Permissions**
   - Description: Initialize project, setup dependencies, and declare AndroidManifest permissions.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[pattern_validation, security_review]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: Minimum SDK 34, Clean Architecture baseline.

2. **Story 1.2: Implement resilient Foreground Service with persistent notification**
   - Description: Implement `camera` type Foreground Service with a persistent notification.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[service_lifecycle_validation]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: Android 14+ background rules, PartialWakeLock.

3. **Story 1.3: CameraX integration within the background service**
   - Description: Set up CameraX image analysis use case without UI preview.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @architect`
   - **Quality Gate Tools**: `[memory_leak_check, performance_profiling]`
   - **Quality Gates**: Pre-Commit
   - **Focus**: Efficient frame extraction for ML Kit.

4. **Story 1.4: Permission status dashboard**
   - Description: Create an onboarding screen to check and request Camera and Notification permissions.
   - **Executor Assignment**: `executor: @ux-design-expert`, `quality_gate: @dev`
   - **Quality Gate Tools**: `[accessibility_review]`
   - **Quality Gates**: Pre-Commit
   - **Focus**: Clear UX for critical permissions.

## Handoff to Story Manager
"Please develop detailed user stories for this greenfield epic. Key considerations: Android 14+ foreground service limitations and battery optimization guidelines. Each story must ensure the background process is not killed by the system."