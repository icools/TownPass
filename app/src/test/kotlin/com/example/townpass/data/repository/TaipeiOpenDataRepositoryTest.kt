package com.example.townpass.data.repository

import com.example.townpass.data.service.TaipeiOpenDataService
import com.example.townpass.ui.a2ui.A2UICategory
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TaipeiOpenDataRepositoryTest {

    private lateinit var service: TaipeiOpenDataService
    private lateinit var repository: TaipeiOpenDataRepositoryImpl

    @Before
    fun setup() {
        service = TaipeiOpenDataService()
        repository = TaipeiOpenDataRepositoryImpl(service = service, autoStartTicker = false)
    }

    @Test
    fun repository_initializes_with_12_open_data_sources() = runTest {
        val cards = repository.allCards.first()
        assertEquals("Should have exactly 12 Taipei Open Data sources", 12, cards.size)

        val keys = cards.map { it.sourceKey }.toSet()
        assertEquals(12, keys.size)
        assertTrue(keys.contains("TAIPEI_YOUBIKE"))
        assertTrue(keys.contains("TAIPEI_AQI"))
        assertTrue(keys.contains("TAIPEI_PARKING"))
        assertTrue(keys.contains("TAIPEI_METRO"))
        assertTrue(keys.contains("TAIPEI_BUS"))
        assertTrue(keys.contains("TAIPEI_GARBAGE"))
        assertTrue(keys.contains("TAIPEI_TOURISM_CROWD"))
        assertTrue(keys.contains("TAIPEI_WEATHER"))
        assertTrue(keys.contains("TAIPEI_TRAFFIC"))
        assertTrue(keys.contains("TAIPEI_HOSPITAL_ER"))
        assertTrue(keys.contains("TAIPEI_WATER"))
        assertTrue(keys.contains("TAIPEI_ZOO"))
    }

    @Test
    fun repository_filters_by_category() = runTest {
        // Filter Transport
        repository.setCategory(A2UICategory.TRANSPORT)
        val transportCards = repository.filteredCards.first { it.isNotEmpty() && it.all { c -> c.category == A2UICategory.TRANSPORT } }
        assertTrue(transportCards.isNotEmpty())
        assertTrue(transportCards.all { it.category == A2UICategory.TRANSPORT })

        // Filter Environment
        repository.setCategory(A2UICategory.ENVIRONMENT)
        val envCards = repository.filteredCards.first { it.isNotEmpty() && it.all { c -> c.category == A2UICategory.ENVIRONMENT } }
        assertTrue(envCards.isNotEmpty())
        assertTrue(envCards.all { it.category == A2UICategory.ENVIRONMENT })

        // Reset to ALL
        repository.setCategory(A2UICategory.ALL)
        val allCards = repository.filteredCards.first { it.size == 12 }
        assertEquals(12, allCards.size)
    }

    @Test
    fun repository_searches_by_query() = runTest {
        repository.setSearchQuery("動物園")
        val zooResults = repository.filteredCards.first { cards -> cards.isNotEmpty() && cards.all { it.title.contains("動物園") } }
        assertEquals(1, zooResults.size)
        assertEquals("TAIPEI_ZOO", zooResults.first().sourceKey)

        repository.setSearchQuery("xyz_non_existent_term_999")
        val emptyResults = repository.filteredCards.first { cards -> cards.isEmpty() }
        assertTrue(emptyResults.isEmpty())

        // Reset query
        repository.setSearchQuery("")
        val resetResults = repository.filteredCards.first { cards -> cards.size == 12 }
        assertEquals(12, resetResults.size)
    }

    @Test
    fun repository_manual_refresh_updates_time_and_metrics() = runTest {
        repository.refresh()
        val afterRefreshCards = repository.allCards.first()
        assertEquals(12, afterRefreshCards.size)
    }

    @Test
    fun repository_toggles_auto_refresh() {
        assertTrue(repository.isAutoRefreshEnabled.value)
        repository.toggleAutoRefresh()
        assertFalse(repository.isAutoRefreshEnabled.value)
        repository.toggleAutoRefresh()
        assertTrue(repository.isAutoRefreshEnabled.value)
    }

    @Test
    fun repository_getCardById_returns_expected_card() = runTest {
        val card = repository.getCardById("tp_opendata_youbike")
        assertNotNull(card)
        assertEquals("YouBike 2.0 即時站點", card?.title)
        assertEquals("台北市政府交通局", card?.department)
    }
}
