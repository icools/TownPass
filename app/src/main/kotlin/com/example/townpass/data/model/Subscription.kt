package com.example.townpass.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionResponse(
    val data: List<SubscriptionItem> = emptyList()
)

@Serializable
data class SubscriptionItem(
    val title: String = "",
    val content: String = "",
    @SerialName("web_url") val webUrl: String? = null,
    val datetime: Long = 0L
)
