package com.example.townpass.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    val name: String? = null,
    val status: Int = 0,
    val reasonPhrase: String? = null,
    val data: AccountData,
    val version: String? = null
)

@Serializable
data class AccountData(
    val id: String = "",
    val account: String = "",
    val username: String = "",
    val realName: String = "",
    val idNo: String = "",
    val email: String? = null,
    val phoneNo: String? = null,
    val birthday: String? = null,
    val memberType: String = "personal",
    val verifyLevel: String = "3",
    val residentAddress: String? = null,
    val citizen: Boolean = true,
    val nativePeople: Boolean = false,
    val addresses: List<AccountAddress> = emptyList()
)

@Serializable
data class AccountAddress(
    val zip3: Int = 0,
    val city: String = "",
    val town: String = "",
    val village: String = "",
    val street: String = "",
    val usageType: String = "0",
    val seq: Int = 1,
    val priority: Boolean = true
)
