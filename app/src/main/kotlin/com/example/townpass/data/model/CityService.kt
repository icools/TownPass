package com.example.townpass.data.model

data class CityServiceItem(
    val id: String,
    val title: String,
    val description: String,
    val category: String = "市政服務",
    val destinationUrl: String = "",
    val isPinned: Boolean = true
)

data class PopularBillItem(
    val id: String,
    val name: String,
    val uri: String,
    val category: String = "熱門項目"
)
