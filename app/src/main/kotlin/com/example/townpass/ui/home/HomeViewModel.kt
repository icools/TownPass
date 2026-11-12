package com.example.townpass.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.townpass.data.model.AccountData
import com.example.townpass.data.model.ActivityItem
import com.example.townpass.data.model.BannerItem
import com.example.townpass.data.model.SubscriptionItem
import com.example.townpass.data.repository.TownPassRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HomeViewModel(
    private val repository: TownPassRepository
) : ViewModel() {

    val account: StateFlow<AccountData> = repository.accountFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AccountData()
    )

    val banners: StateFlow<List<BannerItem>> = repository.bannersFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val activities: StateFlow<List<ActivityItem>> = repository.activitiesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val subscriptions: StateFlow<List<SubscriptionItem>> = repository.subscriptionsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
