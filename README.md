# ☕ Roast Buddy

> **Your personal coffee discovery companion** — find the perfect roast, track what you've tried, and deepen your coffee knowledge.

![Android](https://img.shields.io/badge/Platform-Android-green) ![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue) ![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-brightgreen) ![Firebase](https://img.shields.io/badge/Database-Firebase-orange)
 
---

## 📱 Overview

Roast Buddy is a fully polished Android coffee discovery app that helps coffee lovers find their perfect roast. Instead of overwhelming users with choices, it guides them through a personalized quiz, explains every recommendation, and provides tools to track, learn, and build their coffee journey over time.

On first launch, users create a personal profile that personalizes their entire experience — from the home screen greeting to brew method preferences. All screens are organized into individual Kotlin files following clean MVVM architecture, with comprehensive code comments throughout.
 
---

## ✨ Features

| Feature | Description |
|---|---|
| 👤 **User Profile** | One-time setup collecting name, brew method, and experience level — saved with DataStore and shown on every launch |
| 🎯 **Roast Finder Quiz** | 3-question quiz mapping brew method, flavor preference, and strength to a personalized coffee recommendation with explanation |
| 📦 **Artisanal Catalog** | 6 curated coffees fetched from Firebase Firestore with roast filters, search, detail views, and brew recommendations |
| ❤️ **Wishlist** | Save favorite coffees to a persistent on-device wishlist powered by Room database |
| 📓 **Tasting Journal** | Log coffees with roast level, 1–5 star rating, tasting notes, and auto-stamped date — persisted with Room |
| 📊 **Coffee Stats** | Personal analytics — total coffees tried, average rating, favorite roast, top rated coffee, and roast breakdown bars |
| 🎓 **Education Hub** | 6 curated articles and videos with scrollable category filters, opening directly in browser |
 
---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Navigation** | Navigation Compose (10 destinations) |
| **State Management** | ViewModel + StateFlow |
| **User Preferences** | DataStore (user profile persistence) |
| **Local Database** | Room v2 (Journal entries + Wishlist) |
| **Cloud Database** | Firebase Firestore (Coffee catalog) |
| **AI / Networking** | Gemini API via OkHttp (with rule-based fallback) |
| **Architecture** | Single-activity, MVVM |
 
---

## 🗂 Project Structure

```
com.example.rooastbuddy/
│
├── MainActivity.kt              # App entry point + NavHost + RoastBuddyApp
│
├── ── ViewModels ──
├── CoffeeViewModel.kt           # Quiz logic, Firestore catalog, AI recommendation
├── JournalViewModel.kt          # Journal + Wishlist state management
├── ProfileViewModel.kt          # User profile state management
│
├── ── Data Layer ──
├── UserPreferences.kt           # DataStore — user profile persistence
├── AppDatabase.kt               # Room database instance (version 2)
├── JournalDao.kt                # Room DAO — journal + wishlist queries
├── JournalEntry.kt              # Room entity — tasting journal
├── WishlistItem.kt              # Room entity — wishlist
│
├── ── Screens ──
├── HomeScreen.kt                # Personalized home with feature grid
├── QuizScreen.kt                # 3-question Roast Finder Quiz
├── ResultScreen.kt              # AI-powered recommendation result
├── CatalogScreen.kt             # Firestore catalog with filters + search
├── CoffeeCard.kt                # Reusable catalog item card
├── CoffeeDetailScreen.kt        # Coffee detail view + wishlist toggle
├── JournalScreen.kt             # Tasting journal list
├── AddEntryForm.kt              # Inline journal entry form
├── WishlistScreen.kt            # Saved coffees wishlist
├── StatsScreen.kt               # Personal coffee analytics dashboard
├── EducationScreen.kt           # Articles + videos hub
├── ProfileSetupScreen.kt        # First-launch onboarding
├── ProfileScreen.kt             # Profile view + edit
│
└── ui/theme/
    ├── Color.kt                 # Pine green + coffee brown palette
    ├── Theme.kt                 # MaterialTheme configuration
    └── Type.kt                  # Typography
```
 
---

## 🚀 Getting Started

### Prerequisites

- Android Studio Hedgehog or later
- Android SDK 24+
- A Firebase project with Firestore enabled
- A Google AI Studio API key (Gemini)
### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/erdkash1/Roast_Buddy.git
   cd Roast_Buddy
   ```

2. **Add Firebase config**
    - Create a project at [console.firebase.google.com](https://console.firebase.google.com)
    - Add an Android app with package name `com.example.rooastbuddy`
    - Download `google-services.json` and place it in the `app/` folder
3. **Add your Gemini API key**
    - Get a free key at [aistudio.google.com](https://aistudio.google.com)
    - Open `CoffeeViewModel.kt` and replace `YOUR_API_KEY_HERE` with your key
4. **Seed Firestore**
    - Create a collection called `coffees` in Firestore
    - Add documents with fields: `name`, `roast`, `notes`, `description`
      | name | roast | notes | description |
      |---|---|---|---|
      | Ethiopian Yirgacheffe | Light | Bright & Citrusy | Delicate citrus and floral notes, perfect for pour-over |
      | Colombian Supremo | Medium | Chocolate & Nutty | Smooth caramel sweetness with a clean finish |
      | Sumatra Mandheling | Dark | Earthy & Bold | Full body, low acidity, cedar and dark chocolate |
      | Guatemala Antigua | Medium | Caramel & Spicy | Rich cocoa and brown sugar with a hint of cinnamon |
      | Kenya AA | Light | Berry & Wine | Bold blackcurrant and tomato acidity with a winey finish |
      | Brazil Santos | Dark | Nutty & Smooth | Low acidity with walnut and milk chocolate undertones |
5. **Build and run**
   ```bash
   ./gradlew assembleDebug
   ```
   Or open in Android Studio and click **Run**.
---

## 🏗 Architecture

```
UI Layer (Jetpack Compose — 13 screen files)
                    ↕
     ViewModel Layer (MVVM — 3 ViewModels)
        ↕                ↕               ↕
Firebase Firestore   Room Database    DataStore
(Coffee Catalog)  (Journal+Wishlist) (User Profile)
        ↕
  Gemini AI API
(Recommendations)
```
 
---

## 📱 App Flow

```
First Launch → Profile Setup → Home Screen
                                    ↓
              ┌─────────────────────┼──────────────────────┐
           Quiz                  Catalog               Journal
              ↓                     ↓                     ↓
         Recommendation          Detail View           Add Entry
              ↓                     ↓                     ↓
         Back to Home            Wishlist              Stats
                                                          ↓
                                                    Education Hub
```
 
---

## 🔮 Future Extensions

- **Full AI integration** — live Gemini API with proper quota management and billing
- **Firebase Authentication** — user accounts to sync data across devices
- **Mock checkout flow** — cart, address form, and confirmation screen
- **Coffee origin map** — interactive map showing where each coffee comes from
- **Dark mode** — toggle between light and dark theme
- **Social sharing** — share tasting journal entries with friends
- **Push notifications** — remind users to log new coffees they've tried
---

## 👨‍💻 Author

**Erdenesuren Shirmen**
Built with Kotlin + Jetpack Compose as a final class project demonstrating modern Android development with cloud and local data persistence, MVVM architecture, and a polished custom Material 3 theme.
 
---

## 📄 License

This project is for educational purposes.