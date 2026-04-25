# ☕ Roast Buddy

> **Your personal coffee discovery companion** — find the perfect roast, track what you've tried, and deepen your coffee knowledge.
 
---

## 📱 Overview

Roast Buddy is an Android app that helps coffee lovers discover the right coffee for their taste and brewing style. Instead of overwhelming users with choices, it guides them through a personalized quiz, explains every recommendation, and provides tools to track, learn, and build their coffee journey over time. On first launch, users create a personal profile that personalizes the entire experience.
 
---

## ✨ Features

| Feature | Description |
|---|---|
| 👤 **User Profile** | Create a personal profile with name, preferred brew method, and experience level — saved with DataStore and shown on every launch |
| 🎯 **Roast Finder Quiz** | 3-question quiz mapping brew method, flavor preference, and strength to a personalized coffee recommendation with explanation |
| 📦 **Artisanal Catalog** | 6 curated coffees fetched from Firebase Firestore with roast filters, search, detail views, and brew recommendations |
| ❤️ **Wishlist** | Save favorite coffees to a persistent on-device wishlist powered by Room database |
| 📓 **Tasting Journal** | Log coffees with roast level, 1–5 star rating, tasting notes, and auto-stamped date |
| 📊 **Coffee Stats** | Personal analytics — total coffees tried, average rating, favorite roast, and roast breakdown bars |
| 🎓 **Education Hub** | 6 curated articles and videos with category filters, opening directly in browser |
 
---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI** | Jetpack Compose + Material 3 |
| **Navigation** | Navigation Compose |
| **State Management** | ViewModel + StateFlow |
| **User Preferences** | DataStore (user profile persistence) |
| **Local Database** | Room (Journal entries + Wishlist) |
| **Cloud Database** | Firebase Firestore (Coffee catalog) |
| **AI / Networking** | Gemini API via OkHttp |
| **Architecture** | Single-activity, MVVM |
 
---

## 🗂 Project Structure

```
com.example.rooastbuddy/
├── MainActivity.kt          # All UI screens (Compose)
├── CoffeeViewModel.kt       # Quiz logic, Firestore catalog, AI recommendation
├── JournalViewModel.kt      # Journal + Wishlist state management
├── ProfileViewModel.kt      # User profile state management
├── UserPreferences.kt       # DataStore — user profile persistence
├── JournalEntry.kt          # Room entity — tasting journal
├── WishlistItem.kt          # Room entity — wishlist
├── JournalDao.kt            # Room DAO — journal + wishlist queries
├── AppDatabase.kt           # Room database instance
└── ui/theme/
    ├── Color.kt             # Pine green + coffee brown palette
    ├── Theme.kt             # MaterialTheme configuration
    └── Type.kt              # Typography
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
    - Create a Firebase project at [console.firebase.google.com](https://console.firebase.google.com)
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
   # Open in Android Studio and click Run, or:
   ./gradlew assembleDebug
   ```

---

## 📱 App Screens

| Setup | Home | Quiz |
|---|---|---|
| Profile setup on first launch | Personalized welcome with feature cards | 3-question preference quiz |

| Recommendation | Catalog | Journal |
|---|---|---|
| Smart match with explanation | Firestore catalog with filters & search | Log coffees with star ratings |

| Wishlist | Stats | Education |
|---|---|---|
| Saved coffees from catalog | Personal analytics dashboard | Articles and videos hub |
 
---

## 🏗 Architecture

```
UI (Compose Screens)
        ↕
ViewModel (CoffeeViewModel / JournalViewModel / ProfileViewModel)
        ↕                    ↕                    ↕
Firebase Firestore      Room Database         DataStore
(Coffee Catalog)    (Journal + Wishlist)   (User Profile)
        ↕
  Gemini AI API
(Recommendations)
```
 
---

## 🔮 Future Extensions

- **Real AI integration** — live Gemini API recommendations beyond rule-based scoring
- **User accounts** — Firebase Authentication to sync data across devices
- **Mock checkout** — order flow with cart and confirmation screen
- **Coffee map** — interactive map showing origins tied to catalog entries
- **Dark mode** — toggle between light and dark theme
- **Social sharing** — share tasting journal entries with friends
---

## 👨‍💻 Author

**Erdenesuren Shirmen**
Built with Kotlin + Jetpack Compose as a class project demonstrating modern Android development with cloud and local data persistence.
 
---

## 📄 License

This project is for educational purposes.