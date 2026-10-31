# 📱 Dealership Management (Sales-Man)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple.svg?style=for-the-badge&logo=kotlin)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Platform-Android-3DDC84.svg?style=for-the-badge&logo=android)](https://developer.android.com/)
[![Min SDK](https://img.shields.io/badge/Min%20SDK-24-blue.svg?style=for-the-badge)](https://developer.android.com/)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-36-brightgreen.svg?style=for-the-badge)](https://developer.android.com/)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVVM-orange.svg?style=for-the-badge)](https://developer.android.com/topic/architecture)
[![Dagger Hilt](https://img.shields.io/badge/DI-Dagger%20Hilt-yellow.svg?style=for-the-badge)](https://dagger.dev/hilt/)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

An enterprise-grade **Dealership Sales & Distribution Management (DSR / Field Sales Representative)** Android application. Built using **Clean Architecture**, **MVVM**, **Kotlin Coroutines & Flow**, and **Dagger Hilt**, this app equips field sales agents with end-to-end tooling to onboard retailers, book orders, track real-time inventory, manage deliveries, collect payments, and print instant thermal receipts over Bluetooth POS printers.

---

## 🌟 Key Features

### 📍 Territory & Retailer Management
- **Geographical Hierarchy:** Browse and filter stores by Division, District, Sub-district, and Route/Area.
- **Retailer Onboarding:** Register new store owners with storefront photos, owner details, contact numbers, NID, and GPS geo-location.
- **Store Profiles:** Access customer order history, outstanding dues, and contact information.

### 📦 Product Catalog & Stock Tracking
- **Real-Time Stock Availability:** Verify warehouse inventory levels before taking orders to eliminate stock-out issues.
- **Categorized Browsing:** Seamlessly navigate product categories with dynamic pricing and packaging unit details.

### 🛒 Order Booking & Processing
- **Smart Cart:** Quick product addition, automated pricing, unit calculation, and order summary.
- **Instant Order Submission:** Place direct distributor orders with real-time API sync.
- **Delivery Workflow:** Track "Today's Delivery" with pending and completed order statuses.

### 💳 Payment Collection & Ledger
- **Multiple Payment Channels:** Record payments via Cash, Bank, or Mobile Financial Services (bKash/Nagad).
- **Payment History:** Keep an accurate balance ledger with real-time due tracking per retailer.

### 🖨️ Wireless Bluetooth Thermal Printing (ESC/POS)
- **Direct RFCOMM Socket Printing:** Connect to any paired Bluetooth POS thermal receipt printer.
- **Formatted Invoices:** Generates clean, monospace receipt layouts with salesman information, customer data, itemized lines, and totals.

### 🔐 Security & Account Management
- **JWT Authentication:** Secure token storage with access token and refresh token management.
- **User Profile & Customization:** In-app profile image update with image picker & cropping, password reset, and profile editing.
- **Offline / Network Detection:** Reactive connection state alerts preventing lost transactions.

---

## 🏗️ Architecture & Tech Stack

This project strictly adheres to **Clean Architecture** and the **MVVM (Model-View-ViewModel)** design pattern with a unidirectional data flow.

```
                  ┌────────────────────────┐
                  │   Presentation Layer   │
                  │   (Fragments, ViewModels, UI)
                  └───────────┬────────────┘
                              │
                  ┌───────────▼────────────┐
                  │      Domain Layer      │
                  │ (Use Cases, Models, Repo Interfaces)
                  └───────────┬────────────┘
                              │
                  ┌───────────▼────────────┐
                  │       Data Layer       │
                  │ (DTOs, Retrofit Services, Repo Impls)
                  └────────────────────────┘
```

### Tech Stack:
- **Language:** [Kotlin](https://kotlinlang.org/) (Coroutines + StateFlow / LifecycleScope)
- **Architecture:** Clean Architecture + MVVM + Repository Pattern
- **Dependency Injection:** [Dagger Hilt](https://dagger.dev/hilt/)
- **Networking:** [Retrofit 2](https://square.github.io/retrofit/) & [Gson](https://github.com/google/gson) + OkHttp
- **UI & Layout:** ViewBinding, Material Components 3, [SDP & SSP](https://github.com/intuit/sdp) (Responsive screen sizing)
- **Navigation:** Jetpack Navigation Component with SafeArgs
- **Image Processing:** [Glide](https://github.com/bumptech/glide), [Coil](https://coil-kt.github.io/coil/), and [Dhaval2404 ImagePicker](https://github.com/Dhaval2404/ImagePicker)
- **Hardware Integration:**
  - Android Bluetooth API (RFCOMM direct ESC/POS socket printing)
  - Google Play Services Location (GPS store tagging)
  - Camera & Media Store APIs
- **Animations:** [Lottie Android](https://github.com/airbnb/lottie-android)

---

## 📂 Project Structure

```bash
com.wevx.dealershipmanagement/
├── core/
│   ├── common/              # BaseFragment, Resource state wrappers (Success, Error, Loading)
│   └── di/                  # Hilt Dependency Injection modules (NetworkModule, AppModule)
├── data/
│   ├── dto/                 # Request & Response Data Transfer Objects (Auth, Order, Store, Payment, Product)
│   ├── remote/              # Retrofit API service definitions
│   └── repository/          # Repository implementations
├── domain/
│   ├── models/              # Clean Domain entities (CartItem, Product, StoreOwner, etc.)
│   ├── repository/          # Repository contracts / interfaces
│   └── use_case/            # Business logic use cases
├── presentation/
│   ├── auth/                # Login, Registration, Forgot Password, Profile management
│   ├── home/                # Territory selection & Store list
│   ├── addCustomer/         # New retailer registration
│   ├── product/             # Product catalog & Cart
│   ├── payment/             # Payment processing
│   ├── receipt/             # Thermal receipt preview & Bluetooth printing
│   ├── order/               # Today's delivery & Order statuses
│   ├── orderDetails/        # Comprehensive order breakdown
│   ├── stockAvailability/   # Warehouse stock overview
│   └── adapter/             # RecyclerView adapters & ViewHolders
└── utils/                   # TokenManager, NetworkUtils, Constants, Extension functions
```

---

## 🚀 Getting Started

### Prerequisites
- **Android Studio** Ladybug (2024.2.1) or newer
- **JDK:** Version 11 or higher
- **Android Device / Emulator:** Android 7.0 (API level 24) or higher
- *(Optional)* Portable **Bluetooth POS Thermal Printer** (58mm or 80mm ESC/POS compatible)

### Installation
1. **Clone the repository:**
   ```bash
   git clone https://github.com/rifatHossainDevs/Sales-Man.git
   ```

2. **Open the project in Android Studio:**
   - Launch Android Studio and choose `Open...`.
   - Select the cloned directory.

3. **Configure API Base URL:**
   - Open `app/src/main/java/com/wevx/dealershipmanagement/utils/Constants.kt`
   - Adjust `BASE_URL` to point to your backend server:
     ```kotlin
     const val BASE_URL = "https://your-api-endpoint.com/api/v1/"
     ```

4. **Build & Run:**
   - Sync Gradle dependencies.
   - Connect an Android device or start an emulator.
   - Click **Run (`Shift + F10`)**.

---

## 📱 Hardware & Permissions

This application utilizes device hardware capabilities to streamline field operations:
- **Bluetooth & Bluetooth Admin (`BLUETOOTH_CONNECT`, `BLUETOOTH_SCAN`):** Required to discover and connect to portable ESC/POS thermal printers for issuing paper receipts.
- **Location (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`):** Required for Bluetooth discovery and capturing accurate GPS coordinates when onboarding retailer shops.
- **Camera & Storage (`CAMERA`, `READ_MEDIA_IMAGES`):** Allows field agents to take live photos of storefronts or upload profile images.

---

## 📸 Screenshots

| Login & Splash | Territory / Home | Retailer Onboarding |
|:---:|:---:|:---:|
| *(Add Screenshot)* | *(Add Screenshot)* | *(Add Screenshot)* |

| Product Catalog | Today's Delivery | Receipt & Bluetooth Print |
|:---:|:---:|:---:|
| *(Add Screenshot)* | *(Add Screenshot)* | *(Add Screenshot)* |

> *Tip: Replace placeholders above with paths to your screenshots in an `assets/` or `screenshots/` folder.*

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

---

## 👨‍💻 Author

**Rifat Hossain**
- GitHub: [@rifatHossainDevs](https://github.com/rifatHossainDevs)
