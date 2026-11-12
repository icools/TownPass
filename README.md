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

2. **台北即時 Open Data 專區 (A2UI 動態即時儀表板)**:
   - 基於 **A2UI (Adaptive / Agent-to-UI)** 聲明式動態卡片架構
   - 串接 **12 種台北市市政 Open Data 即時資料來源**：
     1. **YouBike 2.0 即時站點**: 可借車輛與空位即時統計、主要樞紐站點明細
     2. **台北市空氣品質 (AQI)**: AQI 指數、PM2.5、PM10 各行政區測站即時數值
     3. **公有停車場即時車位**: 府前廣場/大安森林公園/信義國小小型車與機車剩餘位
     4. **台北捷運營運與車廂擁擠度**: 6 條路線即時營運狀況、準點率、車廂擁擠度燈號
     5. **台北市區公車即時動態**: 307、299、承德幹線到站倒數與低地板無障礙公車資訊
     6. **定點垃圾車抵達即時動態**: 收運點預計抵達時間、車號、一般/資源回收清運
     7. **台北主要景點人潮警示度**: 台北101、西門町、陽明山人潮流量監控燈號 (綠/黃/紅)
     8. **行政區微氣象與即時降雨**: 行政區測站即時溫濕度、降雨機率、紫外線指數
     9. **主要幹道即時車速與路況**: 市民高架、建國高架、新生高架時速與事故施工狀態
     10. **市立聯合醫院急診看診動態**: 仁愛、中興、和平、陽明等各院區急診看診與等候住院即時人數
     11. **自來水直飲台水質監測**: 餘氯、濁度、pH 值即時 100% 直飲合格檢測
     12. **台北市立動物園即時入園**: 大貓熊館叫號、在園人數與熱門展館排隊時間
   - **動態即時互動功能**:
     - 支援定時自動即時動態輪詢更新（可隨時暫停/啟用）
     - 旋轉動畫手動立即更新按鈕
     - 即時連線呼吸脈動燈號動畫（LivePulseBadge）
     - 即時飽和度儀表進度條（A2UIProgressBar）
     - 展開/收合站點詳細遙測數據
     - 6 大主題分類切換（全部、交通運輸、環境氣象、醫療照護、休閒觀光、民生用水）
     - 即時關鍵字搜尋過濾功能
     - 首頁（Home）直接嵌入 TOP 3 即時動態卡片與專屬看板直達入口

3. **首頁 (Home)**:
   - 市政焦點橫幅輪播（NewsBanner Carousel）
   - 台北即時動態 A2UI 卡片展示專區
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
