# WakeUpMan UI/UX Specification & Design System

## Introduction
This document defines the user experience goals, information architecture, user flows, and visual design specifications for WakeUpMan's user interface. Since this is an Android Native app (Jetpack Compose), these specifications map directly to the Compose Material Theme and custom components.

### Overall UX Goals & Principles
**Target User Personas:**
- **Fatigued Driver / Night Worker:** Users who are tired, possibly driving in dark environments, and need an application that doesn't distract them but wakes them up aggressively when needed.

**Usability Goals:**
- **"Set and Forget":** Minimal friction to start monitoring.
- **Night-time Safety:** The UI must not blind the user or cause glare before an emergency.
- **Massive Touch Targets:** When an alarm triggers, the user must be able to hit the dismiss button even with compromised motor coordination.

**Design Principles:**
1. **High Contrast, Low Glare** - Dark mode is the ONLY mode.
2. **Industrial Safety Aesthetics** - Colors communicate status (Standby, Warning, Emergency).
3. **Immediate Feedback** - Haptic and visual responses must be instant.

## Information Architecture (IA)

### Navigation Structure
- **Primary Navigation:** Single-screen focus (Dashboard) with a settings/calibration overlay.
- **Secondary Navigation:** Permission Onboarding flow (linear).

## User Flows

### Flow: Start Monitoring
**User Goal:** Begin active drowsiness monitoring.
- Step 1: Open App -> Dashboard.
- Step 2: Ensure permissions are granted.
- Step 3: Tap "Start Vigilance".
- Step 4: Screen dims, small pulsating indicator shows active status.

### Flow: Critical Alert Dismissal
**User Goal:** Stop the blaring alarm and strobe.
- Step 1: Alarm triggers (Max volume, strobe, vibration).
- Step 2: Screen turns Emergency Red with a giant "I'm Awake" button.
- Step 3: User taps anywhere on the massive button.
- Step 4: System returns to Dashboard in a paused state.

## Component Library / Design System

### Design System Approach
Native Android Jetpack Compose Material 3 Theme, highly customized for Dark Mode only.

### Core Components

**1. Vigilance Toggle Button**
- **Purpose:** Start/Stop monitoring.
- **States:** Default (Carbon Black), Active (Pulsating Alert Yellow border).
- **Usage:** Centered on the Dashboard.

**2. Critical Dismiss Button**
- **Purpose:** Stop the emergency alarm.
- **States:** Pulsating Emergency Red.
- **Usage:** Takes up 70% of the screen height during an alert.

**3. Permission Card**
- **Purpose:** Guide user to enable Battery Optimization/FGS.
- **States:** Incomplete (Neutral/Warning), Complete (Success Green).

## Branding & Style Guide

### Color Palette
| Color Type | Hex Code | Usage |
|------------|----------|-------|
| Background | `#0A0A0A` | Carbon Black - App background to prevent glare |
| Primary    | `#FFD600` | Alert Yellow - Primary actions, active status |
| Critical   | `#FF2400` | Emergency Red - The critical alert screen and stop button |
| Surface    | `#1A1A1A` | Dark Gray - Cards and secondary backgrounds |
| On-Surface | `#FFFFFF` | White - Primary text |

### Typography
- **Primary:** Roboto or system default Sans-Serif.
- **Display (Critical Screen):** Massive, Bold (e.g., 64sp).

## Animation & Micro-interactions
- **Vigilance Active:** Slow, "breathing" pulse animation (Scale 1.0 to 1.05 over 3 seconds) on the active status indicator to show the app is alive without distracting.
- **Critical Alert:** Fast, aggressive strobe effect tied to the hardware flashlight and screen background.

## Next Steps
- @dev to implement `Color.kt`, `Theme.kt` and `Type.kt` in Jetpack Compose based on this spec (Story 4.1).
- @dev to build the Permission Onboarding flow (Story 1.4 & 4.2).
