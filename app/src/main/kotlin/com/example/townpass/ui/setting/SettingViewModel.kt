package com.example.townpass.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.townpass.data.model.AccountData
import com.example.townpass.data.repository.TownPassRepository
import com.example.townpass.util.IdNumberValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn

class SettingViewModel(
    private val repository: TownPassRepository
) : ViewModel() {

    val account: StateFlow<AccountData> = repository.accountFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccountData()
    )

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _quickLoginEnabled = MutableStateFlow(true)
    val quickLoginEnabled: StateFlow<Boolean> = _quickLoginEnabled.asStateFlow()

    val lastLoginTime: String = "2026/11/12 16:30:00"

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun setQuickLoginEnabled(enabled: Boolean) {
        _quickLoginEnabled.value = enabled
    }

    fun updateAccountField(key: String, value: String): Boolean {
        return when (key) {
            "idNo" -> {
                if (!IdNumberValidator.isValid(value)) return false
                repository.updateAccount(idNo = value)
                true
            }
            "realName" -> {
                if (value.isBlank()) return false
                repository.updateAccount(realName = value)
                true
            }
            "birthday" -> {
                repository.updateAccount(birthday = value)
                true
            }
            "email" -> {
                if (!value.contains("@") || !value.contains(".")) return false
                repository.updateAccount(email = value)
                true
            }
            "phoneNo" -> {
                if (!Regex("^09\\d{8}$").matches(value)) return false
                repository.updateAccount(phoneNo = value)
                true
            }
            "residentAddress" -> {
                if (value.isBlank()) return false
                repository.updateAccount(residentAddress = value)
                true
            }
            else -> false
        }
    }
}
