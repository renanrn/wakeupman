# WakeUpMan Unified Project Structure

```text
wakeupman/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/wakeupman/
│   │   │   │   ├── core/           # Dependency Injection (Hilt modules), Base classes, Extensions
│   │   │   │   ├── data/           # Repositories implementations, Room DB, DataStore, CameraX wrapper
│   │   │   │   ├── domain/         # DrowsinessEngine, Models, Interfaces, UseCases
│   │   │   │   ├── service/        # VigilanceService, Wakelock Manager, AlertManager
│   │   │   │   └── ui/             # Jetpack Compose Screens, ViewModels, Theme (Color, Type, Shape)
│   │   │   └── res/                # XML layouts (if any), Drawables, Values
│   │   ├── test/                   # Unit Tests (JUnit, MockK)
│   │   └── androidTest/            # Instrumentation Tests (Compose UI Tests, Espresso)
│   └── build.gradle.kts
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

## Module Responsibilities

- **core:** Contains application-wide utilities, Hilt module configurations for Dependency Injection, and Kotlin extension functions used across layers.
- **domain:** Pure Kotlin module containing business rules. Definitions of interfaces for repositories and hardware managers, data models, and the core `DrowsinessEngine`.
- **data:** Implementation of the data and hardware interfaces defined in the domain layer. Contains Room Database DAOs, DataStore preferences, and the CameraX frame extraction logic.
- **service:** Android-specific background execution components. Houses the Foreground Service (`VigilanceService`), the `AlertManager` for firing audio/flash/haptics, and `PartialWakeLock` management.
- **ui:** The presentation layer. Jetpack Compose UI screens, composable components, `Theme.kt`, and ViewModels.
