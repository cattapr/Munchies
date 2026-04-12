# Munchies!

A food delivery app built with Jetpack Compose and Clean Architecture.

## Tech Stack
- **UI** — Jetpack Compose
- **Architecture** — MVVM + Clean Architecture (data, domain, feature layers)
- **DI** — Hilt
- **Networking** — Retrofit + Gson
- **Image Loading** — Coil

## Features
- Browse and multi filter restaurants
- Restaurant details displayed in a sheet with open/closed status
- Pull to refresh with in-memory caching
- Smooth animations and press interactions
- TalkBack accessibility support

## State Management
- **UiState** — a single `StateFlow` holding all screen state
- **UiEvent** — user interactions are sent from the UI to the ViewModel as sealed class events (e.g. `OnFilterSelected`, `OnRestaurantSelected`)
- **UiEffect** — one-time side effects like snackbars are emitted via `SharedFlow` to avoid re-triggering on recomposition


## Caching
The app uses a custom `CachedResult` class for in-memory caching:

- **Restaurants** — fetched once on app start, cached for the session. Pull to refresh bypasses the cache and forces a fresh fetch.
- **Filters** — unique filter IDs are collected from the restaurant list and each filter is fetched once.
- **Open status** — fetched when a restaurant details sheet is opened. If the same restaurant is opened again, the cached status is returned immediately without a new network request. Cache is bypassed when the user taps "Retry" after an error.

Concurrent requests are protected by a `Mutex` to ensure only one in-flight request exists at a time for the same resource.

## Error Handling
- **Restaurant list errors** — shown as a snackbar with a "Retry" action that refetches the restaurant list. The snackbar persists until the user retries or dismisses it.
- **Open status errors** — shown as a snackbar inside the details sheet with a "Retry" action specific to that restaurant.

Both use `SharedFlow` effects emitted from the ViewModel so errors are only shown once and don't reappear on recomposition.

---

Enjoy! 🎉
