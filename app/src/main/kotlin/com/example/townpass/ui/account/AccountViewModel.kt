package com.example.townpass.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.townpass.data.model.AccountData
import com.example.townpass.data.repository.TownPassRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class AccountViewModel(
    repository: TownPassRepository
) : ViewModel() {

    val account: StateFlow<AccountData> = repository.accountFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccountData()
    )

    val appVersion: String = "1.0.0 (build 2001)"
}
