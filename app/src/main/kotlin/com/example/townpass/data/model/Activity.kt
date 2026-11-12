package com.example.townpass.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    val data: List<ActivityItem> = emptyList()
)

@Serializable
data class ActivityItem(
    val id: String = "",
    @SerialName("image_url") val imageUrl: String = "",
    @SerialName("web_url") val webUrl: String? = null,
    val title: String = "",
    @SerialName("start_datetime") val startDateTime: Long = 0L,
    @SerialName("end_datetime") val endDateTime: Long = 0L,
    val content: String = ""
)
