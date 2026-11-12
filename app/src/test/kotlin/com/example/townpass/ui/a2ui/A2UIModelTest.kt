package com.example.townpass.ui.a2ui

import com.example.townpass.data.service.TaipeiOpenDataService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class A2UIModelTest {

    private val service = TaipeiOpenDataService()

    @Test
    fun testAllCardsSatisfySchemaRequirements() {
        val cards = service.getDynamicOpenDataCards(0)
        assertEquals(12, cards.size)

        cards.forEach { card ->
            assertTrue("Card id must not be blank", card.id.isNotBlank())
            assertTrue("Card title must not be blank", card.title.isNotBlank())
            assertTrue("Card subtitle must not be blank", card.subtitle.isNotBlank())
            assertTrue("Card department must not be blank", card.department.isNotBlank())
            assertTrue("Primary label must not be blank", card.primaryLabel.isNotBlank())
            assertTrue("Primary value must not be blank", card.primaryValue.isNotBlank())
            assertTrue("Badge text must not be blank", card.badgeText.isNotBlank())
            assertTrue("Metrics should not be empty", card.metrics.isNotEmpty())
            assertTrue("Details should not be empty", card.details.isNotEmpty())

            // Test progress calculation if present
            card.progress?.let { progress ->
                assertTrue(progress.current >= 0)
                assertTrue(progress.max > 0)
                assertTrue(progress.displayPercent in 0..100)
            }

            // Test action format if present
            card.action?.let { action ->
                assertTrue(action.label.isNotBlank())
                if (action.actionType == "OPEN_URL") {
                    assertNotNull(action.url)
                    assertTrue(action.url!!.startsWith("http"))
                }
            }
        }
    }

    @Test
    fun testProgressPercentageCalculation() {
        val progress = A2UIProgress(current = 150f, max = 300f, label = "測試進度")
        assertEquals(50, progress.displayPercent)

        val fullProgress = A2UIProgress(current = 100f, max = 100f, label = "滿載")
        assertEquals(100, fullProgress.displayPercent)
    }

    @Test
    fun testDynamicCycleFluctuations() {
        val cycle0 = service.getDynamicOpenDataCards(cycleCounter = 0)
        val cycle2 = service.getDynamicOpenDataCards(cycleCounter = 2)

        val youBike0 = cycle0.first { it.sourceKey == "TAIPEI_YOUBIKE" }
        val youBike2 = cycle2.first { it.sourceKey == "TAIPEI_YOUBIKE" }

        // Both should be valid, and may have fluctuating values based on cycle counter
        assertTrue(youBike0.primaryValue.toInt() > 0)
        assertTrue(youBike2.primaryValue.toInt() > 0)
    }
}
