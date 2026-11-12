# TownPass (Android Native - Kotlin & Jetpack Compose)

TownPass is an open-source municipal services application developed for Taipei City. This repository contains the **pure Native Android** edition of TownPass, engineered with modern Android architecture standards.

## 🚀 Architecture & Tech Stack

- **Platform**: Native Android (100% Kotlin)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with Material 3
- **Architecture**: MVVM (Model-View-ViewModel) with Clean Architecture & Unidirectional Data Flow (UDF)
- **Navigation**: [Jetpack Navigation 3](https://developer.android.com/guide/navigation) (`NavDisplay`, `NavKey`, `entryProvider`)
- **State Management**: Kotlin Coroutines & `StateFlow`
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/) for Compose
- **Serialization**: Kotlinx Serialization JSON
- **Build System**: Gradle 9.3 with Kotlin DSL (`build.gradle.kts`) and Version Catalogs (`libs.versions.toml`)
- **Android Gradle Plugin (AGP)**: 9.1.0
- **Target Architecture**: Exclusively `arm64-v8a` (64-bit ARM)
- **Min / Target SDK**: Android 7.0 (API 24) / Android 16 (API 36)
- **CLI & Agent Tooling**: [Android CLI](https://developer.android.com/tools/agents/android-cli) support (`android describe`, `android analyze`, `android build`)

## 📱 Features

1. **服務 (City Services)**:
   - "我的服務" 快捷釘選網格（1999 網路通話、線上申辦、有話要說陳情、臨櫃叫號、i-Voting、動物園、電子借閱證等）
   - 服務編輯管理（隨選自訂首頁釘選服務）
   - 官方服務卡片與熱門市政推薦

2. **首頁 (Home)**:
   - 市政焦點橫幅輪播（NewsBanner Carousel）
   - 活動訊息即時快訊橫向展示
   - 市政快訊專欄卡片
   - 個人化市政訂閱消息通知

3. **優惠 (Perk)**:
   - 台北通票夾整合（Ticket Wallet）
   - 特約商圈與市政活動折扣券（動物園門票折抵、文創特惠、健康集點悠遊卡加值折抵）

4. **帳務 (Bill)**:
   - 台北智慧支付（pay.taipei）掃描繳費入口
   - 熱門查繳項目快捷選單（停車費、水費、地方稅、學雜費、公宅租金維管費、聯醫醫療費等）

5. **帳戶與設定 (Account & Settings)**:
   - 市民個人資料卡與實名驗證狀態
   - 基本資料即時編輯與驗證（台灣身分證字號權重校驗碼、手機門號、電子郵件）
   - 偏好設定、生物辨識快速登入、訊息通知開關
   - 意見回饋表單提交

6. **訊息中心 (Message Center)**:
   - 個人通知、市府訊息、系統訊息分頁標籤
   - 未讀提示與富文本活動連結

7. **活動詳情 (Activity Info)**:
   - 活動詳情圖文檢視、日期區間、外部活動官網整合（In-App WebView）

## 🛠️ Build & Test

### Run Unit Tests
```bash
./gradlew test
```

### Build Debug APK
```bash
./gradlew assembleDebug
```

### Build Release APK (arm64-v8a only)
```bash
./gradlew assembleRelease
```
The generated APK will be at:
`app/build/outputs/apk/release/app-release.apk`

### Using Android CLI
```bash
# Describe project variants and build outputs
android describe --project_dir=.

# Analyze APK size and dex markers
android analyze app/build/outputs/apk/release/app-release.apk
```

## 📄 License
This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details.
