package com.example.townpass.ui.setting

import com.example.townpass.data.repository.TownPassRepositoryImpl
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SettingViewModelTest {

    @Test
    fun updateAccountField_validatesInputs() {
        val repo = TownPassRepositoryImpl(context = null)
        val viewModel = SettingViewModel(repo)

        // Invalid phone number
        assertFalse(viewModel.updateAccountField("phoneNo", "12345"))
        // Valid phone number
        assertTrue(viewModel.updateAccountField("phoneNo", "0987654321"))

        // Invalid email
        assertFalse(viewModel.updateAccountField("email", "invalid_email"))
        // Valid email
        assertTrue(viewModel.updateAccountField("email", "user@taipei.gov.tw"))

        // Valid Taiwan ID
        assertTrue(viewModel.updateAccountField("idNo", "A123456789"))
        // Invalid Taiwan ID
        assertFalse(viewModel.updateAccountField("idNo", "A123456788"))
    }
}
