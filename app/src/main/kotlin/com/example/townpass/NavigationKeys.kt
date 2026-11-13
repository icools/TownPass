package com.example.townpass

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object MainNavKey : NavKey

@Serializable
data object AccountNavKey : NavKey

@Serializable
data object SettingNavKey : NavKey

@Serializable
data object BasicInfoNavKey : NavKey

@Serializable
data class BasicInfoEditNavKey(
    val fieldKey: String,
    val title: String,
    val currentValue: String
) : NavKey

@Serializable
data object MessageNavKey : NavKey

@Serializable
data class MessageDetailNavKey(
    val messageId: String
) : NavKey

@Serializable
data object ActivityListNavKey : NavKey

@Serializable
data class ActivityDetailNavKey(
    val activityId: String
) : NavKey

@Serializable
data object ServiceEditNavKey : NavKey

@Serializable
data object FeedbackNavKey : NavKey

@Serializable
data class WebViewNavKey(
    val url: String,
    val title: String = ""
) : NavKey

@Serializable
data object TaipeiOpenDataNavKey : NavKey

@Serializable
data object YouBikeNavKey : NavKey


