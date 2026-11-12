package com.example.townpass.ui.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.townpass.data.model.CityServiceItem
import com.example.townpass.data.repository.TownPassRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ServiceViewModel(
    private val repository: TownPassRepository
) : ViewModel() {

    val allServices: StateFlow<List<CityServiceItem>> = repository.servicesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val pinnedServices: StateFlow<List<CityServiceItem>> = repository.servicesFlow
        .map { list -> list.filter { it.isPinned } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun togglePin(serviceId: String) {
        repository.togglePinService(serviceId)
    }
}
