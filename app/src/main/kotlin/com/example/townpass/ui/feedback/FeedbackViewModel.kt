package com.example.townpass.ui.feedback

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class FeedbackViewModel : ViewModel() {
    private val _isSubmitted = MutableStateFlow(false)
    val isSubmitted: StateFlow<Boolean> = _isSubmitted.asStateFlow()

    fun submitFeedback(category: String, message: String): Boolean {
        if (message.isBlank()) return false
        _isSubmitted.value = true
        return true
    }

    fun reset() {
        _isSubmitted.value = false
    }
}
