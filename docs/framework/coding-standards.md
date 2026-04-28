# WakeUpMan Coding Standards

## 1. Kotlin Style Guide
- Follow the official Kotlin Style Guide and Android Kotlin guidelines.
- Use meaningful, descriptive variable and function names.
- Prefer immutability: use `val` instead of `var` wherever possible.
- Avoid nullable types unless absolutely necessary.

## 2. Jetpack Compose Guidelines
- Use `@Composable` functions correctly, keeping them stateless when possible.
- Lift state up: pass state and event callbacks as parameters.
- Do not perform heavy computations or side-effects directly inside Composable functions. Use `LaunchedEffect` or ViewModels.
- Name Composables using PascalCase (e.g., `VigilanceButton`).

## 3. Clean Architecture & MVVM
- **Presentation:** ViewModels should not have any reference to Android framework classes (like Context or Views).
- **Domain:** Business logic must reside in the Domain layer (UseCases/Interactors), completely decoupled from external frameworks.
- **Data:** Repositories implement interfaces defined in the Domain layer. 

## 4. Error Handling
- Never silently swallow exceptions. Log them or handle them gracefully.
- Use Kotlin's `Result` type or a custom sealed class (e.g., `sealed class Resource<T>`) to wrap success/error responses from data layers.

## 5. Coroutines & Concurrency
- Never block the Main thread.
- Inject Dispatchers (`Dispatchers.IO`, `Dispatchers.Default`, `Dispatchers.Main`) to make testing easier.
- Use `viewModelScope` for coroutines in ViewModels, and `lifecycleScope` for UI components.

## 6. Security & Privacy
- Do not log sensitive user data (e.g., camera frames, location).
- Process all ML Kit data on-device and discard frames immediately after analysis.