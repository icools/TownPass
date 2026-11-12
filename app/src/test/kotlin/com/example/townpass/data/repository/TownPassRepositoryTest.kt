package com.example.townpass.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TownPassRepositoryTest {

    @Test
    fun repository_initializesDefaultData() = runTest {
        val repo = TownPassRepositoryImpl(context = null)

        val account = repo.accountFlow.first()
        assertNotNull(account)
        assertEquals("金大森", account.realName)

        val bills = repo.bills
        assertTrue(bills.isNotEmpty())
        assertEquals("停車費", bills[0].name)
    }

    @Test
    fun repository_updatesAccount() = runTest {
        val repo = TownPassRepositoryImpl(context = null)
        repo.updateAccount(realName = "測試用戶", phoneNo = "0912345678")

        val updated = repo.accountFlow.first()
        assertEquals("測試用戶", updated.realName)
        assertEquals("0912345678", updated.phoneNo)
    }

    @Test
    fun repository_togglesServicePin() = runTest {
        val repo = TownPassRepositoryImpl(context = null)
        val initialServices = repo.servicesFlow.first()
        val firstService = initialServices.first()
        val initialPinned = firstService.isPinned

        repo.togglePinService(firstService.id)

        val updatedServices = repo.servicesFlow.first()
        assertEquals(!initialPinned, updatedServices.first().isPinned)
    }
}
