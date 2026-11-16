# In and Out Laundry - Production Ready Android Application

## 1. Project Overview
**In and Out Laundry** is a fully production-ready, highly robust mobile application designed to streamline laundry and dry-cleaning order management. Built to professional software engineering standards, it features secure authentication (SHA-256 password hashing & Google SSO), REST API integration with ASP.NET Core Web API, real-time order tracking, sandbox payments, customer support, and offline-resilient fallback mechanisms.

## 2. Production Features & Reliability
- **Zero-Crash Architecture:** Comprehensive exception handling, coroutine supervision, and offline fallback ensure the app never crashes under network failures or invalid user inputs.
- **Security & Encryption:** Password hashing, secure token storage, and cleartext traffic separation.
- **Google Play Store Ready:** Configured with R8 code shrinking, resource optimization, and custom ProGuard keep rules for Retrofit/Gson serialization.
- **Service Catalog:** 11+ professional laundry and dry-cleaning services.
- **Order Management & Sandbox Payments:** Real-time tracking and sandbox payments (DemoCard, DemoEFT, Cash).
- **Automated Testing & CI/CD:** Unit test suite and GitHub Actions workflow.

## 3. Technology Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose & Material 3
- **Architecture:** MVVM (Model-View-ViewModel), Repository Pattern
- **Networking:** Retrofit 2, OkHttp 4, Gson Converter
- **Asynchronous Processing:** Kotlin Coroutines & Flow

## 4. Building for Google Play Store (Release Bundle)
To generate a production-ready Android App Bundle (`.aab`) for Google Play Console publishing:
```bash
./gradlew bundleRelease
```
Ensure release signing configs are configured in `app/build.gradle.kts` before uploading to the Play Console.

## 5. Automated Tests & Build
Run unit tests:
```bash
./gradlew testDebugUnitTest
```
Build debug APK:
```bash
./gradlew assembleDebug
```
