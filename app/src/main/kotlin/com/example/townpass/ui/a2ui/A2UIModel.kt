package com.example.townpass.ui.a2ui

import androidx.compose.ui.graphics.Color
import com.example.townpass.ui.theme.TPAccentOrange
import com.example.townpass.ui.theme.TPPrimaryTeal
import com.example.townpass.ui.theme.TPWhite

/**
 * A2UI (Agent-to-UI / Adaptive UI) Declarative Component System.
 * Defines the schemas and models for dynamically rendering real-time Taipei Open Data cards.
 */

enum class A2UICategory(val title: String, val iconName: String) {
    ALL("全部", "widgets"),
    TRANSPORT("交通運輸", "directions_transit"),
    ENVIRONMENT("環境氣象", "cloud"),
    HEALTH("醫療照護", "local_hospital"),
    TOURISM("休閒觀光", "attractions"),
    LIVING("民生用水", "water_drop")
}

enum class A2UIStatus(val label: String, val colorHex: Long) {
    NORMAL("正常", 0xFF10B981),      // Green
    GOOD("良好", 0xFF059669),        // Emerald
    MODERATE("普通", 0xFFF59E0B),    // Amber
    BUSY("擁擠", 0xFFF97316),        // Orange
    ALERT("警戒", 0xFFEF4444),       // Red
    LIVE("即時", 0xFF0D9488),        // Teal
    INFO("資訊", 0xFF3B82F6)         // Blue
}

data class A2UIMetric(
    val label: String,
    val value: String,
    val unit: String = "",
    val status: A2UIStatus = A2UIStatus.NORMAL,
    val note: String? = null
)

data class A2UIDetailRow(
    val title: String,
    val value: String,
    val subtitle: String = "",
    val status: A2UIStatus = A2UIStatus.NORMAL,
    val tag: String? = null
)

data class A2UIProgress(
    val current: Float,
    val max: Float,
    val label: String,
    val displayPercent: Int = ((current / max.coerceAtLeast(1f)) * 100).toInt()
)

data class A2UIAction(
    val label: String,
    val actionType: String = "VIEW_DETAILS",
    val url: String? = null
)

/**
 * A2UI Dynamic Card Data Structure.
 * Any open data source can be converted into this schema and rendered uniformly
 * with adaptive, dynamic styling in Jetpack Compose.
 */
data class A2UICardData(
    val id: String,
    val sourceKey: String,
    val category: A2UICategory,
    val title: String,
    val subtitle: String,
    val department: String,
    val status: A2UIStatus,
    val badgeText: String,
    val primaryValue: String,
    val primaryLabel: String,
    val primaryUnit: String = "",
    val metrics: List<A2UIMetric> = emptyList(),
    val details: List<A2UIDetailRow> = emptyList(),
    val progress: A2UIProgress? = null,
    val tags: List<String> = emptyList(),
    val action: A2UIAction? = null,
    val lastUpdated: String = "",
    val updateIntervalSeconds: Int = 10,
    val isRealTime: Boolean = true
)
