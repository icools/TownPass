package com.example.townpass.ui.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.townpass.data.model.ActivityItem
import com.example.townpass.data.repository.TownPassRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ActivityViewModel(
    private val repository: TownPassRepository
) : ViewModel() {

    val activities: StateFlow<List<ActivityItem>> = repository.activitiesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun getActivity(id: String): ActivityItem? {
        return repository.getActivityById(id)
    }
}
