<div align="center">

# 📸 InstaSaver Pro - Kotlin Multiplatform App



### Kotlin Multiplatform (KMP) Open Source Project

[![Kotlin](https://img.shields.io/badge/Kotlin-Multiplatform-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/docs/multiplatform.html)
[![Compose](https://img.shields.io/badge/Compose-Multiplatform-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Firebase](https://img.shields.io/badge/Firebase-Integrated-FFCA28?style=for-the-badge&logo=firebase&logoColor=black)](https://firebase.google.com/)
[![RevenueCat](https://img.shields.io/badge/RevenueCat-Subscriptions-F25C5C?style=for-the-badge)](https://www.revenuecat.com/)
[![AdMob](https://img.shields.io/badge/AdMob-Ads-EA4335?style=for-the-badge&logo=google&logoColor=white)](https://admob.google.com/)

**A fully functional cross-platform app with shared business logic and fully shared UI — built for learning and real-world production reference.**

[▶️ Live on Google Play](https://play.google.com/store/apps/details?id=com.clipsaver.quickreels&hl=en&gl=gb) · [⭐ Star on GitHub](https://github.com/ShahzaibAli02/InstaSaver-Kmp) · [🍴 Fork the Repo](https://github.com/ShahzaibAli02/InstaSaver-Kmp/fork)

</div>

---

## 📖 Project Overview

**InstaSaver Pro** is a social media video downloader app that allows users to download HD videos. It's designed as both a learning resource and a production-grade reference for building cross-platform apps with Kotlin Multiplatform.

**What you'll learn from this project:**

- 🧩 Kotlin Multiplatform (KMP) setup and structure
- 🎨 Compose Multiplatform for fully shared UI
- 🔥 Firebase integration in a KMP project
- 📢 AdMob ads integration (Banner & Interstitial)
- 💳 Paywall & subscriptions using RevenueCat
- 🏗️ Clean architecture patterns in KMP
- 🌐 Shared networking layer setup

---

## 🏛️ Architecture

| Property | Value |
|---|---|
| 🔁 Shared Code | **90%** |
| 🎨 Shared UI | **100%** |
| 🧠 Architecture | **MVVM** |
| 📦 ViewModels | Shared |
| 🌐 Networking | Shared Layer |

> Clean, scalable, and production-ready structure throughout.

---

## 🛠️ Technologies Used

| Category | Technology |
|---|---|
| 📱 Cross-platform | Kotlin Multiplatform (KMP) |
| 🎨 UI | Compose Multiplatform |
| 🧠 Architecture | MVVM |
| 🌐 Networking | REST APIs |
| 🔥 Backend Services | Firebase |
| 💳 Subscriptions | RevenueCat |
| 📢 Ads | Google AdMob |

---

## 💰 Monetization

### 🆓 Free Users — AdMob Ads
- Banner & Interstitial Ads
- Integrated with Google AdMob
- Clean separation between Free and Premium users

### 👑 Premium Users — RevenueCat Paywall
- Lifetime purchase option
- Subscription support
- All ads removed for premium users

---

## ⚙️ Setup Guide

### 1️⃣ Clone the Repository

```bash
git clone https://github.com/ShahzaibAli02/InstaSaver-Kmp.git
```

---

### 2️⃣ Firebase Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project
3. Add your **Android App** and **iOS App**

**Android / Shared Setup:**

Download `google-services.json` and place it inside:
```
commonMain/
```

**iOS Setup:**

- Add your iOS app in Firebase Console
- Download the iOS configuration file (`.plist`)
- Add it inside the iOS module
- Initialize Firebase in your iOS App entry point

---

### 3️⃣ Update Base URL

> ⚠️ **IMPORTANT:** Update the API base URL before running.

Navigate to `NetworkHelperImpl.kt` and replace:

```kotlin
// Before
val baseURL : String = "http://192.168.100.105:3004"

// After
val baseURL : String = "https://your-api-url.com"
```

> 📄 API documentation: [Postman Docs](https://documenter.getpostman.com/view/17181476/2sA3XLG4x8)

---

### 4️⃣ AdMob Test Setup

> ⚠️ **DO NOT use real AdMob IDs during development!** Use Google's official test IDs below.

**Test App ID:**
```
ca-app-pub-3940256099942544~3347511713
```

**Test Ad Unit IDs:**

| Ad Type | Test ID |
|---|---|
| 🏷️ Banner | `ca-app-pub-3940256099942544/6300978111` |
| 📺 Interstitial | `ca-app-pub-3940256099942544/1033173712` |
| 🎁 Rewarded | `ca-app-pub-3940256099942544/5224354917` |

> ✅ Always replace with your **real IDs** before a production release.

---

### 5️⃣ RevenueCat Setup

1. Create a [RevenueCat](https://www.revenuecat.com/) account
2. Add your Android & iOS apps
3. Configure your subscription and/or lifetime products
4. Replace the API keys inside the project with your own

---

## 🤝 Contributing

Contributions are welcome and appreciated! Here's how to get involved:

1. 🍴 **Fork** the repository
2. 🏗️ Improve the architecture or add features
3. 🐛 Open issues for bugs or suggestions
4. 🔃 Submit pull requests

---

## ⭐ Support

If this project helped you, please consider **starring the repository** on GitHub — it helps others discover it too!

[![Star on GitHub](https://img.shields.io/github/stars/ShahzaibAli02/InstaSaver-Kmp?style=social)](https://github.com/ShahzaibAli02/InstaSaver-Kmp)

---

<div align="center">

**Happy Coding! 🚀**

Made with ❤️ using Kotlin Multiplatform

</div>