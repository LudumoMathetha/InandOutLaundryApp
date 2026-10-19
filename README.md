# In and Out Laundry - Android Application

## 1. Project Overview
**In and Out Laundry** is a professional mobile application designed to streamline laundry and dry-cleaning order management. Users can securely register/login (with password hashing and Google SSO), browse 10+ laundry services, book pickups, track order progress in real-time, update preferences, and view in-app help documentation.

## 2. Features
- **User Authentication & Security:** Secure registration and login with SHA-256 password hashing. Google Single Sign-On (SSO) integration.
- **Service Catalog:** 10+ professional laundry services (Wash & Fold, Dry Cleaning, Express Ironing, Sneaker Cleaning, Comforter Care, etc.).
- **Order Tracking:** 10+ seeded initial orders with real-time status updates (Received, Processing, Ready, Out for Delivery, Completed).
- **Settings & Preferences:** Dark mode toggle, notification toggles, and default pickup address management.
- **User Support:** In-app Help & User Guide screen.
- **Robust Validation:** Client-side input validation preventing crashes on invalid input.
- **Automated Testing:** Unit tests for validation, authentication logic, and data repositories.
- **Continuous Integration:** GitHub Actions automated workflow for testing, linting, and building debug APKs.

## 3. Technology Stack
- **Language:** Kotlin
- **UI Framework:** Jetpack Compose & Material 3
- **Architecture:** MVVM (Model-View-ViewModel), Repository Pattern
- **Asynchronous Processing:** Kotlin Coroutines & Flow
- **Build System:** Gradle with Android Gradle Plugin (AGP) 9.4.1

## 4. Setup Instructions
1. Clone the repository and open the project in **Android Studio**.
2. Ensure you have JDK 17 configured.
3. Sync project with Gradle files.
4. Run the app on an Android Emulator or physical Android device (API 24+).

## 5. Automated Tests & Build
Run the unit tests via Gradle:
```bash
./gradlew testDebugUnitTest
```
Build debug APK:
```bash
./gradlew assembleDebug
```

## 6. Demonstration Video Voice-Over Script
1. **Introduction:** Introduce In and Out Laundry app purpose and MVVM architecture.
2. **Authentication:** Demonstrate registration, password validation, secure login, and Google SSO.
3. **Dashboard & Services:** Showcase the 10+ seeded laundry services and booking flow.
4. **Order Tracking:** View active orders and detailed status screens.
5. **Settings & Profile:** Toggle dark mode, update address, and inspect profile details.
6. **Automated Testing & CI:** Show unit test execution and GitHub Actions build pipeline.
