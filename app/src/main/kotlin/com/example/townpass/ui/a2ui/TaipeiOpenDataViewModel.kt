package com.example.townpass.ui.a2ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.townpass.data.repository.TaipeiOpenDataRepository
import com.example.townpass.data.repository.TaipeiOpenDataRepositoryImpl
import kotlinx.coroutines.flow.StateFlow

class TaipeiOpenDataViewModel(
    private val repository: TaipeiOpenDataRepository = TaipeiOpenDataRepositoryImpl()
) : ViewModel() {

    val filteredCards: StateFlow<List<A2UICardData>> = repository.filteredCards
    val selectedCategory: StateFlow<A2UICategory> = repository.selectedCategory
    val searchQuery: StateFlow<String> = repository.searchQuery
    val isAutoRefreshEnabled: StateFlow<Boolean> = repository.isAutoRefreshEnabled
    val lastRefreshedTime: StateFlow<String> = repository.lastRefreshedTime
    val isRefreshing: StateFlow<Boolean> = repository.isRefreshing

    fun setCategory(category: A2UICategory) {
        repository.setCategory(category)
    }

    fun setSearchQuery(query: String) {
        repository.setSearchQuery(query)
    }

    fun toggleAutoRefresh() {
        repository.toggleAutoRefresh()
    }

    fun refresh() {
        repository.refresh()
    }
}
