# 📰 NewsApp

![Compose](https://img.shields.io/badge/Jetpack%20Compose-Material3-green.svg)
![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-blue.svg)
![API](https://img.shields.io/badge/API-NewsAPI.org-orange.svg)

A comprehensive, robust Android news application built with modern Android development standards. This app allows users to browse breaking news, search for specific topics, filter by categories, and save articles for offline reading. It features a sophisticated offline-caching mechanism ensuring a seamless user experience even without internet connectivity.

<div align="center">
    
  <h3>
     <a href="https://github.com/Ahmedsayed0895/News_App/releases/latest">Download Latest APK</a>
  </h3>
  <p>Run the app immediately without compiling (CI/CD Automated Build)</p>

</div>

---

<img width="1024" height="474" alt="news app poster" src="https://github.com/user-attachments/assets/2f7e7e06-8cc0-45a0-b518-9d11a0a0a042" />


---

##  Key Features

* **Real-time News Updates:** Fetches top headlines and category-based news from NewsAPI.
* **Smart Search:** Live search functionality with **Debouncing** to minimize API calls and handle user input efficiently.
* **Infinite Scrolling (Pagination):** seamless data loading using custom pagination logic to handle large datasets efficiently.
* **Offline Support (Caching):**
    * Caches news locally using **Room Database**.
    * **Offline-First approach:** Users can view previously loaded news and saved articles without an internet connection.
    * Intelligent fallback mechanism: Tries to fetch from Network -> Fails -> Fetches from Cache.
* **Bookmarks System:** Save articles to "Favorites" stored locally.
* **User Authentication:**
    * Sign Up & Login simulation.
    * Persistent Session Management using `SharedPreferences`.
    * Profile Screen with secure Logout functionality.
* **Rich UI/UX:**
    * **Jetpack Compose** & Material 3 Design.
    * **Pull-to-Refresh** capability.
    * Smooth Navigation Transitions (Slide animations).
    * Custom Error Handling & User-friendly messages.
* **WebView Integration:** Read full articles directly within the app.

---

## Tech Stack & Libraries

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel)
* **Dependency Injection:** [Hilt](https://dagger.dev/hilt/)
* **Networking:**
    * [Retrofit2](https://square.github.io/retrofit/) & OkHttp3
    * GSON Converter
* **Local Storage:**
    * [Room Database](https://developer.android.com/training/data-storage/room) (SQLite abstraction)
    * DataStore / SharedPreferences (Session management)
* **Asynchronous Programming:** [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) & [Flow](https://kotlinlang.org/docs/flow.html)
* **Image Loading:** [Coil](https://coil-kt.github.io/coil/) (with Disk Caching enabled)
* **Navigation:** [Navigation Compose](https://developer.android.com/jetpack/compose/navigation)
* **Animation:** Lottie & Compose Animation API.

---

## Architecture Overview

The application follows the **Clean Architecture** principles to ensure separation of concerns, testability, and scalability.

1.  **Presentation Layer (UI):**
    * Composables (`HomeScreen`, `DetailsScreen`).
    * ViewModels (`HomeViewModel`): Holds the UI state (`StateFlow`) and handles UI logic.
2.  **Domain Layer:**
    * **Repository Interface:** Defines the contract for data operations.
3.  **Data Layer:**
    * **Repository Implementation:** Decides whether to fetch data from API or Local DB (Single Source of Truth).
    * **Data Sources:**
        * *Remote:* Retrofit Service (NewsAPI).
        * *Local:* Room DAO & SharedPreferences.
---


## Getting Started

### Prerequisites
* Android Studio Ladybug (or newer).
* JDK 17+.
* [NewsAPI](https://newsapi.org/) Key.

### Installation
1.  Clone the repository:
    ```bash
    git clone https://github.com/Ahmedsayed0895/News_App.git
    ```
2.  Open the project in **Android Studio**.
3.  Navigate to `local.properties` (or your Constants file) and add your API Key:
    ```properties
    API_KEY="YOUR_API_KEY_HERE"
    ```
4.  Sync Gradle and Run on an Emulator or Physical Device.

---
