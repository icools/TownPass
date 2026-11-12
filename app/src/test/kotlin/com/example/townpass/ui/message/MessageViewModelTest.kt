package com.example.townpass.ui.message

import com.example.townpass.data.model.MessageCategory
import com.example.townpass.data.repository.TownPassRepositoryImpl
import org.junit.Assert.assertEquals
import org.junit.Test

class MessageViewModelTest {

    @Test
    fun defaultCategory_isTCG() {
        val repo = TownPassRepositoryImpl(context = null)
        val viewModel = MessageViewModel(repo)
        assertEquals(MessageCategory.TCG, viewModel.selectedCategory.value)
    }

    @Test
    fun selectCategory_updatesSelection() {
        val repo = TownPassRepositoryImpl(context = null)
        val viewModel = MessageViewModel(repo)
        viewModel.selectCategory(MessageCategory.PERSONAL)
        assertEquals(MessageCategory.PERSONAL, viewModel.selectedCategory.value)
    }
}
