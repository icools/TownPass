package com.example.townpass.data.model

import kotlinx.serialization.Serializable

/**
 * YouBike 2.0 Station Data Model from Taipei City Open Data.
 */
@Serializable
data class YouBikeStation(
    val sno: String,
    val name: String,
    val district: String,
    val address: String,
    val totalSpaces: Int,
    val availableBikes: Int,
    val emptySpaces: Int,
    val latitude: Double,
    val longitude: Double,
    val isActive: Boolean = true,
    val updatedTime: String = "",
    val isFavorite: Boolean = false
) {
    /**
     * Station status category based on remaining bikes and empty return spaces.
     */
    val status: YouBikeStatus
        get() = when {
            !isActive -> YouBikeStatus.INACTIVE
            availableBikes == 0 -> YouBikeStatus.NO_BIKES
            emptySpaces == 0 -> YouBikeStatus.FULL_DOCKS
            availableBikes <= 3 -> YouBikeStatus.LOW_BIKES
            else -> YouBikeStatus.NORMAL
        }

    /**
     * Rate of available bikes over total spaces.
     */
    val bikeRate: Float
        get() = if (totalSpaces > 0) (availableBikes.toFloat() / totalSpaces).coerceIn(0f, 1f) else 0f
}

enum class YouBikeStatus(val label: String, val colorHex: Long) {
    NORMAL("車輛充裕", 0xFF10B981),    // Green
    LOW_BIKES("車輛較少", 0xFFF59E0B), // Amber
    NO_BIKES("無車可借", 0xFFEF4444),  // Red
    FULL_DOCKS("車位已滿", 0xFFF97316), // Orange
    INACTIVE("暫停營運", 0xFF6B7280)   // Gray
}

enum class YouBikeFilterMode(val label: String) {
    ALL("全部站點"),
    RENT("可借車優先"),
    RETURN("可還位優先")
}

data class YouBikeStats(
    val totalStations: Int = 0,
    val totalAvailableBikes: Int = 0,
    val totalEmptySpaces: Int = 0,
    val totalSpaces: Int = 0,
    val activeStations: Int = 0
) {
    val activeRate: String
        get() = if (totalStations > 0) {
            String.format(java.util.Locale.TAIWAN, "%.1f%%", (activeStations.toFloat() / totalStations) * 100)
        } else "100%"
}
