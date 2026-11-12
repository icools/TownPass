package com.example.townpass.ui.message

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.townpass.data.model.MessageCategory
import com.example.townpass.data.model.MessageItem
import com.example.townpass.data.repository.TownPassRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class MessageViewModel(
    private val repository: TownPassRepository
) : ViewModel() {

    private val _selectedCategory = MutableStateFlow(MessageCategory.TCG)
    val selectedCategory: StateFlow<MessageCategory> = _selectedCategory.asStateFlow()

    val filteredMessages: StateFlow<List<MessageItem>> = combine(
        repository.messagesFlow,
        _selectedCategory
    ) { messages, category ->
        messages.filter { it.type.equals(category.rawValue, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun selectCategory(category: MessageCategory) {
        _selectedCategory.value = category
    }

    fun markAsRead(id: String) {
        repository.markMessageAsRead(id)
    }

    fun getMessage(id: String): MessageItem? {
        return repository.getMessageById(id)
    }
}
