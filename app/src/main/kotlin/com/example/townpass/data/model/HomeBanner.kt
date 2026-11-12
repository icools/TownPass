package com.example.townpass.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HomeBannerResponse(
    val data: List<BannerItem> = emptyList()
)

@Serializable
data class BannerItem(
    @SerialName("image_url") val imageUrl: String = "",
    @SerialName("web_url") val webUrl: String = "",
    val title: String = ""
)
