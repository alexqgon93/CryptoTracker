# Welcome to the Bitpanda Android code challenge

## Objective:

Develop an Android application that displays a list of the best/worst performing crypto coins in the
last 24 hours. You can use the provided template to kick start you implementation or start from
scratch.

## Requirements:

* Display a list of the top 10 crypto coins based on price change percentage over the last 24 hours.
* Provide a way to switch between the 10 best and 10 worst performing crypto coins
* Each list items should contain the following asset info:
    * Name
    * Symbol
    * Change % (format: xx.xx%)
    * Price in Euro
* Provide an option to refresh the list using fresh data
* BONUS: Make the prices update via socket events

## Resources:

Use the following public API to implement the task: https://docs.coincap.io/

* Fetching of all the assets: https://api.coincap.io/v2/assets
* Converting price to Euro: https://api.coincap.io/v2/rates
* *BONUS: price updates: wss://ws.coincap.io/prices?assets=bitcoin*

## Deliverables:

* Source code of the Android application
* Documentation explaining the implementation details and the architecture of the application.
* Optionally, provide a demo video or screenshots showcasing the functionality of the application.

## Submission:

* You can push your code in the repository provided in this classroom.

## Additional Notes:

* Follow Android development best practices and architectural guidelines.
* Take care of error handling
* Use reactive programing practices.
* Make sure the application has good test coverage.
* *BONUS: Use Jetpack Compose for the UI.*
* *BONUS: Feel free to add extra features or enhancements to demonstrate your Android development
  skills and creativity.*

**Good Luck!!**

**From Bitpanda Android team**

---

## Architecture & Implementation Details

### 1. Clean Architecture (ui → domain → data → network)

- Layered separation with unidirectional dependency flow.
- ui: ViewModels, UI state, event handling, lightweight `CoinUIModel` plus mapping/formatting.
- domain: Pure Kotlin entities, use cases (`AssetsUseCase`, `RatesUseCase`), sealed `AppError`.
- data: Repository implementations converting raw network NetworkModels (DTO's) into domain models,
  centralizing
  error translation.
- network: Retrofit for the endpoints and ktor for the websocket service interfaces + DTO
  definitions (kept inside data for
  simplicity due to app size).
- No multi-module split (intentionally) to avoid overhead; boundaries make later extraction trivial.

### 2. Error Handling

- All low-level exceptions mapped to `AppError` variants.
- ViewModel reacts by exposing `ScreenState` (`LOADING`, `SUCCESS`, `ERROR`) and supports retry.

### 3. Mapping Flow

`NetworkModels (DTO's) → Domain Model → Adjusted Domain (EUR conversion if rate found) → UI Model (formatted fields)`

### 4. Version Catalog

- Centralized dependency + version management via `libs.versions.toml`.
- Benefits: single source of truth, reduced duplication, easier upgrades, future-ready for
  modularization.

### 5. Testing Strategy

- Achieved thorough coverage across domain, data, network, and UI (Jetpack Compose) components.
- Domain logic tested for all success and error scenarios, including edge cases and error mapping.
- Data and network layers validated by stubbing remote sources, simulating various responses and failures.
- UI and ViewModel tests assert state transitions, sorting, filtering, and retry flows.
- Mapper correctness ensured by capturing and verifying input/output arguments, including EUR conversion and top/bottom selection.
- Used mockk to isolate dependencies, enabling focused and reliable unit tests.
- Coroutines controlled with test dispatchers or polling helpers for deterministic, fast execution.
- Structure supports easy extension to future UI or integration tests.

### 6. Rationale For Single Module

- Small scope: faster iteration, simpler navigation, only one single composable, reduced Gradle
  config.
- Architectural seams preserved for effortless future module extraction (`:domain`, `:data`, `:ui`).

### 7. Extensibility

- Easy to add:
    - Caching layer (Room or in-memory) without touching UI/domain.
    - More fiat conversions (extend rate selection logic).
- Clear separation encourages incremental complexity without refactoring core layers.

### 8. Quality & Maintainability

- Consistent state handling pattern.
- Pure domain layer enables high test coverage.
- Version catalog + clear naming conventions streamline maintenance.

### 9. Future Improvements (Optional)

- Introduce pagination or search.
- Enable WebSocket real-time updates (given a API key tier 2+).
- Extract modules when feature set grows.
- Implement offline cache with stale/refresh policy.

---

## How To Run

1. Add in local.properties file
2. Sync Gradle (version catalog initializes dependencies).
3. Launch app: standard Android Studio Run configuration.

## How To Test

- All unit tests reside under `app/src/test/...`
- Execute: `./gradlew testDebugUnitTest`
- Extend tests by following existing ViewModel test patterns (state assertion after async load).

---

## Summary

The project emphasizes clarity, testability, and readiness for scaling. Clean layering, unified
error handling, comprehensive tests, and centralized dependency management establish a solid
foundation while avoiding premature complexity.