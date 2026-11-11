# Architecture

Crypto Tracker uses a production-style multi-module Android architecture. The module graph makes dependency direction visible and enforceable at build time.

## Module graph

| Module | Responsibility |
| --- | --- |
| `:app` | Application class, activity, manifest/resources, and top-level composition. |
| `:feature:coins` | Coins screen, ViewModel, UI contract, feature-specific components, strings, and UI tests. |
| `:core:ui` | Shared theme, design tokens, and formatting utilities. |
| `:core:domain` | Pure domain models, repository contracts, and use cases. |
| `:core:data` | Repository implementations, DTO-to-domain mappers, and data DI bindings. |
| `:core:network` | Retrofit/OkHttp clients, API interfaces, DTOs, network error mapping, and network DI. |
| `:core:testing` | Shared testing dependencies and future fixtures/fakes. |

## Dependency direction

```text
:app
  -> :feature:coins
  -> :core:data
  -> :core:ui

:feature:coins
  -> :core:domain
  -> :core:ui

:core:data
  -> :core:domain
  -> :core:network

:core:network
  -> :core:domain

:core:testing
  -> :core:domain
```

No module depends upward into `:app` or sideways into feature implementation details. Domain remains free of Android UI dependencies.

## Data flow

1. `:core:network` returns network DTOs from CoinCap REST endpoints.
2. `:core:data` maps DTOs into domain models and maps transport failures into domain errors.
3. `:core:domain` exposes use cases and repository contracts.
4. `:feature:coins` combines use cases into screen state and maps domain models into UI models.
5. `:core:ui` provides shared formatting/theme primitives used by feature UI.
6. `:app` hosts the feature entry point.

## Error model

Network failures are normalized into data failures and then mapped into domain `AppError` values. Feature UI should render states derived from domain errors and should not inspect transport-specific exceptions.

## Quality model

- Build-time module boundaries keep dependencies reviewable and prevent feature implementation details from leaking into core modules.
- Unit tests cover domain use cases, data mappers, repository behavior, network error mapping, and feature state transitions.
- Compose UI tests cover important rendering and interaction paths for feature components.
- CI runs the same core checks expected locally: unit tests, ktlint, Android lint, and debug assembly.

## Current trade-offs

- `:core:domain` is an Android library for Gradle consistency, but it is kept Android-UI-free and only exposes pure Kotlin/domain APIs.
- Real-time socket support is not part of the current production path; the app stays REST-only until the API contract, lifecycle handling, retry behavior, and tests are validated.
- Formatting belongs to UI/feature mappers, not domain models. Domain models remain reusable by non-UI consumers.
- `:core:testing` currently centralizes shared test dependencies and is ready for fixtures/fakes as module tests mature.

## Next evolution

1. Add test fixtures and fake repositories to `:core:testing`.
2. Introduce Gradle convention plugins to reduce repeated module configuration and keep quality gates consistent.
3. Add offline/stale-cache support in a dedicated data/cache boundary if product scope grows.
4. Add production-ready real-time updates only after the socket API contract and lifecycle behavior are validated.
