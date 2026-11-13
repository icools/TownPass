package com.example.townpass.data.repository

import com.example.townpass.data.model.YouBikeFilterMode
import com.example.townpass.data.service.YouBikeService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class YouBikeRepositoryTest {

    private lateinit var service: YouBikeService
    private lateinit var repository: YouBikeRepositoryImpl

    @Before
    fun setup() {
        service = YouBikeService()
        repository = YouBikeRepositoryImpl(service = service, autoStartTicker = false)
    }

    @Test
    fun service_parses_json_correctly() {
        val sampleJson = """
            [
              {
                "sno": "500101001",
                "sna": "YouBike2.0_捷運科技大樓站",
                "sarea": "大安區",
                "ar": "復興南路二段235號前",
                "Quantity": 28,
                "available_rent_bikes": 10,
                "available_return_bikes": 18,
                "latitude": 25.02605,
                "longitude": 121.5436,
                "act": "1"
              }
            ]
        """.trimIndent()

        val list = service.parseStationsJson(sampleJson)
        assertEquals(1, list.size)
        val st = list.first()
        assertEquals("500101001", st.sno)
        assertEquals("捷運科技大樓站", st.name)
        assertEquals("大安區", st.district)
        assertEquals(28, st.totalSpaces)
        assertEquals(10, st.availableBikes)
        assertEquals(18, st.emptySpaces)
        assertTrue(st.isActive)
    }

    @Test
    fun repository_initializes_and_calculates_stats() = runTest {
        val stations = repository.allStations.first { it.isNotEmpty() }
        assertTrue("Stations should not be empty", stations.isNotEmpty())

        val stats = repository.stats.first { it.totalStations > 0 }
        assertTrue(stats.totalStations >= 20)
        assertTrue(stats.totalAvailableBikes > 0)
        assertTrue(stats.totalEmptySpaces > 0)
        assertTrue(stats.totalSpaces > 0)
    }

    @Test
    fun repository_filters_by_district() = runTest {
        repository.allStations.first { it.isNotEmpty() }

        // Filter by 信義區
        repository.setDistrict("信義區")
        val xinyiStations = repository.filteredStations.first { it.isNotEmpty() && it.all { st -> st.district == "信義區" } }
        assertTrue(xinyiStations.isNotEmpty())
        assertTrue(xinyiStations.all { it.district == "信義區" })

        // Filter by 大安區
        repository.setDistrict("大安區")
        val daanStations = repository.filteredStations.first { it.isNotEmpty() && it.all { st -> st.district == "大安區" } }
        assertTrue(daanStations.isNotEmpty())
        assertTrue(daanStations.all { it.district == "大安區" })

        // Reset to 全部
        repository.setDistrict("全部")
        val allStations = repository.filteredStations.first { it.size >= 20 }
        assertTrue(allStations.size >= 20)
    }

    @Test
    fun repository_filters_by_mode() = runTest {
        repository.allStations.first { it.isNotEmpty() }

        // Mode: RENT (可借車優先)
        repository.setFilterMode(YouBikeFilterMode.RENT)
        val rentStations = repository.filteredStations.first { it.isNotEmpty() }
        assertTrue(rentStations.all { it.availableBikes > 0 })

        // Mode: RETURN (可還車優先)
        repository.setFilterMode(YouBikeFilterMode.RETURN)
        val returnStations = repository.filteredStations.first { it.isNotEmpty() }
        assertTrue(returnStations.all { it.emptySpaces > 0 })
    }

    @Test
    fun repository_searches_by_query() = runTest {
        repository.allStations.first { it.isNotEmpty() }

        repository.setSearchQuery("科技大樓")
        val results = repository.filteredStations.first { it.any { st -> st.name.contains("科技大樓") } }
        assertTrue(results.isNotEmpty())
        assertTrue(results.any { it.name.contains("科技大樓") })

        repository.setSearchQuery("non_existent_fake_station_999")
        val emptyResults = repository.filteredStations.first { it.isEmpty() }
        assertTrue(emptyResults.isEmpty())

        repository.setSearchQuery("")
        val reset = repository.filteredStations.first { it.size >= 20 }
        assertTrue(reset.size >= 20)
    }

    @Test
    fun repository_toggles_favorite() = runTest {
        val stations = repository.allStations.first { it.isNotEmpty() }
        val firstSno = stations.first().sno

        assertFalse(stations.first().isFavorite)
        repository.toggleFavorite(firstSno)

        val updated = repository.allStations.first { it.first().sno == firstSno && it.first().isFavorite }
        assertTrue(updated.first().isFavorite)

        repository.toggleFavorite(firstSno)
        val toggledBack = repository.allStations.first { it.first().sno == firstSno && !it.first().isFavorite }
        assertFalse(toggledBack.first().isFavorite)
    }
}
