# Crypto Tracker

An Android app that shows the best and worst performing crypto assets of the last 24 hours, priced in euro. I built it the way I would start a finance-facing product: market data is treated as untrusted input at the boundary, failures are typed instead of swallowed, and every layer can be tested on its own.

Built as a take-home exercise and then polished.

## Features

- Top 10 and lowest 10 crypto assets by 24-hour percentage change.
- Asset rows with name, symbol, percentage change, Euro price, and estimated 24-hour price delta.
- Manual refresh and retry flows.
- Layered UI, domain, data, and network boundaries.
- Unit tests for domain/data/network/UI mapping plus Compose UI tests.

## Architecture

The app uses a production-style multi-module architecture with Gradle-enforced boundaries:

```text
:app
:feature:coins
:core:ui
:core:domain
:core:data
:core:network
:core:testing
```

High-level flow:

`network DTOs -> data mappers/repository -> domain models/use cases -> feature state -> Compose UI`

See [`docs/architecture.md`](docs/architecture.md) for the layer responsibilities, error model, and module evolution path.

Key technologies:

- Kotlin, Coroutines, and Jetpack Compose.
- Hilt for dependency injection.
- Retrofit, OkHttp, and Moshi for REST APIs.
- Arrow `Either` for domain/data error propagation.
- Gradle version catalog for dependency management.
- ktlint, JUnit 5, MockK, Kotest assertions, and Compose UI tests.

## Engineering highlights

- **Explicit module ownership:** app, feature, UI, domain, data, network, and testing concerns are split into focused Gradle modules.
- **Unidirectional data flow:** transport DTOs are mapped into domain models before feature state reaches Compose UI.
- **Typed error propagation:** network/data failures are normalized into domain-level `AppError` values before presentation.
- **Reviewable quality gates:** local and CI checks cover unit tests, ktlint, Android lint, and debug assembly.

## API configuration

The app reads CoinCap data from `https://rest.coincap.io/v3/`.

Create `local.properties` in the repository root and add:

```properties
api_key=YOUR_COINCAP_API_KEY
```

If no key is present, debug builds still compile with an empty key so local development fails explicitly at the API boundary instead of during Gradle configuration. Network logging is limited in debug builds and disabled in release builds.

## How to run

1. Install Android Studio with the Android SDK matching `compileSdk`.
2. Add `api_key` to `local.properties`.
3. Sync Gradle.
4. Run the `app` configuration.

## Quality gates

Run the local quality checks before opening a pull request:

```bash
./gradlew testDebugUnitTest ktlintCheck
```

Useful additional checks:

```bash
./gradlew lintDebug assembleDebug
```

The same checks are configured in GitHub Actions under `.github/workflows/android-quality.yml`.

## Decisions and trade-offs

- **Typed errors, not silent failures.** Network and data failures are normalized into domain-level `AppError` values (Arrow `Either`) before they reach the UI, so the screen can offer a proper retry instead of an empty list. Cost: more mapping code than catching exceptions in the ViewModel.
- **Domain models, not DTOs.** Transport DTOs are mapped before they reach UI state, so formatting and presentation stay out of the network and data layers. Cost: mapper boilerplate. In exchange, an API change touches one layer.
- **REST first, real-time later.** The current experience is served by REST refreshes. I removed speculative socket code until the API contract, lifecycle, retry behaviour and tests are validated: a price feed that reconnects badly is worse than a manual refresh. Cost: no live prices.
- **Fail at the API boundary.** A missing API key still compiles, so the failure shows up where the request is made instead of during Gradle configuration. Network logging is limited in debug builds and off in release.

## Roadmap

- Add production-ready real-time price updates once the API contract, lifecycle, retry behavior, and tests are validated.
- Add offline/stale-cache behavior and richer empty/error states.
