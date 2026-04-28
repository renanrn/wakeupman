# Epic 4: UI/UX Final & Onboarding de Segurança

## Epic Goal
Deliver the final polished user experience, focusing on high-contrast night usage and ensuring users configure battery optimization correctly.

## Epic Description

**Existing System Context:**
- Current relevant functionality: Core monitoring and alerting are functional.
- Technology stack: Jetpack Compose, Room Database.
- Integration points: Ties together all previous epics into a cohesive app flow.

**Enhancement Details:**
- What's being added/changed: Dark theme styling, battery optimization tutorials, incident logging, and overall polish.
- How it integrates: Wraps the core engine in a user-friendly, "Set and Forget" interface.
- Success criteria: Users successfully whitelist the app from battery optimization and understand the UI intuitively.

## Stories

1. **Story 4.1: High-Contrast Dark Theme implementation**
   - Description: Apply Carbon Black, Alert Yellow, and Emergency Red branding across all screens.
   - **Executor Assignment**: `executor: @ux-design-expert`, `quality_gate: @dev`
   - **Quality Gate Tools**: `[design_system_validation, contrast_checking]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: Aviation/Industrial safety aesthetics.

2. **Story 4.2: Educational Battery Optimization Guide**
   - Description: Specific onboarding steps to teach users how to disable battery restrictions (especially on Samsung/Xiaomi).
   - **Executor Assignment**: `executor: @ux-design-expert`, `quality_gate: @pm`
   - **Quality Gate Tools**: `[user_flow_validation]`
   - **Quality Gates**: Pre-Commit, Pre-PR
   - **Focus**: Crucial for background resilience.

3. **Story 4.3: Local Incident Log (Room database)**
   - Description: Store dates and times of drowsiness events locally for the user's review.
   - **Executor Assignment**: `executor: @data-engineer`, `quality_gate: @dev`
   - **Quality Gate Tools**: `[schema_validation, data_privacy_check]`
   - **Quality Gates**: Pre-Commit
   - **Focus**: 100% local, no cloud sync (Privacy first).

4. **Story 4.4: Safety polish and final acceptance testing**
   - Description: End-to-end testing, bug fixing, and final UX refinements.
   - **Executor Assignment**: `executor: @dev`, `quality_gate: @pm`
   - **Quality Gate Tools**: `[e2e_testing, performance_review]`
   - **Quality Gates**: Pre-Commit, Pre-PR, Pre-Deployment
   - **Focus**: Meeting the 200ms latency requirement.

## Handoff to Story Manager
"Please develop detailed user stories for the final polish epic. Key considerations: Battery optimization on Android is highly fragmented; the educational guide is critical. Ensure the incident log strictly adheres to the privacy-first on-device requirement."