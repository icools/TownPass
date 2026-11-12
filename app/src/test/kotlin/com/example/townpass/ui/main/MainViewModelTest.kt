package com.example.townpass.ui.main

import com.example.townpass.ui.components.MainTab
import org.junit.Assert.assertEquals
import org.junit.Test

class MainViewModelTest {

    @Test
    fun defaultTab_isHome() {
        val viewModel = MainViewModel()
        assertEquals(MainTab.HOME, viewModel.currentTab.value)
    }

    @Test
    fun selectTab_updatesCurrentTab() {
        val viewModel = MainViewModel()
        viewModel.selectTab(MainTab.SERVICE)
        assertEquals(MainTab.SERVICE, viewModel.currentTab.value)

        viewModel.selectTab(MainTab.BILL)
        assertEquals(MainTab.BILL, viewModel.currentTab.value)
    }
}
