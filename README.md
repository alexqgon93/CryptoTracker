# Crypto Tracker

Crypto Tracker is a Jetpack Compose Android app that shows the best and worst performing crypto assets over the last 24 hours, priced in Euro.

## Features

- Top 10 and lowest 10 crypto assets by 24-hour percentage change.
- Asset rows with name, symbol, percentage change, Euro price, and estimated 24-hour price delta.
- Manual refresh and retry flows.
- Layered UI, domain, data, and network boundaries.
- Unit tests for domain/data/network/UI mapping plus Compose UI tests.

## Architecture

The app uses a Staff+ multi-module architecture with Gradle-enforced boundaries:

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

## API configuration

The app reads CoinCap data from `https://rest.coincap.io/v3/`.

Create `local.properties` in the repository root and add:

```properties
api_key=YOUR_COINCAP_API_KEY
```

If no key is present, debug builds still compile with an empty key so local development fails explicitly at the API boundary instead of during Gradle configuration. Release builds do not log full network bodies.

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

## Engineering trade-offs

- **Module boundaries:** the project uses Gradle modules to keep app, feature, UI, domain, data, network, and testing responsibilities explicit.
- **REST first:** the current user experience is intentionally served through REST refreshes; speculative real-time socket code was removed until a production-ready API contract is validated.
- **Domain-first mapping:** network DTOs are converted before reaching UI state, keeping formatting and presentation concerns outside network/data layers.

## Roadmap

- Add production-ready real-time price updates once the API contract, lifecycle, retry behavior, and tests are validated.
- Add offline/stale-cache behavior and richer empty/error states.
