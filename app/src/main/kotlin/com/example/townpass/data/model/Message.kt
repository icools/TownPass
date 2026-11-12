package com.example.townpass.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    val data: List<MessageItem> = emptyList()
)

@Serializable
data class MessageItem(
    val id: String = "",
    val title: String = "",
    val datetime: Long = 0L,
    val type: String = "TCG", // PERSONAL, TCG, SYSTEM
    @SerialName("has_read") var hasRead: Boolean = false,
    @SerialName("image_url") val imageUrl: String? = null,
    @SerialName("rich_text") val richText: List<RichTextItem>? = null
)

@Serializable
data class RichTextItem(
    val text: String? = null,
    val url: String? = null
)

enum class MessageCategory(val rawValue: String, val displayName: String) {
    PERSONAL("PERSONAL", "個人通知"),
    TCG("TCG", "市府訊息"),
    SYSTEM("SYSTEM", "系統訊息");

    companion object {
        fun fromRaw(raw: String): MessageCategory {
            return entries.firstOrNull { it.rawValue.equals(raw, ignoreCase = true) } ?: TCG
        }
    }
}
